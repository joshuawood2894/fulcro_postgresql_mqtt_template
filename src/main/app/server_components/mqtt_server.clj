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
    [clojure.string :as s]))

(defn handle-json
  [^String topic metadata ^bytes payload]
  (if (= (try
           (cheshire/parse-string (String. payload "UTF-8"))
           (catch Exception e -1)) -1)
    (do
      (log/info "Garbage message received over MQTT")
      nil)
    (let [data (cheshire/parse-string (String. payload "UTF-8"))]
      (log/info "JSON message received over MQTT!"))))

(defn mqtt-start []
  (log/info "Starting MQTT Server")
  (let [id   (mh/generate-id)
        hostname (-> config :mqtt-spec :hostname)
        username (-> config :mqtt-spec :username)
        password (-> config :mqtt-spec :password)
        qos (-> config :mqtt-spec :qos)
        conn (mh/connect
               hostname
               {:client-id id
                :opts {:username username
                       :password password
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

