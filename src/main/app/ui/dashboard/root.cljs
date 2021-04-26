(ns app.ui.dashboard.root
  (:require
    ["react-grid-layout" :refer [Responsive]]
    [app.ui.dashboard.air-h2s :as uah]
    [app.ui.dashboard.humidity :as uh]
    [app.ui.dashboard.pressure :as up]
    [app.ui.dashboard.air-temperature :as uat]
    [app.ui.dashboard.ph :as uph]
    [app.ui.dashboard.conductivity :as uc]
    [app.ui.dashboard.water-temperature :as uwt]
    [app.ui.dashboard.depth :as ud]
    [app.ui.dashboard.gps :as ug]
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(defsc Charts [this {:charts/keys [humidity air-temperature pressure
                                   air-h2s ph conductivity
                                   water-temperature depth] :as props}]
  {:query         [{:charts/air-h2s (comp/get-query uah/AirH2SData)}
                   {:charts/humidity (comp/get-query uh/HumidityData)}
                   {:charts/pressure (comp/get-query up/PressureData)}
                   {:charts/air-temperature (comp/get-query
                                              uat/AirTemperatureData)}
                   {:charts/ph (comp/get-query uph/PHData)}
                   {:charts/conductivity (comp/get-query uc/ConductivityData)}
                   {:charts/water-temperature (comp/get-query
                                                uwt/WaterTemperatureData)}
                   {:charts/depth (comp/get-query ud/DepthData)}]
   :ident         (fn [_ _] [:component/id ::charts])
   :initial-state {:charts/air-h2s           {}
                   :charts/humidity          {}
                   :charts/pressure          {}
                   :charts/air-temperature   {}
                   :charts/ph                {}
                   :charts/conductivity      {}
                   :charts/water-temperature {}
                   :charts/depth             {}}}
  (div
    (ant/row {}
      (ant/col {} (uh/ui-humidity-data humidity))
      (ant/col {} (uat/ui-air-temperature-data air-temperature))
      (ant/col {} (up/ui-pressure-data pressure))
      (ant/col {} (uah/ui-air-h2s-data air-h2s))
      (ant/col {} (uph/ui-ph-data ph))
      (ant/col {} (uc/ui-conductivity-data conductivity))
      (ant/col {} (uwt/ui-water-temperature-data water-temperature))
      (ant/col {} (ud/ui-depth-data depth)))))

(def ui-charts (comp/factory Charts))

(defsc Dashboard [this {:dashboard/keys [charts gps] :as props}]
  {:query         [{:dashboard/charts (comp/get-query Charts)}
                   {:dashboard/gps (comp/get-query ug/GPSData)}]
   :initial-state {:dashboard/charts {}
                   :dashboard/gps {}}
   :ident         (fn [] [:component/id :dashboard])
   :route-segment ["dashboard"]}
  (div
    (ant/row {}
      (ant/col {}
        (ui-charts charts)
        (ug/ui-gps gps)))))

;(comment
;  ;(def layout [{:i "a" :x 0 :y 0 :w 1 :h 2 :static true}
;  ;             {:i "b" :x 1 :y 0 :w 3 :h 2 :minW 2 :maxW 4}
;  ;             {:i "c" :x 4 :y 0 :w 1 :h 2}])
;
;  ;(responsive-grid-layout {:className   "layout"
;  ;                         :layouts     layout
;  ;                         :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
;  ;                         :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}}
;  ;  (div {:key 1
;  ;        :style {:backgroundColor "blue"}} 1)
;  ;  (div {:key 2
;  ;        :style {:backgroundColor "red"}} 2)
;  ;  (div {:key 3
;  ;        :style {:backgroundColor "white"}} 3))
;  )