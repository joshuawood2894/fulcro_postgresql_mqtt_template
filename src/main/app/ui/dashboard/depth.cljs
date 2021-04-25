(ns app.ui.dashboard.depth
  (:require
    [app.model.dashboard.depth :as md]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.depth :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc DepthChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "Water Depth in Centimeters"
                          md/toggle-depth-settings! (:toggle-settings props))
             :cover     (if (empty? (:depth props))
                          (div {:style {:width  485 :height 275}}
                            (ant/ant-empty {:style {:paddingTop "15%"}}))
                          (dc/create-rechart (:chart-type props)
                           {:data         (:depth props)
                            :x-axis-label "Time"
                            :y-axis-label "Depth in Centimeters"
                            :unit-symbol  "cm"
                            :data-key     "depth"
                            :id           "depth-id"
                            :color        (:color props)
                            :min-bound    (:min-bound props)
                            :max-bound    (:max-bound props)}))
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          md/set-depth-start-end-datetime!
                          md/set-depth-color!
                          md/set-depth-min-bound!
                          md/set-depth-max-bound!
                          md/redo-depth-min-max-bound!
                          (:chart-type props)
                          md/set-depth-chart-type!)}))

(def ui-depth-chart (comp/factory DepthChart))

(defsc DepthData [this {:depth-data/keys [id depth
                                          start-date end-date
                                          toggle-settings color
                                          min-bound max-bound
                                          chart-type]
                        :as              props}]
  {:query         [:depth-data/id {:depth-data/depth (comp/get-query dl/DepthReading)}
                   :depth-data/start-date :depth-data/end-date
                   :depth-data/toggle-settings :depth-data/color
                   :depth-data/min-bound :depth-data/max-bound
                   :depth-data/chart-type]
   :ident         :depth-data/id
   :initial-state {:depth-data/id              1
                   :depth-data/toggle-settings false
                   :depth-data/min-bound       js/NaN
                   :depth-data/max-bound       js/NaN
                   :depth-data/chart-type      "line"
                   :depth-data/color           ant/blue-primary
                   :depth-data/start-date      (js/Date.)
                   :depth-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :depth-data/depth           [{:id    1 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:24:00")
                                                 :depth 393}
                                                {:id    2 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:25:00")
                                                 :depth 295}
                                                {:id    3 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:26:00")
                                                 :depth 300}
                                                {:id    4 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:27:00")
                                                 :depth 400}
                                                {:id    5 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:28:00")
                                                 :depth 450}
                                                {:id    6 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:29:00")
                                                 :depth 380}
                                                {:id    7 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:30:00")
                                                 :depth 375}]}}
  (ui-depth-chart {:depth           depth
                   :toggle-settings toggle-settings
                   :color           color
                   :min-bound       min-bound
                   :max-bound       max-bound
                   :chart-type      chart-type}))

(def ui-depth-data (comp/factory DepthData))
