(ns app.model.dashboard.ph
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-ph-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/min-bound min :ph-data/max-bound max))))

(defmutation set-ph-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/chart-type chart-type))))

(defmutation set-ph-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/min-bound min))))

(defmutation set-ph-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/max-bound min))))

(defmutation set-ph-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/color color))))

(defmutation toggle-ph-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/toggle-settings (not toggle-settings)))))

(defmutation set-ph-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :ph-data]
      #(assoc % :ph-data/start-date start :ph-data/end-date end))))
