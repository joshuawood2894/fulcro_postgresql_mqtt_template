(ns app.server-components.mqtt-server
  (:require
    [mount.core :refer [defstate]]
    [app.server-components.config :refer [config]]
    [clojurewerkz.machine-head.client :as mh]
    [app.model.database :as db]
    [taoensso.timbre :as log]
    [cheshire.core :as cheshire]
    [honeysql.core :as sql]
    [honeysql.helpers :as h]
    [honeysql-postgres.helpers :as pgh]
    [clj-time.coerce :as clj-time]
    [clojure.string :as s]
    [com.fulcrologic.fulcro.networking.websocket-protocols :refer [push]]
    [app.server-components.websockets :refer [websockets]]))

(defn get-timestamp [content]
  (-> (get-in content ["system" "unix-time"])
      str
      Long/decode
      clj-time/from-epoch
      clj-time/to-sql-time))

(defn handle-data-message [content]
  (let [gateway-uuid (get-in content ["system" "uuid"])
        type (get-in content ["system" "type"])
        iccid (get-in content ["system" "iccid"])
        battery (get-in content ["system" "battery"])
        band (get-in content ["system" "band"])
        rsrp (get-in content ["system" "rsrp"])
        unix-time (get-timestamp content)
        worker-type (get-in content ["worker" "type"])
        worker-uuid (get-in content ["worker" "uuid"])
        measurement (get-in content ["content" "value"])
        sensor-id (get-in content ["content" "sensor-id"])]
    (do
      (db/execute!
        db/pool
        (-> (h/insert-into :gateway)
            (h/values [{:id           (sql/call :cast gateway-uuid :uuid)
                        :gateway-type type
                        :iccid        iccid}])
            (pgh/upsert (-> (pgh/on-conflict :id)
                            (pgh/do-nothing)))))
      (db/execute!
        db/pool
        (-> (h/insert-into :system-readings)
            (h/values [{:gateway-id (sql/call :cast gateway-uuid :uuid)
                        :battery    battery
                        :band       band
                        :rsrp       rsrp
                        :unix-time  unix-time}])))
      (db/execute!
        db/pool
        (-> (h/insert-into :workers)
            (h/values [{:id          (sql/call :cast worker-uuid :uuid)
                        :gateway-id  (sql/call :cast gateway-uuid :uuid)
                        :worker-type worker-type}])
            (pgh/upsert (-> (pgh/on-conflict :id)
                            (pgh/do-nothing)))))
      (let [id (db/execute!
                 db/pool
                 (-> (h/insert-into :data-readings)
                     (h/values [{:worker-id  (sql/call :cast worker-uuid :uuid)
                                 :unix-time  unix-time
                                 :data-value measurement
                                 :sensor-id  sensor-id}])
                     (pgh/returning :id)))]
        (log/info "Data message received: " sensor-id)
        id))))

(defn handle-gps-message [content]
  (let [gateway-uuid (get-in content ["system" "uuid"])
        type (get-in content ["system" "type"])
        iccid (get-in content ["system" "iccid"])
        battery (get-in content ["system" "battery"])
        band (get-in content ["system" "band"])
        rsrp (get-in content ["system" "rsrp"])
        unix-time (get-timestamp content)
        worker-type (get-in content ["worker" "type"])
        worker-uuid (get-in content ["worker" "uuid"])
        latitude (get-in content ["content" "latitude"])
        longitude (get-in content ["content" "longitude"])
        speed (get-in content ["content" "speed"])
        heading (get-in content ["content" "heading"])
        hdop (get-in content ["content" "hdop"])
        nsv (get-in content ["content" "nsv"])]
    (do
      (db/execute!
        db/pool
        (-> (h/insert-into :gateway)
            (h/values [{:id           (sql/call :cast gateway-uuid :uuid)
                        :gateway-type type
                        :iccid        iccid}])
            (pgh/upsert (-> (pgh/on-conflict :id)
                            (pgh/do-nothing)))))
      (db/execute!
        db/pool
        (-> (h/insert-into :system-readings)
            (h/values [{:gateway-id (sql/call :cast gateway-uuid :uuid)
                        :battery    battery
                        :band       band
                        :rsrp       rsrp
                        :unix-time  unix-time}])))
      (db/execute!
        db/pool
        (-> (h/insert-into :workers)
            (h/values [{:id          (sql/call :cast worker-uuid :uuid)
                        :gateway-id  (sql/call :cast gateway-uuid :uuid)
                        :worker-type worker-type}])
            (pgh/upsert (-> (pgh/on-conflict :id)
                            (pgh/do-nothing)))))
      (let [id (db/execute!
                 db/pool
                 (-> (h/insert-into :gps-readings)
                     (h/values [{:worker-id (sql/call :cast worker-uuid :uuid)
                                 :unix-time unix-time
                                 :latitude  latitude
                                 :longitude longitude
                                 :speed     speed
                                 :heading   heading
                                 :hdop      hdop
                                 :nsv       nsv}])
                     (pgh/returning :id)))]
        (log/info "GPS message received: lat(" latitude ") lon(" longitude ")")
        id))))

(defn handle-json
  [^String topic metadata ^bytes payload]
  (if (= (try
           (cheshire/parse-string (String. payload "UTF-8"))
           (catch Exception e -1)) -1)
    (do
      (log/info "Garbage message received over MQTT")
      nil)
    (let [data (cheshire/parse-string (String. payload "UTF-8"))
          msg-type (data "msg-type")]
      (log/info "JSON message received over MQTT!")
      (let [client-uid (-> @(:connected-uids websockets)
                           :any
                           first)]
        (cond
          (= msg-type "data") (let [id (handle-data-message data)]
                                (push websockets client-uid :topic-data
                                      (conj data {:id (:data-readings/id (into {} id))})))
          (= msg-type "gps") (let [id (handle-gps-message data)]
                               (push websockets client-uid :topic-data
                                     (conj data {:id (:gps-readings/id (into {} id))}))))))))

(defn mqtt-start []
  (log/info "Starting MQTT Server")
  (let [id (mh/generate-id)
        hostname (-> config :mqtt-spec :hostname)
        username (-> config :mqtt-spec :username)
        password (-> config :mqtt-spec :password)
        qos (-> config :mqtt-spec :qos)
        conn (mh/connect
               hostname
               {:client-id id
                :opts      {:username       username
                            :password       password
                            :auto-reconnect true}})]
    (mh/subscribe conn {"#" qos} handle-json)
    conn)
  )

(defn mqtt-stop
  [conn]
  (mh/disconnect conn))

(defstate mqtt-server
          :start (mqtt-start)
          :stop (mqtt-stop mqtt-server))

