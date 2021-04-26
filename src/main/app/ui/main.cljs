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
    (h3 "Main")
    (p (str "Welcome to the Fulcro template. "
         "The Sign up and login functionalities are partially implemented, "
         "but mostly this is just a blank slate waiting "
         "for your project."))
    (dom/img {:src   "images/rowboat1.jpg"
              :alt   "rowboat1"
              :width "100%"
              })))