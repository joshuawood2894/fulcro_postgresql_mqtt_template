(ns app.ui.data-logger.root
  (:require
    ["react-infinite-scroller" :refer [InfiniteScroll]]
    [app.ui.data-logger.air-h2s :as dlah]
    [app.ui.data-logger.pressure :as dlp]
    [app.ui.data-logger.humidity :as dlh]
    [app.ui.data-logger.air-temperature :as dlat]
    [app.ui.data-logger.ph :as dlph]
    [app.ui.data-logger.conductivity :as dlc]
    [app.ui.data-logger.water-temperature :as dlwt]
    [app.ui.data-logger.depth :as dld]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]))

(defsc DataLogger [this {:root-data-logger/keys [air-h2s pressure humidity
                                                 air-temperature ph
                                                 conductivity water-temperature
                                                 depth] :as props}]
  {:query         [{:root-data-logger/air-h2s
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.air-h2s/AirH2SData))}
                   {:root-data-logger/pressure
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.pressure/PressureData))}
                   {:root-data-logger/humidity
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.humidity/HumidityData))}
                   {:root-data-logger/air-temperature
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.air-temperature/AirTemperatureData))}
                   {:root-data-logger/ph
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.ph/PHData))}
                   {:root-data-logger/conductivity
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.conductivity/ConductivityData))}
                   {:root-data-logger/water-temperature
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.water-temperature/WaterTemperatureData))}
                   {:root-data-logger/depth
                    (comp/get-query
                      (comp/registry-key->class
                        :app.ui.dashboard.depth/DepthData))}]
   :initial-state {:root-data-logger/air-h2s           {}
                   :root-data-logger/pressure          {}
                   :root-data-logger/humidity          {}
                   :root-data-logger/air-temperature   {}
                   :root-data-logger/ph                {}
                   :root-data-logger/conductivity      {}
                   :root-data-logger/water-temperature {}
                   :root-data-logger/depth             {}}
   :ident         (fn [] [:component/id :root-data-logger])
   :route-segment ["data-logger"]}
  (div
    (ant/list-ant {:bordered true
                   :style    {:border          "1px solid #e8e8e8"
                              :borderRadius    "4px"
                              :backgroundColor "#ffffff"}}
      (map dlah/ui-air-h2s-reading (:air-h2s-data/air-h2s air-h2s))
      (map dlp/ui-pressure-reading (:pressure-data/pressure pressure))
      (map dlh/ui-humidity-reading (:humidity-data/humidity humidity))
      (map dlat/ui-air-temperature-reading (:air-temperature-data/air-temperature air-temperature))
      (map dlph/ui-ph-reading (:ph-data/ph ph))
      (map dlc/ui-conductivity-reading (:conductivity-data/conductivity conductivity))
      (map dlwt/ui-water-temperature-reading
        (:water-temperature-data/water-temperature water-temperature))
      (map dld/ui-depth-reading (:depth-data/depth depth)))))
