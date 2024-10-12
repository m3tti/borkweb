(ns utils.htmc
  (:require
   [view.core :as c]))

;; kudos to https://kalabasa.github.io/htmz/
(defn htmc
  "Has to be a function due to the hotreload of the htmc.cljs code. If you want to extend it."
  []
  [:div 
   (c/cljs-module "htmc")
   [:iframe {:id "htmc" :hidden true :name "htmc"}]])
