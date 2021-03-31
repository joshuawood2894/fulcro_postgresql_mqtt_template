(ns app.ui.dashboard
  (:require
    ["react-grid-layout" :refer [Responsive]]
    [app.ui.recharts.line-chart :as lc]
    [app.ui.recharts.area-chart :as ac]
    [app.ui.antd.date-picker :as dp]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger :as dl]
    [app.ui.leaflet.leaflet :as lf]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]))

(defsc TemperatureChart [this props]
  (ant/card {:style     {:width     400
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     "Temperature in Degrees Celsius"
             :cover     (lc/ui-line-chart {:data         props
                                           :x-axis-label "Time"
                                           :y-axis-label "Degrees Celsius"
                                           :unit-symbol  (char 176)
                                           :data-key     "temperature"
                                           :color        ant/blue-primary})

             :actions   [(dp/ui-range-picker {:key "setting"})]}))

(def ui-temperature-chart (comp/factory TemperatureChart))

(defsc TemperatureData [this {:temperature-data/keys [id temperature] :as props}]
  {:query         [:temperature-data/id {:temperature-data/temperature (comp/get-query dl/TemperatureReading)}]
   :ident          :temperature-data/id
   :initial-state {:temperature-data/id 1
                   :temperature-data/temperature [{:id 1 :time 1 :temperature 20}
                                                  {:id 2 :time 2 :temperature 21}
                                                  {:id 3 :time 3 :temperature 22}
                                                  {:id 4 :time 4 :temperature 20}
                                                  {:id 5 :time 5 :temperature 19}
                                                  {:id 6 :time 6 :temperature 20}
                                                  {:id 7 :time 7 :temperature 23}]}}
  (div
    (ui-temperature-chart temperature)))

(def ui-temperature-data (comp/factory TemperatureData))

(defsc HumidityChart [this props]
  (ant/card {:style     {:width     400
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     "Percent Humidity"
             :cover     (ac/ui-area-chart {:data         props
                                           :x-axis-label "Time"
                                           :y-axis-label "Percentage"
                                           :unit-symbol  "%"
                                           :data-key     "humidity"
                                           :color        ant/blue-primary})

             :actions   [(dp/ui-range-picker {:key "setting"})]}))

(def ui-humidity-chart (comp/factory HumidityChart))

(defsc HumidityData [this {:humidity-data/keys [id humidity] :as props}]
  {:query         [:humidity-data/id {:humidity-data/humidity (comp/get-query dl/HumidityReading)}]
   :ident          :humidity-data/id
   :initial-state  {:humidity-data/id 1
                    :humidity-data/humidity [{:id 1 :time "1" :humidity 40}
                                             {:id 2 :time "2" :humidity 44}
                                             {:id 3 :time "3" :humidity 39}
                                             {:id 4 :time "4" :humidity 40}
                                             {:id 5 :time "5" :humidity 35}
                                             {:id 6 :time "6" :humidity 40}
                                             {:id 7 :time "7" :humidity 42}]}}
  (div
    (ui-humidity-chart humidity)))

(def ui-humidity-data (comp/factory HumidityData))

(defsc PressureChart [this props]
  (ant/card {:style     {:width     400
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     "Pressure in Hectopascals"
             :cover     (ac/ui-area-chart {:data         props
                                           :x-axis-label "Time"
                                           :y-axis-label "Pressure (hPa)"
                                           :unit-symbol  ""
                                           :data-key      "pressure"
                                           :color        ant/blue-primary})

             :actions   [(dp/ui-range-picker {:key "setting"})]}))

(def ui-pressure-chart (comp/factory PressureChart))

(defsc PressureData [this {:pressure-data/keys [id pressure] :as props}]
  {:query         [:pressure-data/id {:pressure-data/pressure (comp/get-query dl/PressureReading)}]
   :ident          :pressure-data/id
   :initial-state  {:pressure-data/id 1
                    :pressure-data/pressure [{:id 1 :time "1" :pressure 999}
                                             {:id 2 :time "2" :pressure 1000}
                                             {:id 3 :time "3" :pressure 998}
                                             {:id 4 :time "4" :pressure 1001}
                                             {:id 5 :time "5" :pressure 1001}
                                             {:id 6 :time "6" :pressure 998}
                                             {:id 7 :time "7" :pressure 999}]}}
  (div
    (ui-pressure-chart pressure)))

(def ui-pressure-data (comp/factory PressureData))

(defsc Charts [this {:charts/keys [humidity temperature pressure] :as props}]
  {:query         [{:charts/humidity (comp/get-query HumidityData)}
                   {:charts/temperature (comp/get-query TemperatureData)}
                   {:charts/pressure (comp/get-query PressureData)}]
   :ident         (fn [_ _] [:component/id ::charts])
   :initial-state {:charts/humidity     {}
                   :charts/temperature  {}
                   :charts/pressure     {}}}
  (div
    (lf/ui-gps-map)
    (ant/row {}
     (ant/col {} (ui-humidity-data humidity))
     (ant/col {} (ui-temperature-data temperature))
     (ant/col {} (ui-pressure-data pressure)))))

(def ui-charts (comp/factory Charts))

(defsc Dashboard [this {:dashboard/keys [charts] :as props}]
  {:query         [{:dashboard/charts (comp/get-query Charts)}]
   :initial-state {:dashboard/charts {}}
   :ident         (fn [] [:component/id :dashboard])
   :route-segment ["dashboard"]}
  (div
    (ant/row {}
      (ant/col {}
        (ui-charts charts)))))




(comment
  ;(def layout [{:i "a" :x 0 :y 0 :w 1 :h 2 :static true}
  ;             {:i "b" :x 1 :y 0 :w 3 :h 2 :minW 2 :maxW 4}
  ;             {:i "c" :x 4 :y 0 :w 1 :h 2}])

  ;(responsive-grid-layout {:className   "layout"
  ;                         :layouts     layout
  ;                         :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
  ;                         :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}}
  ;  (div {:key 1
  ;        :style {:backgroundColor "blue"}} 1)
  ;  (div {:key 2
  ;        :style {:backgroundColor "red"}} 2)
  ;  (div {:key 3
  ;        :style {:backgroundColor "white"}} 3))
  )