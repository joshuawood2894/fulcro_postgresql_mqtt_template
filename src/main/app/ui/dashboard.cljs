(ns app.ui.dashboard
  (:require
    ["react-grid-layout" :refer [Responsive]]
    ["react-color" :refer [TwitterPicker]]
    [app.application :refer [SPA]]
    [app.model.dashboard :as mdb]
    [app.ui.recharts.line-chart :as lc]
    [app.ui.recharts.area-chart :as ac]
    [app.ui.recharts.scatter-chart :as sc]
    [app.ui.recharts.bar-chart :as bc]
    [app.ui.antd.components :as ant]
    [app.ui.data-logger :as dl]
    [app.ui.leaflet.leaflet :as lf]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(def twitter-picker (interop/react-factory TwitterPicker))

(defn create-rechart [chart-type params]
  (case chart-type
    "line" (lc/ui-line-chart params)
    "area" (ac/ui-area-chart params)
    "bar" (bc/ui-bar-chart params)
    "scatter" (sc/ui-scatter-chart params)))

(defn create-card-title [title toggle-mut toggle-settings]
  (ant/row {}
    (ant/col {:span 6} (div title))
    (ant/col {:span 15})
    (ant/col {:span 3} (ant/switch
                         {:unCheckedChildren
                                    (ant/caret-down)
                          :checkedChildren
                                    (ant/caret-up)
                          :onChange (fn [] (comp/transact! SPA
                                             [(toggle-mut
                                                {:toggle-settings
                                                 toggle-settings})]))}))))

(defn create-dropdown-settings [toggle start-end-mut
                                color-mut min-mut max-mut
                                redo-min-max-mut chart-type
                                chart-type-mut]
  (if toggle
    [(div
       (ant/row {:style {:marginBottom "12px"}}
         (ant/col {:span 24}
           (ant/radio-group {:options     [{:label "Line" :value "line"}
                                           {:label "Area" :value "area"}
                                           {:label "Bar" :value "bar"}
                                           {:label "Scatter" :value "scatter"}]
                             :onChange    (fn [event]
                                            (comp/transact! SPA
                                              [(chart-type-mut {:chart-type (.-value (.-target event))})]))
                             :value       chart-type
                             :optionType  "button"
                             :buttonStyle "solid"
                             :size        "large"})))
       (ant/row {:style {:marginBottom "12px"}}
         (ant/col {:span 1})
         (ant/col {:span  10
                   :style {:margin "auto"}}
           (ant/input {:type        "number"
                       :size        "large"
                       :addonBefore "Min"
                       :onChange    (fn [event]
                                      (comp/transact! SPA
                                        [(min-mut
                                           {:min (. js/Number (parseFloat (.-value (.-target event))))})]))}))
         (ant/col {:span  10
                   :style {:margin "auto"}}
           (ant/input {:type        "number"
                       :size        "large"
                       :addonBefore "Max"
                       :onChange    (fn [event]
                                      (js/console.log (.-value (.-target event)))
                                      (comp/transact! SPA
                                        [(max-mut
                                           {:min (. js/Number (parseFloat (.-value (.-target event))))})]))}))
         (ant/col {:span  2
                   :style {:margin "auto"}}
           (ant/button {:icon    (ant/redo-outlined)
                        :size    "large"
                        :type    "primary"
                        :onClick (fn []
                                   (comp/transact! SPA
                                     [(redo-min-max-mut
                                        {:min "min" :max "max"})]))}))
         (ant/col {:span 1}))
       (ant/row {:style {:marginBottom "12px"}}
         (ant/col {:span 1})
         (ant/col {:span 22}
           (ant/range-picker {:showTime true
                              :format   "MM/DD/YYYY, h:mm:ss A"
                              :size     "large"
                              :bordered true
                              :style {:width "auto"}
                              :onOk     (fn [date]
                                          (when (and (not= nil (nth (js->clj date) 0))
                                                  (not= nil (nth (js->clj date) 1)))
                                            (comp/transact! SPA
                                              [(start-end-mut
                                                 {:start (js/Date. (nth (js->clj date) 0))
                                                  :end   (js/Date. (nth (js->clj date) 1))})])))}))
         (ant/col {:span 1}))
       (ant/row {}
         (ant/col {:span 1})
         (ant/col
           {:span  22
            :style {:margin "auto"}}
           (twitter-picker {:triangle         "hide"
                            :width            "auto"
                            :colors           ["#FF6900" "#FCB900"
                                               "#7BDCB5" "#00D084"
                                               "#8ED1FC" "#0693E3"
                                               "#ABB8C3" "#EB144C"]
                            :onChangeComplete (fn [color]
                                                (comp/transact! SPA
                                                  [(color-mut
                                                     {:color (.-hex color)})]))}))
         (ant/col {:span 1})))]
    nil))

(defsc TemperatureChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (create-card-title "Temperature in Degrees Celsius"
                          mdb/toggle-temperature-settings! (:toggle-settings props))
             :cover     (create-rechart (:chart-type props)
                          {:data         (:temperature props)
                           :x-axis-label "Time"
                           :y-axis-label "Degrees Celsius"
                           :unit-symbol  (char 176)
                           :data-key     "temperature"
                           :id           "temperature-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (create-dropdown-settings (:toggle-settings props)
                          mdb/set-temperature-start-end-datetime!
                          mdb/set-temperature-color!
                          mdb/set-temperature-min-bound!
                          mdb/set-temperature-max-bound!
                          mdb/redo-temperature-min-max-bound!
                          (:chart-type props)
                          mdb/set-temperature-chart-type!)}))

(def ui-temperature-chart (comp/factory TemperatureChart))

(defsc TemperatureData [this {:temperature-data/keys [id temperature
                                                      start-date end-date
                                                      toggle-settings color
                                                      min-bound max-bound
                                                      chart-type]
                              :as                    props}]
  {:query         [:temperature-data/id {:temperature-data/temperature (comp/get-query dl/TemperatureReading)}
                   :temperature-data/start-date :temperature-data/end-date
                   :temperature-data/toggle-settings :temperature-data/color
                   :temperature-data/min-bound :temperature-data/max-bound
                   :temperature-data/chart-type]
   :ident         :temperature-data/id
   :initial-state {:temperature-data/id              1
                   :temperature-data/toggle-settings false
                   :temperature-data/min-bound       js/NaN
                   :temperature-data/max-bound       js/NaN
                   :temperature-data/chart-type      "line"
                   :temperature-data/color           ant/blue-primary
                   :temperature-data/start-date      (js/Date.)
                   :temperature-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :temperature-data/temperature     [{:id 1 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:24:00") :temperature 20}
                                                      {:id 2 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:25:00") :temperature 21}
                                                      {:id 3 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:26:00") :temperature 22}
                                                      {:id 4 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:27:00") :temperature 20}
                                                      {:id 5 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:28:00") :temperature 19}
                                                      {:id 6 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:29:00") :temperature 20}
                                                      {:id 7 :time (js/Date.
                                                                     "March
                                                                     17, 2021
                                                                     15:30:00") :temperature 23}]}}
  (ui-temperature-chart {:temperature     temperature
                         :toggle-settings toggle-settings
                         :color           color
                         :min-bound       min-bound
                         :max-bound       max-bound
                         :chart-type      chart-type}))

(def ui-temperature-data (comp/factory TemperatureData))

(defsc HumidityChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin  "0px"
                         :padding "0px"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (create-card-title "Percent Humidity"
                          mdb/toggle-humidity-settings!
                          (:toggle-settings props))
             :cover     (create-rechart (:chart-type props)
                          {:data         (:humidity props)
                           :x-axis-label "Time"
                           :y-axis-label "Percentage"
                           :unit-symbol  "%"
                           :data-key     "humidity"
                           :id           "humidity-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (create-dropdown-settings (:toggle-settings props)
                          mdb/set-humidity-start-end-datetime!
                          mdb/set-humidity-color! mdb/set-humidity-min-bound!
                          mdb/set-humidity-max-bound!
                          mdb/redo-humidity-min-max-bound!
                          (:chart-type props)
                          mdb/set-humidity-chart-type!)}))

(def ui-humidity-chart (comp/factory HumidityChart))

(defsc HumidityData [this {:humidity-data/keys [id humidity start-date
                                                end-date toggle-settings color
                                                min-bound max-bound chart-type]
                           :as                 props}]
  {:query         [:humidity-data/id {:humidity-data/humidity (comp/get-query dl/HumidityReading)}
                   :humidity-data/start-date :humidity-data/end-date
                   :humidity-data/toggle-settings :humidity-data/color
                   :humidity-data/min-bound :humidity-data/max-bound
                   :humidity-data/chart-type]
   :ident         :humidity-data/id
   :initial-state {:humidity-data/id              1
                   :humidity-data/toggle-settings false
                   :humidity-data/min-bound       js/NaN
                   :humidity-data/max-bound       js/NaN
                   :humidity-data/chart-type      "line"
                   :humidity-data/color           ant/blue-primary
                   :humidity-data/start-date      (js/Date.)
                   :humidity-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :humidity-data/humidity        [{:id 1 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:24:00") :humidity 40}
                                                   {:id 2 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:25:00") :humidity 42}
                                                   {:id 3 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:26:00") :humidity 39}
                                                   {:id 4 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:27:00") :humidity 40}
                                                   {:id 5 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:28:00") :humidity 39}
                                                   {:id 6 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:29:00") :humidity 38}
                                                   {:id 7 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:30:00") :humidity 40}]}}
  (ui-humidity-chart {:humidity        humidity
                      :toggle-settings toggle-settings
                      :color           color
                      :min-bound       min-bound
                      :max-bound       max-bound
                      :chart-type      chart-type}))

(def ui-humidity-data (comp/factory HumidityData))

(defsc PressureChart [this props]
  (ant/card {:style     {:width     500
                         :textAlign "center"}
             :bodyStyle {:margin          "0px"
                         :padding         "0px"
                         :backgroundColor "black"}
             :headStyle {:backgroundColor "#001529"
                         :color           "white"}
             :title     (create-card-title "Pressure in Hectopascals"
                          mdb/toggle-pressure-settings!
                          (:toggle-settings props))
             :cover     (create-rechart (:chart-type props)
                          {:data         (:pressure props)
                           :x-axis-label "Time"
                           :y-axis-label "Pressure (hPa)"
                           :unit-symbol  ""
                           :data-key     "pressure"
                           :id           "pressure-id"
                           :color        (:color props)
                           :min-bound    (:min-bound props)
                           :max-bound    (:max-bound props)})
             :actions   (create-dropdown-settings (:toggle-settings props)
                          mdb/set-pressure-start-end-datetime!
                          mdb/set-pressure-color!
                          mdb/set-pressure-min-bound!
                          mdb/set-pressure-max-bound!
                          mdb/redo-pressure-min-max-bound!
                          (:chart-type props)
                          mdb/set-pressure-chart-type!)}))

(def ui-pressure-chart (comp/factory PressureChart))

(defsc PressureData [this {:pressure-data/keys [id pressure start-date
                                                end-date toggle-settings
                                                color min-bound max-bound
                                                chart-type] :as
                                               props}]
  {:query         [:pressure-data/id {:pressure-data/pressure (comp/get-query dl/PressureReading)}
                   :pressure-data/start-date :pressure-data/end-date
                   :pressure-data/toggle-settings :pressure-data/color
                   :pressure-data/min-bound :pressure-data/max-bound
                   :pressure-data/chart-type]
   :ident         :pressure-data/id
   :initial-state {:pressure-data/id              1
                   :pressure-data/toggle-settings false
                   :pressure-data/min-bound       js/NaN
                   :pressure-data/max-bound       js/NaN
                   :pressure-data/chart-type      "line"
                   :pressure-data/color           ant/blue-primary
                   :pressure-data/start-date      (js/Date.)
                   :pressure-data/end-date        (js/Date. (.setHours (js/Date.) (- (.getHours (js/Date.)) 24)))
                   :pressure-data/pressure        [{:id 1 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:24:00") :pressure 1000}
                                                   {:id 2 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:25:00") :pressure 997}
                                                   {:id 3 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:26:00") :pressure 1001}
                                                   {:id 4 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:27:00") :pressure 999}
                                                   {:id 5 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:28:00") :pressure 1000}
                                                   {:id 6 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:29:00") :pressure 998}
                                                   {:id 7 :time (js/Date.
                                                                  "March
                                                                  17, 2021
                                                                  15:30:00") :pressure 1000}]}}
  (ui-pressure-chart {:pressure        pressure
                      :toggle-settings toggle-settings
                      :color           color
                      :min-bound       min-bound
                      :max-bound       max-bound
                      :chart-type      chart-type}))

(def ui-pressure-data (comp/factory PressureData))

(defsc Charts [this {:charts/keys [humidity temperature pressure] :as props}]
  {:query         [{:charts/humidity (comp/get-query HumidityData)}
                   {:charts/temperature (comp/get-query TemperatureData)}
                   {:charts/pressure (comp/get-query PressureData)}]
   :ident         (fn [_ _] [:component/id ::charts])
   :initial-state {:charts/humidity    {}
                   :charts/temperature {}
                   :charts/pressure    {}}}
  (div
    (ant/row {}
      (ant/col {} (ui-humidity-data humidity))
      (ant/col {} (ui-temperature-data temperature))
      (ant/col {} (ui-pressure-data pressure)))))

(def ui-charts (comp/factory Charts))

(defsc Dashboard [this {:dashboard/keys [charts] :as props}]
  {:query         [{:dashboard/charts (comp/get-query Charts)}]
   :initial-state {:dashboard/charts {}}
   :ident         (fn [] [:component/id :dashboard])
   :route-segment ["dashboard"]}
  (div
    (ant/row {}
      (ant/col {}
        (ui-charts charts)))))




(comment
  ;(def layout [{:i "a" :x 0 :y 0 :w 1 :h 2 :static true}
  ;             {:i "b" :x 1 :y 0 :w 3 :h 2 :minW 2 :maxW 4}
  ;             {:i "c" :x 4 :y 0 :w 1 :h 2}])

  ;(responsive-grid-layout {:className   "layout"
  ;                         :layouts     layout
  ;                         :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
  ;                         :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}}
  ;  (div {:key 1
  ;        :style {:backgroundColor "blue"}} 1)
  ;  (div {:key 2
  ;        :style {:backgroundColor "red"}} 2)
  ;  (div {:key 3
  ;        :style {:backgroundColor "white"}} 3))
  )