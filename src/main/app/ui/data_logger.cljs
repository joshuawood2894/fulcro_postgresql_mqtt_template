(ns app.ui.data-logger
  (:require
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]
    ["react-infinite-scroller" :refer [InfiniteScroll]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [app.application :refer [SPA]]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]))

(def infinite-scroller (interop/react-factory InfiniteScroll))

(defsc HumidityReading [this {:humidity-reading/keys [id time humidity] :as props}]
  {:query         [:humidity-reading/id :humidity-reading/time :humidity-reading/humidity]
   :ident          :humidity-reading/id
   :initial-state {:humidity-reading/id       :param/id
                   :humidity-reading/time     :param/time
                   :humidity-reading/humidity :param/humidity}}
  (ant/list-item-ant {} "id: " id " time: " time " humidity: " humidity))

(def ui-humidity-reading (comp/factory HumidityReading {:keyfn :humidity-reading/id}))

(defsc TemperatureReading [this {:temperature-reading/keys [id time temperature] :as props}]
  {:query         [:temperature-reading/id :temperature-reading/time :temperature-reading/temperature]
   :ident          :temperature-reading/id
   :initial-state {:temperature-reading/id          :param/id
                   :temperature-reading/time        :param/time
                   :temperature-reading/temperature :param/temperature}}
  (ant/list-item-ant {} "id: " id " time: " time " temperature: " temperature))

(def ui-temperature-reading (comp/factory TemperatureReading {:keyfn :temperature-reading/id}))

(defsc PressureReading [this {:pressure-reading/keys [id time pressure] :as props}]
  {:query         [:pressure-reading/id :pressure-reading/time :pressure-reading/pressure]
   :ident          :pressure-reading/id
   :initial-state {:pressure-reading/id          :param/id
                   :pressure-reading/time        :param/time
                   :pressure-reading/pressure :param/pressure}}
  (ant/list-item-ant {} "id: " id " time: " time " pressure: " pressure))

(def ui-pressure-reading (comp/factory PressureReading {:keyfn :pressure-reading/id}))

(defsc DataLogger [this {:root-data-logger/keys [humidity pressure temperature] :as props}]
  {:query [{:root-data-logger/humidity (comp/get-query (comp/registry-key->class :app.ui.dashboard/HumidityData))}
           {:root-data-logger/temperature (comp/get-query (comp/registry-key->class :app.ui.dashboard/TemperatureData))}
           {:root-data-logger/pressure (comp/get-query (comp/registry-key->class :app.ui.dashboard/PressureData))}]
   :initial-state {:root-data-logger/humidity    {}
                   :root-data-logger/pressure    {}
                   :root-data-logger/temperature {}}
   :ident (fn [] [:component/id :root-data-logger])
   :route-segment ["data-logger"]}
  (div
    (ant/list-ant {:bordered true
                   :style {:border "1px solid #e8e8e8"
                           :borderRadius "4px"
                           :backgroundColor "#ffffff"}}
      (map ui-pressure-reading (:pressure-data/pressure pressure))
      (map ui-temperature-reading (:temperature-data/temperature temperature))
      (map ui-humidity-reading (:humidity-data/humidity humidity)))))

(comment
  (merge/merge-component! SPA HumidityReading {:humidity-reading/id 41 :humidity-reading/time "20" :humidity-reading/humidity 2}
    :append [:humidity-data/id 1 :humidity-data/humidity])
  )
