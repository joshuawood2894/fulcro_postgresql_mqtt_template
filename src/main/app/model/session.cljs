(ns app.model.session
  (:require
    [app.application :refer [SPA]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as uism]
    [taoensso.timbre :as log]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro.algorithms.denormalize :as fdn]
    [clojure.string :as str]))

(defn clear [env]
  (uism/assoc-aliased env :error ""))

(defn logout [env]
  (dr/change-route SPA ["main"])
  (-> env
    (clear)
    (uism/assoc-aliased :session-valid? false :current-user "" :user-id "")
    (uism/trigger-remote-mutation :actor/login-form 'app.model.session/logout {})
    (uism/activate :state/logged-out)))

(defn signup-modal-enter [env]
  (-> env
    (clear)
    (uism/update-aliased :login-modal-open? not)
    (uism/update-aliased :signup-modal-open? not)
    (uism/activate :state/signup)
    ))

(defn signup-modal-exit [env]
  (-> env
    (clear)
    (uism/update-aliased :login-modal-open? not)
    (uism/update-aliased :signup-modal-open? not)
    (uism/activate :state/logged-out)))

(defn login [{::uism/keys [event-data] :as env}]
  (-> env
    (clear)
    (uism/trigger-remote-mutation :actor/login-form 'app.model.session/login
      {:email             (:email event-data)
       :password          (:password event-data)
       ::m/returning      (uism/actor-class env :actor/current-session)
       ::uism/ok-event    :event/complete
       ::uism/error-event :event/failed})
    (uism/activate :state/checking-session)))

(defn process-session-result [env error-message]
  (let [success? (uism/alias-value env :session-valid?)]
    (when success?
      (dr/change-route SPA ["main"]))
    (cond-> (clear env)
      success? (->
                 (uism/assoc-aliased :login-modal-open? false)
                 (uism/activate :state/logged-in))
      (not success?) (->
                       (uism/assoc-aliased :error error-message)
                       (uism/activate :state/logged-out)))))

(def global-events
  {:event/toggle-login-modal {::uism/handler (fn [env] (uism/update-aliased env
                                                         :login-modal-open? not))}})

(uism/defstatemachine session-machine
  {::uism/actors
   #{:actor/login-form :actor/current-session :actor/signup-form}

   ::uism/aliases
   {:email              [:actor/login-form :account/email]
    :error              [:actor/login-form :ui/error]
    :login-modal-open?  [:actor/login-form :ui/open?]
    :session-valid?     [:actor/current-session :session/valid?]
    :current-user       [:actor/current-session :account/email]
    :user-id            [:actor/current-session :account/id]
    :signup-modal-open? [:actor/signup-form :ui/signup-open?]}

   ::uism/states
   {:initial
    {::uism/target-states #{:state/logged-in :state/logged-out}
     ::uism/events        {::uism/started  {::uism/handler (fn [env]
                                                             (dr/change-route SPA ["main"])
                                                             (-> env
                                                               (uism/assoc-aliased :error "")
                                                               (uism/load ::current-session :actor/current-session
                                                                 {::uism/ok-event    :event/complete
                                                                  ::uism/error-event :event/failed})))}
                           :event/failed   {::uism/target-state :state/logged-out}
                           :event/complete {::uism/target-states #{:state/logged-in :state/logged-out}
                                            ::uism/handler       #(process-session-result % "")}}}

    :state/checking-session
    {::uism/events
     (merge global-events
       {:event/failed   {::uism/target-states #{:state/logged-out}
                         ::uism/handler       (fn [env]
                                                (-> env
                                                  (clear)
                                                  (uism/assoc-aliased :error "Server error.")))}
        :event/complete {::uism/target-states #{:state/logged-out :state/logged-in}
                         ::uism/handler       #(process-session-result % "Invalid Credentials.")}})}

    :state/logged-in
    {::uism/events
     (merge global-events
       {:event/logout {::uism/target-states #{:state/logged-out}
                       ::uism/handler       logout}})}

    :state/logged-out
    {::uism/events
     (merge global-events
       {:event/login              {::uism/target-states #{:state/checking-session}
                                   ::uism/handler       login}
        :event/enter-signup-modal {::uism/target-states #{:state/signup}
                                   ::uism/handler       signup-modal-enter
                                   }})}

    :state/signup
    {::uism/events
     {:event/exit-signup-modal {::uism/target-states #{:state/logged-out}
                                ::uism/handler       signup-modal-exit}}}}})


(def signup-ident [:component/id :signup])
(defn signup-class [] (comp/registry-key->class :app.ui.auth/Signup))

(defn clear-signup-form*
  "Mutation helper: Updates state map with a cleared signup form that is configured for form state support."
  [state-map]
  (-> state-map
    (assoc-in signup-ident
      {:account/email          ""
       :account/password       ""
       :account/password-again ""})
    (fs/add-form-config* (signup-class) signup-ident)))

(defmutation clear-signup-form [_]
  (action [{:keys [state]}]
    (swap! state clear-signup-form*)))

(defn signup-valid?
  [{:account/keys [email password password-again] :as form} field]
  (try
    (case field
      :account/email (str/includes? email "@")
      :account/password (> (count password) 7)
      :account/password-again (= password-again password)
      true)
    (catch :default _
      false)))

(def signup-validator (fs/make-validator signup-valid?))

(defmutation signup-success! [{:keys [email password] :as params}]
  (action [{:keys [app state]}]
    (js/console.log "signup-success!: " email))
  (remote [env] true)
  (ok-action [{:keys [app state]}]
    (dr/change-route app ["signup-success"])))

(defmutation signup! [{:keys [email password] :as params}]
  (action [{:keys [app state]}]
    (let [ident signup-ident
          completed-state (fs/mark-complete* @state ident)
          form (get-in completed-state ident)
          Signup (comp/registry-key->class :app.ui.auth/Signup)
          signup-props (fdn/db->tree (comp/get-query Signup) form completed-state)
          valid? (= :valid (signup-validator signup-props))]
      (js/console.log "Marking complete")
      (if valid?
        (comp/transact! app [(signup-success! params)])
        (reset! state completed-state)))))

