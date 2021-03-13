(ns app.ui.antd.date-picker
  (:require
    ["antd" :refer [Space DatePicker DatePicker.RangePicker]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(def space (interop/react-factory Space))
(def date-picker (interop/react-factory DatePicker))
(def range-picker (interop/react-factory DatePicker.RangePicker))

(defsc RangePicker [_ _]
  (space {:direction "vertical" :size 12}
         (range-picker {:showTime true
                        :format "MM/DD/YYYY, h:mm:ss A"
                        :size "default"
                        :bordered true})))

(def ui-range-picker (comp/factory RangePicker))