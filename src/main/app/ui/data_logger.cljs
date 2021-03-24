(ns app.ui.data-logger
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc DataLogger [this props]
  {:query         []
   :initial-state {}
   :ident         (fn [] [:component/id :data-logger])
   :route-segment ["data-logger"]}
  (div
    (h3 "Data Logger")))
