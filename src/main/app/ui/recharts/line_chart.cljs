(ns app.ui.recharts.line-chart
  (:require
    [app.ui.recharts.components :as rc]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3]]))

(defsc LineChart [this props]
  (rc/line-chart {:width  400
                  :height 200
                  :data   (:data props)
                  :margin {:top    20
                           :right  30
                           :left   20
                           :bottom -10}}
                 (rc/cartesian-grid {:strokeDasharray "10 10"})
                 (rc/x-axis {:dataKey "time"
                             :tickFormatter (fn [timeStr] (js/console.log timeStr))
                             :height  60
                             :label   (:x-axis-label props)})
                 (rc/y-axis {:unit (:unit-symbol props)
                             :domain ["auto" "auto"]}
                            (rc/label {:angle    -90
                                       :value    (:y-axis-label props)
                                       :position "insideLeft"
                                       :style    {:textAnchor "middle"}}))
                 (rc/tooltip {})
                 ;(rc/legend {})
                 (rc/line {:type    "monotone"
                           :dataKey (:data-key props)
                           :stroke  (:color props)})))

(def ui-line-chart (comp/factory LineChart))
