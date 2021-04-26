(ns app.ui.data-logger.water-temperature
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc WaterTemperatureReading [this {:water-temperature-reading/keys [id time water-temperature]
                                   :as props}]
  {:query         [:water-temperature-reading/id :water-temperature-reading/time :water-temperature-reading/water-temperature]
   :ident         :water-temperature-reading/id
   :initial-state {:water-temperature-reading/id       :param/id
                   :water-temperature-reading/time     :param/time
                   :water-temperature-reading/water-temperature :param/water-temperature}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " water-temperature: " water-temperature))

(def ui-water-temperature-reading (comp/factory WaterTemperatureReading {:keyfn :water-temperature-reading/id}))
