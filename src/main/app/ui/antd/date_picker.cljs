(ns app.ui.antd.date-picker
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(defsc RangePicker [_ _]
  (ant/space {:direction "vertical" :size 12}
         (ant/range-picker {:showTime true
                            :format "MM/DD/YYYY, h:mm:ss A"
                            :size "default"
                            :bordered false})))

(def ui-range-picker (comp/factory RangePicker))