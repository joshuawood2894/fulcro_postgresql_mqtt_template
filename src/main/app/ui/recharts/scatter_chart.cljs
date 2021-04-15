(ns app.ui.recharts.scatter-chart
  (:require
    [app.ui.recharts.components :as rc]
    [app.ui.recharts.util :as u]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]))

(defsc ScatterChart [this props]
  (rc/scatter-chart {:width  485
                     :height 275
                     ;:data   (:data props)
                     :margin {:top    20
                              :right  35
                              :left   30
                              :bottom -15}}
    (rc/cartesian-grid {:strokeDasharray "5 5"})
    (rc/x-axis {:dataKey       "time"
                :name "test1"
                :minTickGap    10
                :tickFormatter (fn [timeStr] (u/format-date
                                               timeStr))
                :height        60
                :tickMargin    10})
    (rc/y-axis {:dataKey (:data-key props)
                :unit   (:unit-symbol props)
                :name "test2"
                :domain [(:min-bound props) (:max-bound props)]}
      (rc/label {:angle    -90
                 :value    (:y-axis-label props)
                 :position "insideLeft"
                 :style    {:textAnchor "middle"}
                 :offset   -10}))
    (rc/tooltip {:cursor {:strokeDasharray "3 3"}
                 :animationEasing    "ease-in"
                 :offset             20
                 :allowEscapeViewBox {:x false :y false}
                 })
    ;(rc/legend {})
    (rc/scatter {:name "test3"
                 :data (:data props)
                 ;:dataKey (:data-key props)
                 :fill    (:color props)})))

(def ui-scatter-chart (comp/factory ScatterChart))
