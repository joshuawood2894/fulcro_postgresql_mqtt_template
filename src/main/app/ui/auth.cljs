(ns app.ui.auth
  (:require
    ["antd" :refer [Drawer Form Button Col Row Input Input.Password Select
                    Select.Option Modal Form.Item Card Spin]]
    ["@ant-design/icons" :refer [UserOutlined LockOutlined]]
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

(def drawer (interop/react-factory Drawer))
(def form (interop/react-factory Form))
(def form-item (interop/react-factory Form.Item))
(def button (interop/react-factory Button))
(def col (interop/react-factory Col))
(def row (interop/react-factory Row))
(def input (interop/react-factory Input))
(def input-password (interop/react-factory Input.Password))
(def select (interop/react-factory Select))
(def select-option (interop/react-factory Select.Option))
(def modal (interop/react-factory Modal))
(def user-outlined (interop/react-factory UserOutlined))
(def lock-outlined (interop/react-factory LockOutlined))
(def card (interop/react-factory Card))
(def spin (interop/react-factory Spin))

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
  (let [submit!  (fn [evt]
                   (when (or (identical? true evt) (evt/enter-key? evt))
                     (comp/transact! this [(m-session/signup! {:email email :password password})])
                     (log/info "Sign up")))
        checked? (fs/checked? props)]
    (div
      (dom/h3 "Signup")
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
                       "Sign Up")))))

(defsc Login [this {:account/keys [email]
                    :ui/keys      [error open?] :as props}]
  {:query         [:ui/open? :ui/error :account/email
                   {[:component/id :session] (comp/get-query session/Session)}
                   [::uism/asm-id ::session/session-id]]
   :css           [[:.floating-menu {:position "absolute !important"
                                     :z-index  1000
                                     :width    "300px"
                                     :right    "0px"
                                     :top      "50px"}]]
   :initial-state {:account/email "" :ui/error ""}
   :ident         (fn [] [:component/id :login])}
  (let [current-state (uism/get-active-state this ::session/session-id)
        {current-user :account/email} (get props [:component/id :session])
        initial?      (= :initial current-state)
        loading?      (= :state/checking-session current-state)
        logged-in?    (= :state/logged-in current-state)
        {:keys [floating-menu]} (css/get-classnames Login)
        password      (or (comp/get-state this :password) "")] ; c.l. state for security
  (div
    (when-not initial?
      (div
        (if logged-in?
          (dom/span current-user
            (button {:onClick #(uism/trigger! this ::session/session-id :event/logout)
                     :style {:marginLeft "15px"}}
                   ent/nbsp "Log out"))
          (button {:onClick #(uism/trigger! this ::session/session-id :event/toggle-modal)}
                  "Login"))
        (modal {:title        "Login"
                :visible      open?
                :closable     false
                :maskClosable false
                :okText       "Log in"
                :onOk         (fn []
                                (uism/trigger! this ::session/session-id :event/login {:email    email
                                                                                       :password password}))
                :onCancel     (fn []
                                (uism/trigger! this ::session/session-id :event/toggle-modal))}
               (form {:labelCol {:span 0}
                      :wrapperCol {:span 24}
                      :name          "normal_login"
                      :className "login-form"
                      :initialValues {:remember false}
                      :onFinish (fn [] (js/console.log "onFinish"))
                      :onFinishFailed (fn [] (js/console.log "onFinishFailed"))}
                     (form-item {:name "mmail"
                                 :rules [{:message "Please input your email!"}]}
                                (input {:prefix      (user-outlined)
                                        :placeholder "Email"
                                        :onChange #(m/set-string! this :account/email :event %)}))
                     (form-item {:name "password"
                                 :rules [{:message "Please input your
                                 password!"}]}
                                (input-password {:prefix (lock-outlined)
                                                 :placeholder "Password"
                                                 :onChange #(comp/set-state! this {:password (evt/target-value %)})
                                                 }))
                     (card {:style {:background "#eeee"}}
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
