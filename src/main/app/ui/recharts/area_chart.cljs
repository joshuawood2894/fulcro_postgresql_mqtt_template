(ns app.ui.recharts.area-chart
  (:require
    [app.ui.recharts.components :as rc]
    [app.ui.recharts.util :as u]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 defs stop
                                                linearGradient]]))

(defsc AreaChart [this props]
  (rc/area-chart {:width  485
                  :height 275
                  :data   (:data props)
                  :margin {:top    20
                           :right  35
                           :left   30
                           :bottom -15}}
    (defs
      (linearGradient {:id (:id props) :x1 "0" :y1 "0" :x2 "0" :y2 "1"}
        (stop {:offset "40%" :stopColor (:color props) :stopOpacity 0.8})
        (stop {:offset "100%" :stopColor (:color props) :stopOpacity 0.1})))
    (rc/cartesian-grid {:strokeDasharray "3 3"})
    (rc/x-axis {:dataKey       "time"
                :minTickGap 20
                ;:label   (:x-axis-label props)
                :tickFormatter (fn [timeStr] (u/format-date
                                               timeStr))
                :height        60
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
    (rc/area {:type        "monotone"
              :dataKey     (:data-key props)
              :stroke      (u/darken-color (str (:color props)) 0.7)
              :strokeWidth 1.5
              :fill        (str "url(#" (:id props) ")")
              :fillOpacity 1})))

(def ui-area-chart (comp/factory AreaChart))