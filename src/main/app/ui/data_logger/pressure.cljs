(ns app.ui.data-logger.pressure
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc PressureReading [this {:pressure-reading/keys [id time pressure] :as
                                                   props}]
  {:query         [:pressure-reading/id :pressure-reading/time :pressure-reading/pressure]
   :ident         :pressure-reading/id
   :initial-state {:pressure-reading/id       :param/id
                   :pressure-reading/time     :param/time
                   :pressure-reading/pressure :param/pressure}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " pressure: " pressure))

(def ui-pressure-reading (comp/factory PressureReading {:keyfn
                                                   :pressure-reading/id}))
