(ns app.ui.leaflet.leaflet
  (:require
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.mutations :as m]
    ["leaflet" :as leaflet]))

(def URL-OSM "http://{s}.tile.osm.org/{z}/{x}/{y}.png")


(defn create-map [this]
  (let [props []
        start (array 39.5 -98.35)
        m (-> js/L
            (.map "mapid")
            (.setView start 15))
        tile1 (-> js/L (.tileLayer URL-OSM
                         #js{:maxZoom     19
                             :attribution "OOGIS RL, OpenStreetMap &copy;"}))
        base (clj->js {"OpenStreetMap" tile1})
        ctrl (-> js/L (.control.layers base nil))]

    (if (= (count props) 0)
      (do
        (.addTo tile1 m)
        (.addTo ctrl m)
        )
      (doseq [point props]
        (-> js/L
          (.marker #js[32.97
                       -95])
          (.addTo m)
          ;(.bindPopup (make-popup point))
          )
        (.addTo tile1 m)
        (.addTo ctrl m)))))

(defsc GPSMap [this props]
  (dom/div {:id "mapid"} "hello"))

(def ui-gps-map (comp/factory GPSMap))