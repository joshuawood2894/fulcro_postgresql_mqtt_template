(ns app.ui.dashboard
  (:require
    ["react-grid-layout" :refer [Responsive]]
    [app.ui.recharts.line-chart :as lc]
    [app.ui.recharts.area-chart :as ac]
    [app.ui.antd.date-picker :as dp]
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(def responsive-grid-layout (interop/react-factory Responsive))

(def data-temp [{:id 1 :time 1 :temperature 20}
                {:id 2 :time 2 :temperature 21}
                {:id 3 :time 3 :temperature 22}
                {:id 4 :time 4 :temperature 20}
                {:id 5 :time 5 :temperature 19}
                {:id 6 :time 6 :temperature 20}
                {:id 7 :time 7 :temperature 23}])

(def data-hum [{:id 1 :time "1" :humidity 40}
               {:id 2 :time "2" :humidity 44}
               {:id 3 :time "3" :humidity 39}
               {:id 4 :time "4" :humidity 40}
               {:id 5 :time "5" :humidity 35}
               {:id 6 :time "6" :humidity 40}
               {:id 7 :time "7" :humidity 42}])

(def layout [{:i "a" :x 0 :y 0 :w 1 :h 2 :static true}
             {:i "b" :x 1 :y 0 :w 3 :h 2 :minW 2 :maxW 4}
             {:i "c" :x 4 :y 0 :w 1 :h 2}])

(defsc Dashboard [this props]
  {:query         []
   :initial-state {}
   :ident         (fn [] [:component/id :dashboard])
   :route-segment ["dashboard"]}
  ;(div
  ;  (ant/row {}
  ;    (ant/col {}
  ;      (ant/card {:style     {:width     400
  ;                             :textAlign "center"}
  ;                 :bodyStyle {:margin  "0px"
  ;                             :padding "0px"}
  ;                 :headStyle {:backgroundColor "#001529"
  ;                             :color           "white"}
  ;                 :title     "Percent Humidity"
  ;                 :cover     (ac/ui-area-chart {:data         data-hum
  ;                                               :x-axis-label "Time"
  ;                                               :y-axis-label "Percentage"
  ;                                               :unit-symbol  "%"
  ;                                               :color        ant/blue-primary})
  ;
  ;                 :actions   [(dp/ui-range-picker {:key "setting"})]}))
  ;    (ant/col {} (ant/card {:style     {:width     400
  ;                                       :textAlign "center"}
  ;                           :bodyStyle {:margin  "0px"
  ;                                       :padding "0px"}
  ;                           :headStyle {:backgroundColor "#001529"
  ;                                       :color           "white"}
  ;                           :title     "Temperature in Degrees Celsius"
  ;                           :cover     (lc/ui-line-chart {:data         data-temp
  ;                                                         :x-axis-label "Time"
  ;                                                         :y-axis-label "Degrees Celsius"
  ;                                                         :unit-symbol  (char 176)
  ;                                                         :color        ant/blue-primary})
  ;
  ;                           :actions   [(dp/ui-range-picker {:key "setting"})]}))

        (responsive-grid-layout {:className   "layout"
                                 :layouts     layout
                                 :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
                                 :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}}
          (div {:key 1
                :style {:backgroundColor "blue"}} 1)
          (div {:key 2
                :style {:backgroundColor "red"}} 2)
          (div {:key 3
                :style {:backgroundColor "white"}} 3))


      )
;))