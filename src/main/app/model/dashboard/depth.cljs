(ns app.model.dashboard.depth
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-depth-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/min-bound min :depth-data/max-bound max))))

(defmutation set-depth-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/chart-type chart-type))))

(defmutation set-depth-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/min-bound min))))

(defmutation set-depth-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/max-bound min))))

(defmutation set-depth-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/color color))))

(defmutation toggle-depth-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/toggle-settings (not toggle-settings)))))

(defmutation set-depth-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :depth-data]
      #(assoc % :depth-data/start-date start :depth-data/end-date end))))
