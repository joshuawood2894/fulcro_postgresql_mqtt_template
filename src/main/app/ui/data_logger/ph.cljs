(ns app.ui.data-logger.ph
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc PHReading [this {:ph-reading/keys [id time ph] :as props}]
  {:query         [:ph-reading/id :ph-reading/time :ph-reading/ph]
   :ident         :ph-reading/id
   :initial-state {:ph-reading/id       :param/id
                   :ph-reading/time     :param/time
                   :ph-reading/ph :param/ph}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " ph: " ph))

(def ui-ph-reading (comp/factory PHReading {:keyfn :ph-reading/id}))
