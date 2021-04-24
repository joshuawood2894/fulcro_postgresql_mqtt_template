(ns app.ui.dashboard.config
  (:require
    ["react-color" :refer [TwitterPicker]]
    [app.application :refer [SPA]]
    [app.ui.recharts.line-chart :as lc]
    [app.ui.recharts.area-chart :as ac]
    [app.ui.recharts.scatter-chart :as sc]
    [app.ui.recharts.bar-chart :as bc]
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

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
