(ns app.ui.dashboard.air-h2s
  (:require
    [app.model.dashboard.air-h2s :as mah]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.air-h2s :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc AirH2SChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "H2S in Parts per Million"
                          mah/toggle-air-h2s-settings! (:toggle-settings props))
             :cover     (if (empty? (:air-h2s props))
                          (div {:style {:width  485 :height 275}}
                            (ant/ant-empty {:style {:paddingTop "15%"}}))
                          (dc/create-rechart (:chart-type props)
                           {:data         (:air-h2s props)
                            :x-axis-label "Time"
                            :y-axis-label "PPM"
                            :unit-symbol  ""
                            :data-key     "air-h2s"
                            :id           "air-h2s-id"
                            :color        (:color props)
                            :min-bound    (:min-bound props)
                            :max-bound    (:max-bound props)}))
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mah/set-air-h2s-start-end-datetime!
                          mah/set-air-h2s-color!
                          mah/set-air-h2s-min-bound!
                          mah/set-air-h2s-max-bound!
                          mah/redo-air-h2s-min-max-bound!
                          (:chart-type props)
                          mah/set-air-h2s-chart-type!)}))

(def ui-air-h2s-chart (comp/factory AirH2SChart))

(defsc AirH2SData [this {:air-h2s-data/keys [id air-h2s
                                             start-date end-date
                                             toggle-settings color
                                             min-bound max-bound
                                             chart-type]
                         :as                props}]
  {:query         [:air-h2s-data/id {:air-h2s-data/air-h2s (comp/get-query dl/AirH2SReading)}
                   :air-h2s-data/start-date :air-h2s-data/end-date
                   :air-h2s-data/toggle-settings :air-h2s-data/color
                   :air-h2s-data/min-bound :air-h2s-data/max-bound
                   :air-h2s-data/chart-type]
   :ident         :air-h2s-data/id
   :initial-state {:air-h2s-data/id              1
                   :air-h2s-data/toggle-settings false
                   :air-h2s-data/min-bound       js/NaN
                   :air-h2s-data/max-bound       js/NaN
                   :air-h2s-data/chart-type      "line"
                   :air-h2s-data/color           ant/blue-primary
                   :air-h2s-data/start-date      (js/Date.)
                   :air-h2s-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :air-h2s-data/air-h2s         [{:id 1 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:24:00") :air-h2s 0}
                                                  {:id 2 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:25:00") :air-h2s 0.1}
                                                  {:id 3 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:26:00") :air-h2s 0}
                                                  {:id 4 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:27:00") :air-h2s 0.2}
                                                  {:id 5 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:28:00") :air-h2s 0}
                                                  {:id 6 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:29:00") :air-h2s 0}
                                                  {:id 7 :time (js/Date.
                                                                 "March
                                                                 17, 2021
                                                                 15:30:00") :air-h2s 0.1}]}}
  (ui-air-h2s-chart {:air-h2s         air-h2s
                     :toggle-settings toggle-settings
                     :color           color
                     :min-bound       min-bound
                     :max-bound       max-bound
                     :chart-type      chart-type}))

(def ui-air-h2s-data (comp/factory AirH2SData))

