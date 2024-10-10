(ns view.kitchensink
  (:require
   [view.components :as c]))

;;
;; Here is the example page of all the included components
;; for your webapp. Like FileDropAreas, Autocompletes or Modals
;;
(defn index [req]
  (c/layout
   req
   [:div
    [:div.container.row
     [:div.col
      [:h3.fw-bold "Preact Components"]
      [:div#cljs.mb-3]]
     [:div.col
      [:h3.fw-bold "Web Component"]
      [:x-greeting {:name "test"}]]
     [:div.col
      [:h3.fw-bold "Drag File"]
      [:div#upload]]]
    (c/cljs-module "counter")
    (c/cljs-module "custom-element")
    (c/cljs-module "drop-area")]))
