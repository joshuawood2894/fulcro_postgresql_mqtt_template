(ns app.ui.root
  (:require
    [app.ui.auth :as auth]
    [app.ui.main :as main]
    [app.ui.session :as session]
    [app.ui.settings :as settings]
    [app.ui.dashboard :as dashboard]
    [app.ui.data-logger :as datal]
    [app.ui.mqtt-logger :as mqttl]
    [app.ui.antd.components :as ant]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b hr]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as uism :refer [defstatemachine]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [taoensso.timbre :as log]))


(dr/defrouter TopRouter [this props]
  {:router-targets [main/Main auth/Signup auth/SignupSuccess
                    settings/Settings dashboard/Dashboard datal/DataLogger
                    mqttl/MqttLogger]})

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
    ;(div :.ui.container
    ;     (div :.ui.secondary.pointing.menu
    ;         (dom/a :.item {:classes [(when (= :main current-tab) "active")]
    ;                        :onClick (fn [] (dr/change-route this ["main"]))} "Main")
    ;         (dom/a :.item {:classes [(when (= :settings current-tab) "active")]
    ;                        :onClick (fn [] (dr/change-route this ["settings"]))} "Settings")
    ;         (div :.right.menu
    ;              (auth/ui-login login)))
    ;     (div :.ui.grid
    ;          (div :.ui.row
    ;               (ui-top-router router))))

    (ant/layout {:style {:minHeight "100vh"}}
      (ant/layout-sider {:collapsible true}
        (div {:style {:height       "32px"
                      :margin       "16px"
                      :marginBottom "3vh"
                      :background   "rgba(255, 255, 255, 0.3)"}})
        (ant/menu {:theme               "dark"
                   :defaultSelectedKeys "1"
                   :mode                "inline"}
          (ant/menu-item {:key     "1"
                          :icon    (ant/home-outlined)
                          :onClick (fn [] (dr/change-route this ["main"]))}
            "Main")
          (when (= true (:session/valid? current-session))
            (ant/menu-submenu {:key   "sub1"
                           :icon  (ant/line-chart-outlined)
                           :title "Gateway"}
              (ant/menu-item {:key     "2"
                          :onClick (fn [] (dr/change-route this ["dashboard"]))} "Dashboard")
              (ant/menu-item {:key     "3"
                          :onClick (fn [] (dr/change-route this ["data-logger"]))} "Data Logger")
              (ant/menu-item {:key     "4"
                          :onClick (fn [] (dr/change-route this ["mqtt-logger"]))} "MQTT Logger")))
          (when (= true (:session/valid? current-session))
            (ant/menu-item {:key     "5"
                        :icon    (ant/setting-outlined)
                        :onClick (fn [] (dr/change-route this ["settings"]))}
              "Settings"))
          ))
      (ant/layout {:style {:background "#eee"}}
        (ant/layout-header {:style {:background   "#fff"
                                :padding      "0"
                                :bottomBorder "5px solid black"}}
          (ant/row {}
            (ant/col {:span 12})
            (ant/col {:span  12
                  :style {:paddingRight "40px"
                          :textAlign    "right"}}
              (auth/ui-login login))))
        (ant/layout-content {:style {:margin "16px"}}
          (div {:style {:background "#eee"
                        :padding    24
                        :minHeight  360}}
            (ui-top-router router)))
        (ant/layout-footer {:style {:textAlign  "center"
                                :background "#fff"}}
          (dom/a {:href   "https://pcdworks.com"
                  :target "_blank"}
            (dom/img {:src   "images/PCDlogo.svg"
                      :alt   "pcdworks.com"
                      :width "300px"})))))))

(def ui-top-chrome (comp/factory TopChrome))

(defsc Root [this {:root/keys [top-chrome]}]
  {:query         [{:root/top-chrome (comp/get-query TopChrome)}]
   :initial-state {:root/top-chrome {}}}
  (ui-top-chrome top-chrome)
  ;(layout {:style {:minHeight "100vh"}}
  ;        (layout-sider {:collapsible true}
  ;                      (div {:style {:height "32px"
  ;                                    :margin "16px"
  ;                                    :marginBottom "3vh"
  ;                                    :background "rgba(255, 255, 255, 0.3)"}})
  ;                      (menu {:theme "dark"
  ;                             :defaultSelectedKeys "1"
  ;                             :mode "inline"}
  ;                            (menu-item {:key "1"
  ;                                        :icon (line-chart-outlined)}
  ;                                       "Outline 1")
  ;                            (menu-item {:key "2"
  ;                                        :icon (desktop-outlined)}
  ;                                       "Outline 2")
  ;                            (menu-submenu {:key "sub1"
  ;                                           :icon (user-outlined)
  ;                                           :title "User"}
  ;                                          (menu-item {:key "3"}
  ;                                                     "Tom")
  ;                                          (menu-item {:key "4"} "Bill")
  ;                                          (menu-item {:key "5"} "Alex"))
  ;                            (menu-submenu {:key "sub2"
  ;                                           :icon (team-outlined)
  ;                                           :title "Team"}
  ;                                          (menu-item {:key "6"} "Team 1")
  ;                                          (menu-item {:key "7"} "Team 2"))
  ;                            (menu-item {:key "8"
  ;                                        :icon (file-outlined)}
  ;                                       "Files")))
  ;        (layout {:style {:background "#ddd"}}
  ;                (layout-header {:style {:background "#fff"
  ;                                        :padding "0"
  ;                                        :bottomBorder "5px solid black"}}
  ;                               (breadcrumb {:style {:margin "16px"}}
  ;                                           (breadcrumb-item {} "User")
  ;                                           (breadcrumb-item {} "Bill")))
  ;                (layout-content {:style {:margin "16px"}}
  ;                                (div {:style {:background "#fff"
  ;                                              :padding 24
  ;                                              :minHeight 360}} "Bill is a Dog"))
  ;                (layout-footer {:style {:textAlign "center"}} (dom/a {:href
  ;                                                                      "https://pcdworks.com"
  ;                                                                      :target "_blank"}
  ;                                                                     (dom/img {:src "images/PCDlogo.svg"
  ;                                                                               :alt "pcdworks.com"
  ;                                                                               :width "300px"})))))
  )
