(ns app.model.dashboard.pressure
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-pressure-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/min-bound min :pressure-data/max-bound max))))

(defmutation set-pressure-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/chart-type chart-type))))

(defmutation set-pressure-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/min-bound min))))

(defmutation set-pressure-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/max-bound min))))

(defmutation set-pressure-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/color color))))

(defmutation toggle-pressure-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/toggle-settings (not toggle-settings)))))

(defmutation set-pressure-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/start-date start :pressure-data/end-date end))))
