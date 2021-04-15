(ns app.application
  (:require
    [com.fulcrologic.fulcro.networking.http-remote :as net]
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.networking.websockets :as fws]
    [taoensso.timbre :as log]))

(defn push-handler [{:keys [topic msg] :as data}]
  ;(print "push handler received!:" (first (js->clj msg)))
  ;(log/info "into" (into {} (js->clj msg)))
  ;(log/info "orig" (array-map (js->clj msg)))
  ;(log/info "huh?" (get-in ["system" "uuid"] msg))
  (log/info "test" msg)
  )

(def secured-request-middleware
  ;; The CSRF token is embedded via server_components/html.clj
  (->
    (net/wrap-csrf-token (or js/fulcro_network_csrf_token "TOKEN-NOT-IN-HTML!"))
    (net/wrap-fulcro-request)))

(defonce SPA (app/fulcro-app
               {;; This ensures your client can talk to a CSRF-protected server.
                ;; See middleware.clj to see how the token is embedded into the HTML
                :remotes {:remote (net/fulcro-http-remote
                                    {:url                "/api"
                                     :request-middleware secured-request-middleware})
                          :ws-remote (fws/fulcro-websocket-remote
                                       {:push-handler push-handler})}}))

(comment
  (-> SPA (::app/runtime-atom) deref ::app/indexes))
