(ns app.model.dashboard.humidity
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-humidity-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/min-bound min :humidity-data/max-bound max))))

(defmutation set-humidity-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/chart-type chart-type))))

(defmutation set-humidity-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/min-bound min))))

(defmutation set-humidity-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/max-bound min))))

(defmutation set-humidity-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/color color))))

(defmutation toggle-humidity-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/toggle-settings (not toggle-settings)))))

(defmutation set-humidity-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/start-date start :humidity-data/end-date end))))
