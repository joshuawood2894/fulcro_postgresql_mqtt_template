(ns app.ui.data-logger.conductivity
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc ConductivityReading [this {:conductivity-reading/keys [id time conductivity] :as 
                                                   props}]
  {:query         [:conductivity-reading/id :conductivity-reading/time :conductivity-reading/conductivity]
   :ident         :conductivity-reading/id
   :initial-state {:conductivity-reading/id       :param/id
                   :conductivity-reading/time     :param/time
                   :conductivity-reading/conductivity :param/conductivity}}
  (ant/list-item-ant {} "id: " id " time: " (str time) " conductivity: " conductivity))

(def ui-conductivity-reading (comp/factory ConductivityReading {:keyfn :conductivity-reading/id}))
