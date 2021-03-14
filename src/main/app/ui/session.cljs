(ns app.ui.session
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]))

(defsc Session
  "Session representation. Used primarily for server queries. On-screen representation happens in Login component."
  [this {:keys [:session/valid? :account/email :account/id] :as props}]
  {:query         [:session/valid? :account/email :account/id]
   :ident         (fn [] [:component/id :session])
   :pre-merge     (fn [{:keys [data-tree]}]
                    (merge {:session/valid? false :account/email ""}
                           data-tree))
   :initial-state {:session/valid? false :account/email ""}})

(def ui-session (comp/factory Session))