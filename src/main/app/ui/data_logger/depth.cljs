(ns app.ui.data-logger.depth
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc DepthReading [this {:depth-reading/keys [id time depth] :as props}]
  {:query         [:depth-reading/id :depth-reading/time :depth-reading/depth]
   :ident         :depth-reading/id
   :initial-state {:depth-reading/id       :param/id
                   :depth-reading/time     :param/time
                   :depth-reading/depth :param/depth}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " depth: " depth))

(def ui-depth-reading (comp/factory DepthReading {:keyfn 
                                                   :depth-reading/id}))
