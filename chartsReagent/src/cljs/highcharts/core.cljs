(ns highcharts.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [highcharts.charts :as hc]))
;; -------------------------
;; Views
(defn home-page []
  [:div.ui.grid.container
   [:div.column
    [:h1.ui.header "Welcome to highcharts"]
    [hc/pie-chart "Top Clients" [["A" 1] ["B" 2] ["C" 3]]]
    [hc/highchart hc/column-chart-base-config]
    [hc/highchart hc/basic-line-chart-base-config]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
