(ns highcharts.charts
  (:require
    [reagent.core :as reagent :refer [atom]]
    [cljs.spec :as s]
    [cljsjs.highcharts]))

(defn highchart [config]
  (reagent/create-class
    {:reagent-render (fn [] [:div {:style {:min-width "310px" :max-width "800px"
                                           :height "400px" :margin "0 auto"}}])
     :component-did-mount #(js/Highcharts.Chart. (reagent/dom-node %) (clj->js config))}))

;; Pie Chart Config
(def pie-chart-base-config
  {:chart {:type "pie"}
   :tooltip {:pointFormat "{series.name}: {point.percentage:.1f}%"}
   :plotOptions {:pie {:allowPointSelect true
                       :cursor "pointer"
                       :dataLabels {:enabled true
                                    :format "{point.name}: {point.percentage:.1f}%"
                                    :style {:color (or (and js/Highcharts.theme js/Highcharts.theme.contrastTextColor) "black")}}}}})

(s/def ::pie-data (s/cat :name string? :y number?))
(s/def ::pie-series (s/coll-of ::pie-data))

(defn pie-chart [title series]
  (let [data (s/conform ::pie-series series)
        config (merge pie-chart-base-config
                      {:title {:text title}
                       :series [{:data data}]})]
    (when-not (s/valid? ::pie-series series)
      (s/explain ::pie-series series))
    [highchart config]))

;;Column Chart
(def column-chart-base-config
  {:chart {:type "column"}
   :title {:text "Product Profit"}
   :xAxis {:categories ["1º Ano" "2º Ano" "3º Ano" "4º Ano" "5º Ano"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Profit (millions)"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix "millions"}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   :credits {:enabled false}
   :series [{:name "Product 1800"
             :data [107 31 635 203 2]}
            {:name "Product 1900"
             :data [133 156 947 408 6]}
            {:name "Product 2000"
             :data [973 914 4054 732 34]}]})

#_
(s/def ::column-data (s/cat :name string? :y1 number?))
#_
(s/def ::column-series (s/coll-of ::column-data))
#_
(defn column-chart [title series]
  (let [data (s/conform ::column-series series)
        config (merge column-chart-base-config
                      {:title {:text title}
                       :series [{:data data}]})]
    (when-not (s/valid? ::column-series series)
      (s/explain ::column-series series))
    [highchart config]))

;; Basic Line
(def basic-line-chart-base-config
  {:title {:text "Receivable vs Payable"}
   :xAxis {:categories ["Jan" "Feb" "Mar" "Apr" "May"
                        "Jun" "Jul" "Aug" "Sep" "Oct"
                        "Nov" "Dec"]}
   :yAxis {:title {:text "Millions"}
           :plotLines [{:value 0
                        :width 1
                        :color "#808080"}]}
   :tooltip {:valueSuffix "ºC"}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "middle"
            :borderWidth 0}
   :series [{:name "Receivable"
             :data [7 6.9 9.5 14.5 18.2 21.5 25.2 26.5 23.3
                    18.3 13.9 9.6]}
            {:name "Payable"
             :data [0.2 2 5.7 11.3 17 22 24.8 24.1 20.1 14.1
                    8.6 2.5]}]})