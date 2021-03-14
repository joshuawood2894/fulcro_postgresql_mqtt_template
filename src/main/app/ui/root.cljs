(ns app.ui.root
  (:require
    [app.ui.auth :as auth]
    [app.ui.main :as main]
    [app.ui.session :as session]
    [app.ui.settings :as settings]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as uism :refer [defstatemachine]]
    [taoensso.timbre :as log]))

(dr/defrouter TopRouter [this props]
  {:router-targets [main/Main auth/Signup auth/SignupSuccess
                    settings/Settings]})

(def ui-top-router (comp/factory TopRouter))

(defsc TopChrome [this {:root/keys [router current-session login]}]
  {:query         [{:root/router (comp/get-query TopRouter)}
                   {:root/current-session (comp/get-query session/Session)}
                   [::uism/asm-id ::TopRouter]
                   {:root/login (comp/get-query auth/Login)}]
   :ident         (fn [] [:component/id :top-chrome])
   :initial-state {:root/router          {}
                   :root/login           {}
                   :root/current-session {}}}
  (let [current-tab (some-> (dr/current-route this this) first keyword)]
      (div :.ui.container
           (div :.ui.secondary.pointing.menu
               (dom/a :.item {:classes [(when (= :main current-tab) "active")]
                              :onClick (fn [] (dr/change-route this ["main"]))} "Main")
               (dom/a :.item {:classes [(when (= :settings current-tab) "active")]
                              :onClick (fn [] (dr/change-route this ["settings"]))} "Settings")
               (div :.right.menu
                    (auth/ui-login login))
                (session/ui-session current-session)
                )
           (div :.ui.grid
                (div :.ui.row
                     (ui-top-router router))))))

(def ui-top-chrome (comp/factory TopChrome))

(defsc Root [this {:root/keys [top-chrome]}]
  {:query         [{:root/top-chrome (comp/get-query TopChrome)}]
   :initial-state {:root/top-chrome {}}}
  (ui-top-chrome top-chrome))
