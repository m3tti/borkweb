(ns view.kitchensink
  (:require
   [view.core :as c]
   [view.layout :as l]))

;;
;; Here is the example page of all the included components
;; for your webapp. Like FileDropAreas, Autocompletes or Modals
;;
(defn index [req]
  (l/layout
   req
   [:div
    [:div.container.row
     [:div.col
      [:h3.fw-bold "Preact Components"]
      [:div#cljs.mb-3]]
     [:div.col
      [:h3.fw-bold "Web Component"]
      [:x-greeting {:name "test"}]]]
    (c/cljs-module "counter")
    (c/cljs-module "custom-element")]))
