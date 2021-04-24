(ns app.ui.dashboard.air-temperature
  (:require
    [app.model.dashboard.air-temperature :as mat]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.air-temperature :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc AirTemperatureChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "H2S in Parts per Million"
                          mat/toggle-air-temperature-settings! (:toggle-settings props))
             :cover     (dc/create-rechart (:chart-type props)
                          {:data         (:air-temperature props)
                           :x-axis-label "Time"
                           :y-axis-label "Degrees Celsius"
                           :unit-symbol  (char 176)
                           :data-key     "air-temperature"
                           :id           "air-temperature-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mat/set-air-temperature-start-end-datetime!
                          mat/set-air-temperature-color!
                          mat/set-air-temperature-min-bound!
                          mat/set-air-temperature-max-bound!
                          mat/redo-air-temperature-min-max-bound!
                          (:chart-type props)
                          mat/set-air-temperature-chart-type!)}))

(def ui-air-temperature-chart (comp/factory AirTemperatureChart))

(defsc AirTemperatureData [this {:air-temperature-data/keys [id air-temperature
                                                             start-date end-date
                                                             toggle-settings color
                                                             min-bound max-bound
                                                             chart-type]
                                 :as                        props}]
  {:query         [:air-temperature-data/id {:air-temperature-data/air-temperature (comp/get-query dl/AirTemperatureReading)}
                   :air-temperature-data/start-date :air-temperature-data/end-date
                   :air-temperature-data/toggle-settings :air-temperature-data/color
                   :air-temperature-data/min-bound :air-temperature-data/max-bound
                   :air-temperature-data/chart-type]
   :ident         :air-temperature-data/id
   :initial-state {:air-temperature-data/id              1
                   :air-temperature-data/toggle-settings false
                   :air-temperature-data/min-bound       js/NaN
                   :air-temperature-data/max-bound       js/NaN
                   :air-temperature-data/chart-type      "line"
                   :air-temperature-data/color           ant/blue-primary
                   :air-temperature-data/start-date      (js/Date.)
                   :air-temperature-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :air-temperature-data/air-temperature [{:id 1 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:24:00") :air-temperature 0}
                                                          {:id 2 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:25:00") :air-temperature 0.1}
                                                          {:id 3 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:26:00") :air-temperature 0}
                                                          {:id 4 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:27:00") :air-temperature 0.2}
                                                          {:id 5 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:28:00") :air-temperature 0}
                                                          {:id 6 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:29:00") :air-temperature 0}
                                                          {:id 7 :time (js/Date.
                                                                         "March
                                                                         17, 2021
                                                                         15:30:00") :air-temperature 0.1}]}}
  (ui-air-temperature-chart {:air-temperature air-temperature
                             :toggle-settings toggle-settings
                             :color           color
                             :min-bound       min-bound
                             :max-bound       max-bound
                             :chart-type      chart-type}))

(def ui-air-temperature-data (comp/factory AirTemperatureData))