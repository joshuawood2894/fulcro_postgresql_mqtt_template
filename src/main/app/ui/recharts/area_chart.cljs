(ns app.ui.recharts.area-chart
  (:require
    [app.ui.recharts.components :as rc]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]))

(defsc AreaChart [this props]
    (rc/area-chart {:width  400
                   :height 200
                   :data   (:data props)
                   :margin {:top    20
                            :right  30
                            :left   20
                            :bottom -10}}
     (rc/x-axis {:dataKey "time"
                 :stroke  "black"
                 :label   (:x-axis-label props)
                 :height  60})
     (rc/y-axis {:unit   (:unit-symbol props)
                 :stroke "black"
                 :domain [min max]}
       (rc/label {:angle    -90
                  :value    (:y-axis-label props)
                  :position "insideLeft"
                  :style    {:textAnchor "middle"}}))
     (rc/cartesian-grid {:strokeDasharray "10 10"})
     (rc/tooltip {})
     (rc/area {:type        "monotone"
               :dataKey     (:data-key props)
               :stroke      (:color props)
               :fill        (:color props)
               :fillOpacity "0.5"})))

(def ui-area-chart (comp/factory AreaChart))