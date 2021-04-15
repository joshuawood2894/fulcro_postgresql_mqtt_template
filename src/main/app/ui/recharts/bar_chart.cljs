(ns app.ui.recharts.bar-chart
  (:require
    [app.ui.recharts.components :as rc]
    [app.ui.recharts.util :as u]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]))

(defsc BarChart [this props]
  (rc/bar-chart {:width  485
                     :height 275
                     :data   (:data props)
                     :margin {:top    20
                              :right  35
                              :left   30
                              :bottom -15}}
    (rc/cartesian-grid {:strokeDasharray "5 5"})
    (rc/x-axis {:dataKey       "time"
                :minTickGap    10
                :tickFormatter (fn [timeStr] (u/format-date
                                               timeStr))
                :height        60
                ;:label   (:x-axis-label props)
                :tickMargin    10})
    (rc/y-axis {:unit   (:unit-symbol props)
                :domain [(:min-bound props) (:max-bound props)]}
      (rc/label {:angle    -90
                 :value    (:y-axis-label props)
                 :position "insideLeft"
                 :style    {:textAnchor "middle"}
                 :offset   -10}))
    (rc/tooltip {:animationEasing    "ease-in"
                 :offset             20
                 :allowEscapeViewBox {:x false :y false}})
    ;(rc/legend {})
    (rc/bar {:dataKey (:data-key props)
                 :fill    (:color props)})))

(def ui-bar-chart (comp/factory BarChart))
