(ns app.ui.auth
  (:require
    [app.ui.antd.components :as ant]
    [app.ui.session :as session]
    [app.model.session :as m-session]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 b]]
    [com.fulcrologic.fulcro.dom.html-entities :as ent]
    [com.fulcrologic.fulcro.dom.events :as evt]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as uism :refer [defstatemachine]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro-css.css :as css]
    [taoensso.timbre :as log]))

(defn field [{:keys [label valid? error-message] :as props}]
  (let [input-props (-> props (assoc :name label) (dissoc :label :valid? :error-message))]
    (div :.ui.field
      (dom/label {:htmlFor label} label)
      (dom/input input-props)
      (dom/div :.ui.error.message {:classes [(when valid? "hidden")]}
        error-message))))

(defsc SignupSuccess [this props]
  {:query         ['*]
   :initial-state {}
   :ident         (fn [] [:component/id :signup-success])
   :route-segment ["signup-success"]}
  (div
    (dom/h3 "Signup Complete!")
    (dom/p "You can now log in!")))

(defsc Signup [this {:account/keys [email password password-again] :as props}]
  {:query             [:account/email :account/password :account/password-again fs/form-config-join]
   :initial-state     (fn [_]
                        (fs/add-form-config Signup
                          {:account/email          ""
                           :account/password       ""
                           :account/password-again ""}))
   :form-fields       #{:account/email :account/password :account/password-again}
   :ident             (fn [] m-session/signup-ident)
   :route-segment     ["signup"]
   :componentDidMount (fn [this]
                        (comp/transact! this [(m-session/clear-signup-form)]))}
  (let [submit! (fn [evt]
                  (when (or (identical? true evt) (evt/enter-key? evt))
                    (comp/transact! this [(m-session/signup! {:email email :password password})])
                    (log/info "Sign up")))
        checked? (fs/checked? props)]
    (div
      (dom/h3 "Signup")

      ;(ant/form {:labelCol       {:span 0}
      ;           :wrapperCol     {:span 12}
      ;           :name           "normal_login"
      ;           :initialValues  {:remember false}
      ;           :onFinish       (fn [] (js/console.log "onFinish"))
      ;           :onFinishFailed (fn [] (js/console.log "onFinishFailed"))}
      ;  (ant/form-item {:name  "email"
      ;                  :rules [{:message "Please input your email!"}]}
      ;    (ant/input {:prefix      (ant/user-outlined)
      ;                :placeholder "Email"
      ;                :onChange    #(m/set-string! this :account/email :event %)}))
      ;  (ant/form-item {:name  "password"
      ;                  :rules [{:message "Please input your password!"}]}
      ;    (ant/input-password {:prefix      (ant/lock-outlined)
      ;                         :placeholder "Password"
      ;                         :onChange    #(comp/set-state! this {:password (evt/target-value %)})})))

      (div :.ui.form {:classes [(when checked? "error")]}
           (field {:label         "Email"
                   :value         (or email "")
                   :valid?        (m-session/valid-email? email)
                   :error-message "Must be an email address"
                   :autoComplete  "off"
                   :onKeyDown     submit!
                   :onChange      #(m/set-string! this :account/email :event %)})
           (field {:label         "Password"
                   :type          "password"
                   :value         (or password "")
                   :valid?        (m-session/valid-password? password)
                   :error-message "Password must be at least 8 characters."
                   :onKeyDown     submit!
                   :autoComplete  "off"
                   :onChange      #(m/set-string! this :account/password :event %)})
           (field {:label         "Repeat Password" :type "password" :value (or password-again "")
                   :autoComplete  "off"
                   :valid?        (= password password-again)
                   :error-message "Passwords do not match."
                   :onChange      #(m/set-string! this :account/password-again :event %)})
           (dom/button :.ui.primary.button {:onClick #(submit! true)}
                       "Sign Up"))

      )))

(defsc Login [this {:account/keys [email]
                    :ui/keys      [error open?] :as props}]
  {:query         [:ui/open? :ui/error :account/email
                   {[:component/id :session] (comp/get-query session/Session)}
                   [::uism/asm-id ::session/session-id]]
   :initial-state {:account/email "" :ui/error ""}
   :ident         (fn [] [:component/id :login])}
  (let [current-state (uism/get-active-state this ::session/session-id)
        {current-user :account/email} (get props [:component/id :session])
        initial? (= :initial current-state)
        loading? (= :state/checking-session current-state)
        logged-in? (= :state/logged-in current-state)
        password (or (comp/get-state this :password) "")    ; c.l. state for security
        drop (ant/menu {:style {:textAlign "center"}}
               (ant/menu-item-group {:title current-user}
                 (ant/divider)
                 (ant/button {:onClick #(uism/trigger! this ::session/session-id :event/logout)
                              :style   {:margin "5px"}
                              :danger  true} "Logout")))]
    (div
      (when-not initial?
        (div
          (if logged-in?
            (ant/dropdown {:overlay drop
                           :trigger ["click"]}
              (ant/avatar {:shape "square"
                           :style {:backgroundColor "#001529"}
                           :icon  (ant/user-outlined)
                           :size  42}))
            (ant/button {:onClick #(uism/trigger! this ::session/session-id :event/toggle-modal)}
              "Login"))
          (ant/modal {:title        "Login"
                      :width        "20vw"
                      :style        {:minWidth "300px"}
                      :visible      open?
                      :closable     false
                      :maskClosable false
                      :okText       "Log in"
                      :onOk         (fn []
                                      (uism/trigger! this ::session/session-id :event/login {:email    email
                                                                                             :password password}))
                      :onCancel     (fn []
                                      (uism/trigger! this ::session/session-id :event/toggle-modal))}
            (ant/form {:labelCol       {:span 0}
                       :wrapperCol     {:span 24}
                       :name           "normal_login"
                       :initialValues  {:remember false}}
              (ant/form-item {:name  "emmail"}
                (ant/input {:prefix      (ant/user-outlined)
                            :placeholder "Email"
                            :onChange    #(m/set-string! this :account/email :event %)}))
              (ant/form-item {:name  "password"}
                (ant/input-password {:prefix      (ant/lock-outlined)
                                     :placeholder "Password"
                                     :onChange    #(comp/set-state! this {:password (evt/target-value %)})}))
              (when (not= error "")
                (dom/p {:style {:color "red"}} error))
              (ant/card {:style {:background "#eeee"}}
                (dom/p "Don't have an account?")
                (dom/p "Please "
                  (dom/a {:onClick (fn []
                                     (uism/trigger! this ::session/session-id :event/toggle-modal {})
                                     (dr/change-route this
                                       ["signup"]))} "Sign Up!"))))))))
    ;(dom/div
    ;    (when-not initial?
    ;      (dom/div :.right.menu
    ;               (if logged-in?
    ;                 (dom/button :.item
    ;                             {:onClick #(uism/trigger! this
    ;                                                       ::session/session-id
    ;                                                       :event/logout)}
    ;                             (dom/span current-user) ent/nbsp "Log out")
    ;                 (dom/div :.item {:style   {:position "relative"}
    ;                                  :onClick #(uism/trigger! this
    ;                                                           ::session/session-id :event/toggle-modal)
    ;                                  }
    ;                          "Login"
    ;                          (when open?
    ;                            (dom/div :.four.wide.ui.raised.teal.segment {:onClick (fn [e]
    ;                                                                                    ;; Stop bubbling (would trigger the menu toggle)
    ;                                                                                    (evt/stop-propagation! e))
    ;                                                                         :classes [floating-menu]}
    ;                                     (dom/h3 :.ui.header "Login")
    ;                                     (div :.ui.form {:classes [(when (seq error) "error")]}
    ;                                          (field {:label    "Email"
    ;                                                  :value    email
    ;                                                  :onChange #(m/set-string! this :account/email :event %)})
    ;                                          (field {:label    "Password"
    ;                                                  :type     "password"
    ;                                                  :value    password
    ;                                                  :onChange #(comp/set-state! this {:password (evt/target-value %)})})
    ;                                          (div :.ui.error.message error)
    ;                                          (div :.ui.field
    ;                                               (dom/button :.ui.button
    ;                                                           {:onClick (fn []
    ;                                                                       (uism/trigger! this ::session/session-id :event/login {:email email
    ;                                                                                                                                :password password}))
    ;                                                            :classes [(when loading? "loading")]} "Login"))
    ;                                          (div :.ui.message
    ;                                               (dom/p "Don't have an account?")
    ;                                               (dom/a {:onClick (fn []
    ;                                                                  (uism/trigger! this ::session/session-id :event/toggle-modal {})
    ;                                                                  (dr/change-route this ["signup"]))}
    ;                                                      "Please sign up!"))))))))))

    ))

(def ui-login (comp/factory Login))
