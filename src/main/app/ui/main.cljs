(ns app.ui.main
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [taoensso.timbre :as log]
    [app.ui.recharts.area-chart :as ac]
    [app.ui.recharts.line-chart :as lc]
    [app.ui.antd.components :as ant]))


(defsc Main [this props]
  {:query         [:main/welcome-message]
   :initial-state {:main/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :main])
   :route-segment ["main"]}
  (div
    ;(ant/row {}
    ;  (ant/col {}
    ;    (ant/card {:style     {:width     400
    ;                           :textAlign "center"}
    ;               :bodyStyle {:margin  "0px"
    ;                           :padding "0px"}
    ;               :headStyle {:backgroundColor "#001529"
    ;                           :color           "white"}
    ;               :title     "Percent Humidity"
    ;               :cover     (ac/ui-area-chart {:data         data-hum
    ;                                             :x-axis-label "Time"
    ;                                             :y-axis-label "Percentage"
    ;                                             :unit-symbol  "%"
    ;                                             :color        ant/blue-primary})
    ;
    ;               :actions   [(dp/ui-range-picker {:key "setting"})]}))
    ;  (ant/col {} (ant/card {:style     {:width     400
    ;                                     :textAlign "center"}
    ;                         :bodyStyle {:margin  "0px"
    ;                                     :padding "0px"}
    ;                         :headStyle {:backgroundColor "#001529"
    ;                                     :color           "white"}
    ;                         :title     "Temperature in Degrees Celsius"
    ;                         :cover     (lc/ui-line-chart {:data         data-temp
    ;                                                       :x-axis-label "Time"
    ;                                                       :y-axis-label "Degrees Celsius"
    ;                                                       :unit-symbol  (char 176)
    ;                                                       :color        ant/blue-primary})
    ;
    ;                         :actions   [(dp/ui-range-picker {:key "setting"})]})))
    (h3 "Main")
    (p (str "Welcome to the Fulcro template. "
         "The Sign up and login functionalities are partially implemented, "
         "but mostly this is just a blank slate waiting "
         "for your project."))))