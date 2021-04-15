(ns app.model.dashboard
  (:require
    [app.application :refer [SPA]]
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.components :as comp]
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

(defmutation redo-temperature-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/min-bound min :temperature-data/max-bound max))))

(defmutation set-temperature-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/chart-type chart-type))))

(defmutation set-temperature-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/min-bound min))))

(defmutation set-temperature-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/max-bound min))))

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

(defmutation set-temperature-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/color color))))

(defmutation set-pressure-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/color color))))

(defmutation set-humidity-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/color color))))

(defmutation toggle-humidity-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/toggle-settings (not toggle-settings)))))

(defmutation toggle-pressure-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/toggle-settings (not toggle-settings)))))

(defmutation toggle-temperature-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/toggle-settings (not toggle-settings)))))

(defmutation set-humidity-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:humidity-data/id 1]
      #(assoc % :humidity-data/start-date start :humidity-data/end-date end))))

(defmutation set-temperature-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:temperature-data/id 1]
      #(assoc % :temperature-data/start-date start :temperature-data/end-date end))))

(defmutation set-pressure-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:pressure-data/id 1]
      #(assoc % :pressure-data/start-date start :pressure-data/end-date end))))

