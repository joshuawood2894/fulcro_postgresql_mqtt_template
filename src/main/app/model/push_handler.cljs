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
    [app.application :refer [SPA]]))

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
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlwt/WaterTemperatureReading
              {:water-temperature-reading/id   id
               :water-temperature-reading/time (js/Date. (* 1000 unix-time))
               :water-temperature-reading/water-temperature
                                               (get-in msg ["content" "value"])}
              :append [:water-temperature-data/id 1 :water-temperature-data/water-temperature]))
          (= sensor-id "conductivity")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlc/ConductivityReading
              {:conductivity-reading/id   id
               :conductivity-reading/time (js/Date. (* 1000 unix-time))
               :conductivity-reading/conductivity
                                          (get-in msg ["content" "value"])}
              :append [:conductivity-data/id 1 :conductivity-data/conductivity]))
          (= sensor-id "ph")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlph/PHReading
              {:ph-reading/id   id
               :ph-reading/time (js/Date. (* 1000 unix-time))
               :ph-reading/ph
                                (get-in msg ["content" "value"])}
              :append [:ph-data/id 1 :ph-data/ph]))))
      (and (= msg-type "data") (= worker-type "environmental"))
      (let [sensor-id (get-in msg ["content" "sensor-id"])]
        (cond
          (= sensor-id "air-temperature")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlat/AirTemperatureReading
              {:air-temperature-reading/id   id
               :air-temperature-reading/time (js/Date. (* 1000 unix-time))
               :air-temperature-reading/air-temperature
                                             (get-in msg ["content" "value"])}
              :append [:air-temperature-data/id 1 :air-temperature-data/air-temperature]))
          (= sensor-id "humidity")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlh/HumidityReading
              {:humidity-reading/id       id
               :humidity-reading/time     (js/Date. (* 1000 unix-time))
               :humidity-reading/humidity (get-in msg ["content" "value"])}
              :append [:humidity-data/id 1 :humidity-data/humidity]))
          (= sensor-id "pressure")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlp/PressureReading
              {:pressure-reading/id       id
               :pressure-reading/time     (js/Date. (* 1000 unix-time))
               :pressure-reading/pressure (get-in msg ["content" "value"])}
              :append [:pressure-data/id 1 :pressure-data/pressure]))
          (= sensor-id "air-h2s")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dlah/AirH2SReading
              {:air-h2s-reading/id      id
               :air-h2s-reading/time    (js/Date. (* 1000 unix-time))
               :air-h2s-reading/air-h2s (get-in msg ["content" "value"])}
              :append [:air-h2s-data/id 1 :air-h2s-data/air-h2s]))))
      (and (= msg-type "data") (= worker-type "controller"))
      (let [sensor-id (get-in msg ["content" "sensor-id"])]
        (cond
          (= sensor-id "depth")
          (do
            (js/console.log "PUSH: " worker-type sensor-id)
            (merge/merge-component! SPA dld/DepthReading
              {:depth-reading/id      id
               :depth-reading/time    (js/Date. (* 1000 unix-time))
               :depth-reading/depth (get-in msg ["content" "value"])}
              :append [:depth-data/id 1 :depth-data/depth])))))))

(defn state-callback [before after]
  (log/info "before: " before "after: " after))

(comment
  ;(merge/merge-component! app.application/SPA dlh/HumidityReading
  ;  {:humidity-reading/id       16
  ;   :humidity-reading/time     (js/Date.)
  ;   :humidity-reading/humidity 42}
  ;  :append [:humidity-data/id 1 :humidity-data/humidity])
  ;
  ;(for [x (vec (range 8 101))]
  ;  (merge/merge-component! app.application/SPA dlh/HumidityReading
  ;    {:humidity-reading/id       x
  ;     :humidity-reading/time     (js/Date.)
  ;     :humidity-reading/humidity (+ (rand-nth (vec (range -3 4))) 40)}
  ;    :append [:humidity-data/id 1 :humidity-data/humidity]))
  )