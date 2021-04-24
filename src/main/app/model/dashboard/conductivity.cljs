(ns app.model.dashboard.conductivity
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li h3 p]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation redo-conductivity-min-max-bound! [{:keys [min max] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/min-bound min :conductivity-data/max-bound max))))

(defmutation set-conductivity-chart-type! [{:keys [chart-type] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/chart-type chart-type))))

(defmutation set-conductivity-min-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/min-bound min))))

(defmutation set-conductivity-max-bound! [{:keys [min] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/max-bound min))))

(defmutation set-conductivity-color! [{:keys [color] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/color color))))

(defmutation toggle-conductivity-settings! [{:keys [toggle-settings] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/toggle-settings (not toggle-settings)))))

(defmutation set-conductivity-start-end-datetime! [{:keys [start end] :as params}]
  (action [{:keys [app state]}]
    (swap! state update-in [:conductivity-data/id 1]
      #(assoc % :conductivity-data/start-date start :conductivity-data/end-date end))))
