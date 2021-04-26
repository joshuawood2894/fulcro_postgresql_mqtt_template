(ns app.ui.data-logger.air-h2s
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc AirH2SReading [this {:air-h2s-reading/keys [id time air-h2s] :as props}]
  {:query         [:air-h2s-reading/id :air-h2s-reading/time :air-h2s-reading/air-h2s]
   :ident         :air-h2s-reading/id
   :initial-state {:air-h2s-reading/id       :param/id
                   :air-h2s-reading/time     :param/time
                   :air-h2s-reading/air-h2s :param/air-h2s}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " air-h2s: " air-h2s))

(def ui-air-h2s-reading (comp/factory AirH2SReading {:keyfn :air-h2s-reading/id}))
