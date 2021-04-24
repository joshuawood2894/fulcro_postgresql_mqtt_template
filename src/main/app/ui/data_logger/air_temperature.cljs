(ns app.ui.data-logger.air-temperature
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc AirTemperatureReading [this {:air-temperature-reading/keys [id time air-temperature] 
                                   :as props}]
  {:query         [:air-temperature-reading/id :air-temperature-reading/time :air-temperature-reading/air-temperature]
   :ident         :air-temperature-reading/id
   :initial-state {:air-temperature-reading/id       :param/id
                   :air-temperature-reading/time     :param/time
                   :air-temperature-reading/air-temperature :param/air-temperature}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " air-temperature: " air-temperature))

(def ui-air-temperature-reading (comp/factory AirTemperatureReading {:keyfn :air-temperature-reading/id}))
