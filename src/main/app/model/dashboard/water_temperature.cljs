(ns app.model.dashboard.water-temperature
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-water-temperature-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/min-bound min :water-temperature-data/max-bound max))))

(defmutation set-water-temperature-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/chart-type chart-type))))

(defmutation set-water-temperature-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/min-bound min))))

(defmutation set-water-temperature-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/max-bound min))))

(defmutation set-water-temperature-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/color color))))

(defmutation toggle-water-temperature-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/toggle-settings (not toggle-settings)))))

(defmutation set-water-temperature-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:water-temperature-data/id 1]
      #(assoc % :water-temperature-data/start-date start :water-temperature-data/end-date end))))
