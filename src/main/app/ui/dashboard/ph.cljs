(ns app.ui.dashboard.ph
  (:require
    [app.model.dashboard.ph :as mph]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.ph :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc PHChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "PH"
                          mph/toggle-ph-settings! (:toggle-settings props))
             :cover     (dc/create-rechart (:chart-type props)
                          {:data         (:ph props)
                           :x-axis-label "Time"
                           :y-axis-label "PH"
                           :unit-symbol  ""
                           :data-key     "ph"
                           :id           "ph-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mph/set-ph-start-end-datetime!
                          mph/set-ph-color!
                          mph/set-ph-min-bound!
                          mph/set-ph-max-bound!
                          mph/redo-ph-min-max-bound!
                          (:chart-type props)
                          mph/set-ph-chart-type!)}))

(def ui-ph-chart (comp/factory PHChart))

(defsc PHData [this {:ph-data/keys [id ph
                                    start-date end-date
                                    toggle-settings color
                                    min-bound max-bound
                                    chart-type]
                     :as           props}]
  {:query         [:ph-data/id {:ph-data/ph (comp/get-query dl/PHReading)}
                   :ph-data/start-date :ph-data/end-date
                   :ph-data/toggle-settings :ph-data/color
                   :ph-data/min-bound :ph-data/max-bound
                   :ph-data/chart-type]
   :ident         :ph-data/id
   :initial-state {:ph-data/id              1
                   :ph-data/toggle-settings false
                   :ph-data/min-bound       js/NaN
                   :ph-data/max-bound       js/NaN
                   :ph-data/chart-type      "line"
                   :ph-data/color           ant/blue-primary
                   :ph-data/start-date      (js/Date.)
                   :ph-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :ph-data/ph              [{:id 1 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:24:00") :ph 4}
                                             {:id 2 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:25:00") :ph 4.2}
                                             {:id 3 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:26:00") :ph 4.5}
                                             {:id 4 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:27:00") :ph 4.2}
                                             {:id 5 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:28:00") :ph 5}
                                             {:id 6 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:29:00") :ph 4.8}
                                             {:id 7 :time (js/Date.
                                                            "March
                                                            17, 2021
                                                            15:30:00") :ph 4.7}]}}
  (ui-ph-chart {:ph              ph
                :toggle-settings toggle-settings
                :color           color
                :min-bound       min-bound
                :max-bound       max-bound
                :chart-type      chart-type}))

(def ui-ph-data (comp/factory PHData))
