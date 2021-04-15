(ns app.server-components.websockets
  (:require [mount.core :refer [defstate]]
            [com.fulcrologic.fulcro.networking.websockets :as fws]
            [taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]]
            [app.server-components.pathom :refer [parser]]))

(defstate websockets
  :start (fws/start! (fws/make-websockets
                 parser
                 {:http-server-adapter (get-sch-adapter)
                  :parser-accepts-env? true
                  ;; I'm not going to cover how to handle CSRF well, if this is a toy project
                  ;;  this bit doesn't matter as much, if it isn't please sit down and read about it.
                  ;;  I've added some notes at the end.
                  :sente-options       {:csrf-token-fn nil}}))
  :stop (fws/stop! websockets))

(defn wrap-websockets [handler]
  (fws/wrap-api handler websockets))