(ns app.ui.dashboard.humidity
  (:require
    [app.model.dashboard.humidity :as mh]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.humidity :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc HumidityChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "Percent Humidity"
                          mh/toggle-humidity-settings! (:toggle-settings props))
             :cover     (if (empty? (:humidity props))
                          (div {:style {:width  485 :height 275}}
                            (ant/ant-empty {:style {:paddingTop "15%"}}))
                          (dc/create-rechart (:chart-type props)
                           {:data         (:humidity props)
                            :x-axis-label "Time"
                            :y-axis-label "Percentage"
                            :unit-symbol  "%"
                            :data-key     "humidity"
                            :id           "humidity-id"
                            :color        (:color props)
                            :min-bound    (:min-bound props)
                            :max-bound    (:max-bound props)}))
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mh/set-humidity-start-end-datetime!
                          mh/set-humidity-color!
                          mh/set-humidity-min-bound!
                          mh/set-humidity-max-bound!
                          mh/redo-humidity-min-max-bound!
                          (:chart-type props)
                          mh/set-humidity-chart-type!)}))

(def ui-humidity-chart (comp/factory HumidityChart))

(defsc HumidityData [this {:humidity-data/keys [humidity
                                                start-date end-date
                                                toggle-settings color
                                                min-bound max-bound
                                                chart-type]
                           :as                 props}]
  {:query         [{:humidity-data/humidity (comp/get-query dl/HumidityReading)}
                   :humidity-data/start-date :humidity-data/end-date
                   :humidity-data/toggle-settings :humidity-data/color
                   :humidity-data/min-bound :humidity-data/max-bound
                   :humidity-data/chart-type]
   :ident         (fn [] [:component/id :humidity-data])
   :initial-state {:humidity-data/toggle-settings false
                   :humidity-data/min-bound       js/NaN
                   :humidity-data/max-bound       js/NaN
                   :humidity-data/chart-type      "line"
                   :humidity-data/color           ant/blue-primary
                   :humidity-data/start-date      (js/Date.)
                   :humidity-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :humidity-data/humidity        [{:id       1 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:24:00")
                                                    :humidity 40}
                                                   {:id       2 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:25:00")
                                                    :humidity 45}
                                                   {:id       3 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:26:00")
                                                    :humidity 50}
                                                   {:id       4 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:27:00")
                                                    :humidity 48}
                                                   {:id       5 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:28:00")
                                                    :humidity 49}
                                                   {:id       6 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:29:00")
                                                    :humidity 47}
                                                   {:id       7 :time (js/Date.
                                                                        "March
                                                                        17, 2021
                                                                        15:30:00")
                                                    :humidity 45}]}}
  (ui-humidity-chart {:humidity        humidity
                      :toggle-settings toggle-settings
                      :color           color
                      :min-bound       min-bound
                      :max-bound       max-bound
                      :chart-type      chart-type}))

(def ui-humidity-data (comp/factory HumidityData))
