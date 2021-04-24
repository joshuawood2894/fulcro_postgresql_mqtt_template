(ns app.ui.dashboard.conductivity
  (:require
    [app.model.dashboard.conductivity :as mc]
    [app.ui.dashboard.config :as dc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger.conductivity :as dl]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc ConductivityChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (dc/create-card-title "H2S in Parts per Million"
                          mc/toggle-conductivity-settings! (:toggle-settings props))
             :cover     (dc/create-rechart (:chart-type props)
                          {:data         (:conductivity props)
                           :x-axis-label "Time"
                           :y-axis-label "Conductivity in Microsiemens"
                           :unit-symbol  "uS"
                           :data-key     "conductivity"
                           :id           "conductivity-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (dc/create-dropdown-settings (:toggle-settings props)
                          mc/set-conductivity-start-end-datetime!
                          mc/set-conductivity-color!
                          mc/set-conductivity-min-bound!
                          mc/set-conductivity-max-bound!
                          mc/redo-conductivity-min-max-bound!
                          (:chart-type props)
                          mc/set-conductivity-chart-type!)}))

(def ui-conductivity-chart (comp/factory ConductivityChart))

(defsc ConductivityData [this {:conductivity-data/keys [id conductivity
                                                        start-date end-date
                                                        toggle-settings color
                                                        min-bound max-bound
                                                        chart-type]
                               :as                     props}]
  {:query         [:conductivity-data/id {:conductivity-data/conductivity (comp/get-query dl/ConductivityReading)}
                   :conductivity-data/start-date :conductivity-data/end-date
                   :conductivity-data/toggle-settings :conductivity-data/color
                   :conductivity-data/min-bound :conductivity-data/max-bound
                   :conductivity-data/chart-type]
   :ident         :conductivity-data/id
   :initial-state {:conductivity-data/id              1
                   :conductivity-data/toggle-settings false
                   :conductivity-data/min-bound       js/NaN
                   :conductivity-data/max-bound       js/NaN
                   :conductivity-data/chart-type      "line"
                   :conductivity-data/color           ant/blue-primary
                   :conductivity-data/start-date      (js/Date.)
                   :conductivity-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :conductivity-data/conductivity    [{:id 1 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:24:00") :conductivity 110}
                                                       {:id 2 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:25:00") :conductivity 270}
                                                       {:id 3 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:26:00") :conductivity 250}
                                                       {:id 4 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:27:00") :conductivity 180}
                                                       {:id 5 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:28:00") :conductivity 175}
                                                       {:id 6 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:29:00") :conductivity 200}
                                                       {:id 7 :time (js/Date.
                                                                      "March
                                                                      17, 2021
                                                                      15:30:00") :conductivity 175}]}}
  (ui-conductivity-chart {:conductivity    conductivity
                          :toggle-settings toggle-settings
                          :color           color
                          :min-bound       min-bound
                          :max-bound       max-bound
                          :chart-type      chart-type}))

(def ui-conductivity-data (comp/factory ConductivityData))
