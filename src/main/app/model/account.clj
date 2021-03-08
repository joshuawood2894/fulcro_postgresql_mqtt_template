(ns app.model.account
  (:require
    [app.model.database :as db]
    [com.wsscode.pathom.connect :as pc :refer [defresolver defmutation]]
    [taoensso.timbre :as log]))

;; Fulcro Inspect Test -- [:all-accounts]
(defresolver all-user-ids-resolver [{:keys [db/pool] :as env} _]
             {::pc/output [{:all-accounts [:account/id]}]}
             {:all-accounts
                (db/execute! db/pool {:select [:id]
                                      :from [:accounts]})})

;; Fulcro Inspect Test -- [{[:account/email <email>][:account/id]}]
(defresolver user-id-by-email-resolver [{:keys [db/pool] :as env} {:keys [account/email] :as input}]
             {::pc/input  #{:account/email}
              ::pc/output [:account/id]}
             {:account/id (db/execute-one! db/pool {:select [:id]
                                                    :from   [:accounts]
                                                    :where  [:= :email (:account/email input)]})})

;; Fulcro Inspect Test -- [{[:account/id <id>][:account/email]}]
(defresolver user-email-by-id-resolver [{:keys [db/pool] :as env} {:keys [account/id] :as input}]
             {::pc/input  #{:account/id}
              ::pc/output [:account/email]}
             {:account/email (db/execute-one! db/pool {:select [:email]
                                                       :from   [:accounts]
                                                       :where  [:= :id (:account/id input)]})})

;; Fulcro Inspect Test -- [{[:account/id <id>][:account/active?]}]
(defresolver user-account-status-by-id-resolver [{:keys [db/pool] :as env} {:keys [account/id] :as input}]
             {::pc/input  #{:account/id}
              ::pc/output [:account/active?]}
             {:account/active? (db/execute-one! db/pool {:select [:active]
                                                         :from   [:accounts]
                                                         :where  [:= :id (:account/id input)]})})

(def resolvers [all-user-ids-resolver
                user-id-by-email-resolver
                user-email-by-id-resolver
                user-account-status-by-id-resolver])
