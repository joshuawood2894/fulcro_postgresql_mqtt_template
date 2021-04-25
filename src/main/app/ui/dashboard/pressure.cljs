(ns app.ui.dashboard.pressure
  (:require
    [app.model.dashboard.pressure :as mp]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.pressure :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc PressureChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "Pressure in Hectopascals"
                          mp/toggle-pressure-settings! (:toggle-settings props))
             :cover     (if (empty? (:pressure props))
                          (div {:style {:width  485 :height 275}}
                            (ant/ant-empty {:style {:paddingTop "15%"}}))
                          (dc/create-rechart (:chart-type props)
                           {:data         (:pressure props)
                            :x-axis-label "Time"
                            :y-axis-label "Pressure (hPa)"
                            :unit-symbol  ""
                            :data-key     "pressure"
                            :id           "pressure-id"
                            :color        (:color props)
                            :min-bound    (:min-bound props)
                            :max-bound    (:max-bound props)}))
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mp/set-pressure-start-end-datetime!
                          mp/set-pressure-color!
                          mp/set-pressure-min-bound!
                          mp/set-pressure-max-bound!
                          mp/redo-pressure-min-max-bound!
                          (:chart-type props)
                          mp/set-pressure-chart-type!)}))

(def ui-pressure-chart (comp/factory PressureChart))

(defsc PressureData [this {:pressure-data/keys [id pressure
                                                start-date end-date
                                                toggle-settings color
                                                min-bound max-bound
                                                chart-type]
                           :as                 props}]
  {:query         [:pressure-data/id {:pressure-data/pressure (comp/get-query dl/PressureReading)}
                   :pressure-data/start-date :pressure-data/end-date
                   :pressure-data/toggle-settings :pressure-data/color
                   :pressure-data/min-bound :pressure-data/max-bound
                   :pressure-data/chart-type]
   :ident         :pressure-data/id
   :initial-state {:pressure-data/id              1
                   :pressure-data/toggle-settings false
                   :pressure-data/min-bound       js/NaN
                   :pressure-data/max-bound       js/NaN
                   :pressure-data/chart-type      "line"
                   :pressure-data/color           ant/blue-primary
                   :pressure-data/start-date      (js/Date.)
                   :pressure-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :pressure-data/pressure        [{:id       1 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:24:00")
                                                    :pressure 1000}
                                                   {:id       2 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:25:00")
                                                    :pressure 999}
                                                   {:id       3 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:26:00")
                                                    :pressure 998}
                                                   {:id       4 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:27:00")
                                                    :pressure 1001}
                                                   {:id       5 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:28:00")
                                                    :pressure 1001}
                                                   {:id       6 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:29:00")
                                                    :pressure 998}
                                                   {:id       7 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:30:00")
                                                    :pressure 997}]}}
  (ui-pressure-chart {:pressure        pressure
                      :toggle-settings toggle-settings
                      :color           color
                      :min-bound       min-bound
                      :max-bound       max-bound
                      :chart-type      chart-type}))

(def ui-pressure-data (comp/factory PressureData))
