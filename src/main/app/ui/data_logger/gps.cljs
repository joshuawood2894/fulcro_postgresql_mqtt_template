(ns app.ui.data-logger.gps
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc GPSReading [this {:gps-reading/keys [id time latitude longitude] :as
                                           props}]
  {:query         [:gps-reading/id :gps-reading/time :gps-reading/latitude
                   :gps-reading/longitude]
   :ident         :gps-reading/id
   :initial-state {:gps-reading/id        :param/id
                   :gps-reading/time      :param/time
                   :gps-reading/latitude  :param/latitude
                   :gps-reading/longitude :param/longitude}}
  (div (str props)))

(def ui-gps-reading (comp/factory GPSReading {:keyfn
                                              :gps-reading/id}))
