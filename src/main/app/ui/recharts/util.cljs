(ns app.ui.recharts.util
  (:require
    [clojure.string :as s]))

(defn format-date [date]
  (let [month (.getMonth date)
        day (.getDay date)
        hour-military (.getHours date)
        hour (if (< hour-military 13)
               hour-military
               (- hour-military 12))
        am-pm (if (< hour-military 13)
                "AM" "PM")
        minute (.getMinutes date)
        second (.getSeconds date)]
    (str month "/" day " " hour ":" minute ":" second " " am-pm)))

(defn darken-color [color factor]
  (let [ colors (rest (s/split color #""))
        red (take 2 (drop 1 colors))
        red_ (int (* factor (.parseInt js/Number (str "0x" (s/join red)) 16)))
        red__ (if (< red_ 16)
                (str "0" (.toString red_ 16))
                (.toString red_ 16))
        green (take 2 (drop 3 colors))
        green_ (int (* factor (.parseInt js/Number (str "0x" (s/join green)) 16)))
        green__ (if (< green_ 16)
                  (str "0" (.toString green_ 16))
                  (.toString green_ 16))
        blue (take 2 (drop 5 colors))
        blue_ (int (* factor (.parseInt js/Number (str "0x" (s/join blue)) 16)))
        blue__ (if (< blue_ 16)
                 (str "0" (.toString blue_ 16))
                 (.toString blue_ 16))]
    (s/join ["#" red__ green__ blue__])))
