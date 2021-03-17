(ns app.ui.dashboard
  (:require
    ["recharts" :refer [LineChart Line XAxis YAxis CartesianGrid Tooltip Legend ResponsiveContainer LinearGradient AreaChart Area Label]]

    [app.ui.antd.components :as ant]
    [app.ui.antd.date-picker :as dp]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(def line-chart (interop/react-factory LineChart))
(def line (interop/react-factory Line))
(def x-axis (interop/react-factory XAxis))
(def y-axis (interop/react-factory YAxis))
(def cartesian-grid (interop/react-factory CartesianGrid))
(def tooltip (interop/react-factory Tooltip))
(def legend (interop/react-factory Legend))
(def responsive-container (interop/react-factory ResponsiveContainer))
(def linear-gradient (interop/react-factory LinearGradient))
(def area-chart (interop/react-factory AreaChart))
(def area (interop/react-factory Area))
(def label (interop/react-factory Label))


(def data-temp [{:id 1 :time 1 :temperature 20}
                {:id 2 :time 2 :temperature 21}
                {:id 3 :time 3 :temperature 22}
                {:id 4 :time 4 :temperature 20}
                {:id 5 :time 5 :temperature 19}
                {:id 6 :time 6 :temperature 20}
                {:id 7 :time 7 :temperature 23}])

(def data-hum[{:id 1 :time "1" :humidity 40}
              {:id 2 :time "2" :humidity 44}
              {:id 3 :time "3" :humidity 39}
              {:id 4 :time "4" :humidity 40}
              {:id 5 :time "5" :humidity 35}
              {:id 6 :time "6" :humidity 40}
              {:id 7 :time "7" :humidity 42}])

(defsc TemperatureChart [this props]
  (ant/card {:style   {:width 400
                       :textAlign "center"}
             :bodyStyle {:margin "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color "white"}
             :title "Temperature in Degrees Celsius"
             :cover (line-chart {:width  400
                                 :height 200
                                 :data   data-temp
                                 :margin {:top    20
                                          :right  30
                                          :left   20
                                          :bottom -10}}
                                  (cartesian-grid {:strokeDasharray "10 10"})
                                  (x-axis {:dataKey "time"
                                           :height  60
                                           :label   "Time"})
                                  (y-axis {:unit  (char 176)}
                                          (label {:angle -90
                                                  :value "Degrees Celsius"
                                                  :position "insideLeft"
                                                  :style {:textAnchor "middle"}}))
                                  (tooltip {})
                                  ;(legend {})
                                  (line {:type    "monotone"
                                         :dataKey "temperature"
                                         :stroke  ant/blue-primary}))

             :actions [(dp/ui-range-picker {:key "setting"})]}))

(def ui-temperature-chart (comp/factory TemperatureChart))

(defsc HumidityChart [this props]
  (ant/card {:style   {:width 400
                       :textAlign "center"}
             :bodyStyle {:margin "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color "white"}
             :title "Percent Humidity"
             :cover (area-chart {:width  400
                                 :height 200
                                 :data   data-hum
                                 :margin {:top    20
                                          :right  30
                                          :left   20
                                          :bottom -10}}
                                (x-axis {:dataKey "time"
                                         :stroke "black"
                                         :label "Time"
                                         :height  60})
                                (y-axis {:unit "%"
                                         :stroke "black"}
                                        (label {:angle -90
                                                :value "Percentage"
                                                :position "insideLeft"
                                                :style {:textAnchor "middle"}}))
                                (cartesian-grid {:strokeDasharray "3 3"})
                                (tooltip {})
                                (area {:type        "monotone"
                                       :dataKey     "humidity"
                                       :stroke      ant/blue-primary
                                       :fill        ant/blue-primary
                                       :fillOpacity "0.5"}))

             :actions [(dp/ui-range-picker {:key "setting"})]}))

(def ui-humidity-chart (comp/factory HumidityChart))