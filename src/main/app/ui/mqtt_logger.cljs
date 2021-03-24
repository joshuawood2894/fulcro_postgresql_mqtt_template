(ns app.ui.mqtt-logger
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc MqttLogger [this props]
  {:query         []
   :initial-state {}
   :ident         (fn [] [:component/id :mqtt-logger])
   :route-segment ["mqtt-logger"]}
  (div
    (h3 "MQTT Logger")))