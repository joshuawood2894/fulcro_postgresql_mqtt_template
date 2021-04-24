(ns app.model.push-handler
  (:require
    [app.ui.data-logger.air-h2s :as dlah]
    [app.ui.data-logger.pressure :as dlp]
    [app.ui.data-logger.humidity :as dlh]
    [app.ui.data-logger.air-temperature :as dlat]
    [app.ui.data-logger.ph :as dlph]
    [app.ui.data-logger.conductivity :as dlc]
    [app.ui.data-logger.water-temperature :as dlwt]
    [app.ui.data-logger.depth :as dld]
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    ;[app.application :refer [SPA]]
    ))

;(merge/merge-component! SPA HumidityReading {:humidity-reading/id       16
;                                             :humidity-reading/time     (js/Date.)
;                                             :humidity-reading/humidity 42}
;                        :append [:humidity-data/id 1 :humidity-data/humidity])

(defn push-handler [{:keys [topic msg] :as data}]
  (let [msg-type (msg "msg-type")
        worker-type (get-in msg ["worker" "type"])
        id (:id msg)
        unix-time (get-in msg ["system" "unix-time"])]
    (cond
      (and (= msg-type "data") (= worker-type "submerged"))
      (let [sensor-id (get-in msg ["content" "sensor-id"])]
        (cond
          (= sensor-id "water-temperature")
          (js/console.log "PUSH: " worker-type sensor-id)
          (= sensor-id "conductivity")
          (js/console.log "PUSH: " worker-type sensor-id)
          (= sensor-id "ph")
          (js/console.log "PUSH: " worker-type sensor-id)))
      (and (= msg-type "data") (= worker-type "environmental"))
      (let [sensor-id (get-in msg ["content" "sensor-id"])]
        (cond
          (= sensor-id "air-temperature")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            ;(merge/merge-component! app.application/SPA dlat/AirTemperatureReading
            ;                        {:temperature-reading/id id
            ;                         :temperature-reading/time (js/Date. (* 1000 unix-time))
            ;                         :temperature-reading/temperature (get-in msg ["content" "value"])}
            ;                        :append [:temperature-data/id 1 :temperature-data/temperature])
            )
          (= sensor-id "humidity")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            ;(merge/merge-component! app.application/SPA dlh/HumidityReading
            ;                        {:humidity-reading/id id
            ;                         :humidity-reading/time (js/Date. (* 1000 unix-time))
            ;                         :humidity-reading/humidity (get-in msg ["content" "value"])}
            ;                        :append [:humidity-data/id 1 :humidity-data/humidity])
            )
          (= sensor-id "pressure")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            ;(merge/merge-component! app.application/SPA dlp/PressureReading
            ;                        {:pressure-reading/id id
            ;                         :pressure-reading/time (js/Date. (* 1000 unix-time))
            ;                         :pressure-reading/pressure (get-in msg ["content" "value"])}
            ;                        :append [:pressure-data/id 1 :pressure-data/pressure])
            )
          (= sensor-id "air-h2s")
          (js/console.log "PUSH: " worker-type sensor-id)))
      (and (= msg-type "data") (= worker-type "controller"))
      (let [sensor-id (get-in msg ["content" "sensor-id"])]
        (cond
          (= sensor-id "depth")
          (js/console.log "PUSH: " worker-type sensor-id))))))

(defn state-callback [before after]
  (log/info "before: " before "after: " after))

(comment
  (merge/merge-component! app.application/SPA dlh/HumidityReading
    {:humidity-reading/id       16
     :humidity-reading/time     (js/Date.)
     :humidity-reading/humidity 42}
    :append [:humidity-data/id 1 :humidity-data/humidity])

  (for [x (vec (range 8 101))]
    (merge/merge-component! app.application/SPA dlh/HumidityReading
      {:humidity-reading/id       x
       :humidity-reading/time     (js/Date.)
       :humidity-reading/humidity (+ (rand-nth (vec (range -3 4))) 40)}
      :append [:humidity-data/id 1 :humidity-data/humidity]))
  )