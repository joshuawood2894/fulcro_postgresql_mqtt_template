(ns app.ui.dashboard.water-temperature
  (:require
    [app.model.dashboard.water-temperature :as mwt]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.water-temperature :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc WaterTemperatureChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "Water Temperature in Degrees
             Celsius"
                          mwt/toggle-water-temperature-settings! (:toggle-settings props))
             :cover     (if (empty? (:water-temperature props))
                          (div {:style {:width  485 :height 275}}
                            (ant/ant-empty {:style {:paddingTop "15%"}}))
                          (dc/create-rechart (:chart-type props)
                           {:data         (:water-temperature props)
                            :x-axis-label "Time"
                            :y-axis-label "Water Temperature"
                            :unit-symbol  (char 176)
                            :data-key     "water-temperature"
                            :id           "water-temperature-id"
                            :color        (:color props)
                            :min-bound    (:min-bound props)
                            :max-bound    (:max-bound props)}))
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mwt/set-water-temperature-start-end-datetime!
                          mwt/set-water-temperature-color!
                          mwt/set-water-temperature-min-bound!
                          mwt/set-water-temperature-max-bound!
                          mwt/redo-water-temperature-min-max-bound!
                          (:chart-type props)
                          mwt/set-water-temperature-chart-type!)}))

(def ui-water-temperature-chart (comp/factory WaterTemperatureChart))

(defsc WaterTemperatureData [this {:water-temperature-data/keys [water-temperature
                                                                 start-date end-date
                                                                 toggle-settings color
                                                                 min-bound max-bound
                                                                 chart-type]
                                   :as                          props}]
  {:query         [{:water-temperature-data/water-temperature (comp/get-query dl/WaterTemperatureReading)}
                   :water-temperature-data/start-date :water-temperature-data/end-date
                   :water-temperature-data/toggle-settings :water-temperature-data/color
                   :water-temperature-data/min-bound :water-temperature-data/max-bound
                   :water-temperature-data/chart-type]
   :ident         (fn [] [:component/id :water-temperature-data])
   :initial-state {:water-temperature-data/toggle-settings   false
                   :water-temperature-data/min-bound         js/NaN
                   :water-temperature-data/max-bound         js/NaN
                   :water-temperature-data/chart-type        "line"
                   :water-temperature-data/color             ant/blue-primary
                   :water-temperature-data/start-date        (js/Date.)
                   :water-temperature-data/end-date          (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :water-temperature-data/water-temperature [{:id                1 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:24:00")
                                                               :water-temperature 22}
                                                              {:id                2 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:25:00")
                                                               :water-temperature 22}
                                                              {:id                3 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:26:00")
                                                               :water-temperature 23}
                                                              {:id                4 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:27:00")
                                                               :water-temperature 21}
                                                              {:id                5 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:28:00")
                                                               :water-temperature 20}
                                                              {:id                6 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:29:00")
                                                               :water-temperature 18}
                                                              {:id                7 :time (js/Date.
                                                                                            "March
                                                                                            17, 2021
                                                                                            15:30:00")
                                                               :water-temperature 19}]}}
  (ui-water-temperature-chart {:water-temperature water-temperature
                               :toggle-settings   toggle-settings
                               :color             color
                               :min-bound         min-bound
                               :max-bound         max-bound
                               :chart-type        chart-type}))

(def ui-water-temperature-data (comp/factory WaterTemperatureData))
