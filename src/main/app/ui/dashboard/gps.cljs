(ns app.ui.dashboard.gps
  (:require
    [app.ui.dashboard.config :as dc]
   [app.ui.antd.components :as ant]
   [app.ui.data-logger.gps :as dl]
   [app.model.dashboard.gps :as gps]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defsc GPSData [this {:gps-data/keys [gps] :as
                                     props}]
  {:query         [:gps-data/id {:gps-data/gps (comp/get-query dl/GPSReading)}]
   :ident         (fn [] [:component/id :gps-data])
   :initial-state {:gps-data/gps [{:id 1 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:24:00") :latitude 32
                                   :longitude -95}
                                  {:id 2 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:25:00") :latitude 32.1
                                   :longitude -95.1}
                                  {:id 3 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:26:00") :latitude 32
                                   :longitude -95}
                                  {:id 4 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:27:00") :latitude 32.1
                                   :longitude -95.1}
                                  {:id 5 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:28:00") :latitude 32
                                   :longitude -95}
                                  {:id 6 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:29:00") :latitude 32.1
                                   :longitude -95.1}
                                  {:id 7 :time (js/Date.
                                                 "March
                                                 17, 2021
                                                 15:30:00") :latitude 32
                                   :longitude -95}]}}
  (div (str props)))

(def ui-gps (comp/factory GPSData))