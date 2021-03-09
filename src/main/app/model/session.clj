(ns app.model.session
  (:require
    [app.model.database :as db]
    [buddy.hashers :as hs]
    [com.fulcrologic.guardrails.core :refer [>defn => | ?]]
    [com.wsscode.pathom.connect :as pc :refer [defresolver defmutation]]
    [taoensso.timbre :as log]
    [clojure.spec.alpha :as s]
    [com.fulcrologic.fulcro.server.api-middleware :as fmw]))

(defresolver current-session-resolver [env input]
  {::pc/output [{::current-session [:session/valid? :account/email :account/id]}]}
  (let [{:keys [account/id account/email session/valid?]}
       (get-in env [:ring/request :session])]
   (if valid?
     (do
       (log/info email "already logged in!")
       {::current-session {:session/valid? true :account/email email :account/id id}})
     {::current-session {:session/valid? false}})))

(defn response-updating-session
  "Uses `mutation-response` as the actual return value for a mutation, but also stores the data into the (cookie-based) session."
  [mutation-env mutation-response]
  (let [existing-session (some-> mutation-env :ring/request :session)]
    (fmw/augment-response
      mutation-response
      (fn [resp]
        (let [new-session (merge existing-session mutation-response)]
          (assoc resp :session new-session))))))

(defmutation login [{:keys [db/pool] :as env} {:keys [email password]}]
  {::pc/output [:session/valid? :account/email]}
  (log/info "Authenticating " email)
  (let [account (db/execute-one! db/pool
                                 {:select [:id :email :passphrase]
                                  :from   [:accounts]
                                  :where  [:= :email email]})
        {expected-email    :accounts/email
         expected-password :accounts/passphrase
         expected-id       :accounts/id} account]
    (if (and (= email expected-email) (hs/check password expected-password))
      (response-updating-session env
        {:session/valid? true
         :account/email  email
         :account/id     expected-id})
      (do
        (log/error "Invalid credentials supplied for" email)
        (throw (ex-info "Invalid credentials" {:email email}))))))

(defmutation logout [env params]
  {::pc/output [:session/valid?]}
  (response-updating-session env {:session/valid? false :account/email ""}))

(defmutation signup! [{:keys [db/pool] :as env} {:keys [email password]}]
  {::pc/output [:signup/result]}
  (let [hashed-password (hs/derive password)]
   (log/info "Signing Up" email)
   (db/execute-one! db/pool
                    {:insert-into :accounts
                     :values      [{:email      email
                                    :passphrase hashed-password}]
                     :returning   [:id]})
   {:signup/result "OK"}))

(def resolvers [current-session-resolver login logout signup!])
