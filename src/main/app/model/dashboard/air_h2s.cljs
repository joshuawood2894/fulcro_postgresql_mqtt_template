(ns app.model.dashboard.air-h2s
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-air-h2s-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/min-bound min :air-h2s-data/max-bound max))))

(defmutation set-air-h2s-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/chart-type chart-type))))

(defmutation set-air-h2s-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/min-bound min))))

(defmutation set-air-h2s-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/max-bound min))))

(defmutation set-air-h2s-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/color color))))

(defmutation toggle-air-h2s-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/toggle-settings (not toggle-settings)))))

(defmutation set-air-h2s-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:air-h2s-data/id 1]
      #(assoc % :air-h2s-data/start-date start :air-h2s-data/end-date end))))
