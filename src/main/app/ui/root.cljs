(ns app.ui.root
  (:require
    ["antd" :refer [Layout Menu Breadcrumb Breadcrumb.Item Layout.Header Layout.Content
                    Layout.Footer Layout.Sider Menu.SubMenu Menu.Item Row Col]]
    ["@ant-design/icons" :refer [LineChartOutlined HomeOutlined
                                 SettingOutlined]]
    [app.ui.auth :as auth]
    [app.ui.main :as main]
    [app.ui.session :as session]
    [app.ui.settings :as settings]
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b hr]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as uism :refer [defstatemachine]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    [taoensso.timbre :as log]))

(def layout (interop/react-factory Layout))
(def menu (interop/react-factory Menu))
(def breadcrumb (interop/react-factory Breadcrumb))
(def breadcrumb-item (interop/react-factory Breadcrumb.Item))
(def line-chart-outlined (interop/react-factory LineChartOutlined))
(def home-outlined (interop/react-factory HomeOutlined))
(def setting-outlined (interop/react-factory SettingOutlined))
(def layout-header (interop/react-factory Layout.Header))
(def layout-content (interop/react-factory Layout.Content))
(def layout-footer (interop/react-factory Layout.Footer))
(def layout-sider (interop/react-factory Layout.Sider))
(def menu-submenu (interop/react-factory Menu.SubMenu))
(def menu-item (interop/react-factory Menu.Item))
(def row (interop/react-factory Row))
(def col (interop/react-factory Col))

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

      (layout {:style {:minHeight "100vh"}}
              (layout-sider {:collapsible true}
                            (div {:style {:height "32px"
                                          :margin "16px"
                                          :marginBottom "3vh"
                                          :background "rgba(255, 255, 255, 0.3)"}})
                            (menu {:theme "dark"
                                   :defaultSelectedKeys "1"
                                   :mode "inline"}
                                  (menu-item {:key "1"
                                              :icon (home-outlined)
                                              :onClick (fn [] (dr/change-route this ["main"]))}
                                             "Main")
                                  (when (= true (:session/valid? current-session))
                                    (menu-submenu {:key   "sub1"
                                                   :icon  (line-chart-outlined)
                                                   :title "Gateway"}
                                                  (menu-item {:key "2"} "Dashboard")
                                                  (menu-item {:key "3"} "Logger")))
                                  (when (= true (:session/valid? current-session))
                                    (menu-item {:key     "5"
                                               :icon    (setting-outlined)
                                               :onClick (fn [] (dr/change-route this ["settings"]))}
                                              "Settings"))
                                  ))
              (layout {:style {:background "#eee"}}
                      (layout-header {:style {:background "#fff"
                                              :padding "0"
                                              :bottomBorder "5px solid black"}}
                                     (row {}
                                       (col {:span 12})
                                       (col {:span 12
                                             :style {:paddingRight "40px"
                                                     :textAlign "right"}}
                                            (auth/ui-login login))))
                      (layout-content {:style {:margin "16px"}}
                                      (div {:style {:background "#fff"
                                                    :padding 24
                                                    :minHeight 360}}
                                           (ui-top-router router)))
                      (layout-footer {:style {:textAlign "center"}}
                                     (dom/a {:href "https://pcdworks.com"
                                             :target "_blank"}
                                            (dom/img {:src "images/PCDlogo.svg"
                                                      :alt "pcdworks.com"
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
