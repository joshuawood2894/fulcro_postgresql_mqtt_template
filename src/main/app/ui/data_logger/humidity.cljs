(ns app.ui.data-logger.humidity
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc HumidityReading [this {:humidity-reading/keys [id time humidity] :as props}]
  {:query         [:humidity-reading/id :humidity-reading/time :humidity-reading/humidity]
   :ident         :humidity-reading/id
   :initial-state {:humidity-reading/id       :param/id
                   :humidity-reading/time     :param/time
                   :humidity-reading/humidity :param/humidity}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " humidity: " humidity))

(def ui-humidity-reading (comp/factory HumidityReading {:keyfn :humidity-reading/id}))
