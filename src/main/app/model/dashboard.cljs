(ns app.model.dashboard
  (:require
    [app.application :refer [SPA]]
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))


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

