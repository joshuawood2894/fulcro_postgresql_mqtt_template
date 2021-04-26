(ns app.model.dashboard.air-temperature
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [app.ui.data-logger.air-temperature :as dlat]))

(defmutation redo-air-temperature-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/min-bound min :air-temperature-data/max-bound max))))

(defmutation set-air-temperature-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/chart-type chart-type))))

(defmutation set-air-temperature-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/min-bound min))))

(defmutation set-air-temperature-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/max-bound min))))

(defmutation set-air-temperature-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/color color))))

(defmutation toggle-air-temperature-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/toggle-settings (not toggle-settings)))))

(defmutation set-air-temperature-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:component/id :air-temperature-data]
      #(assoc % :air-temperature-data/start-date start :air-temperature-data/end-date end))))
