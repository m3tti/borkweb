(ns view.profile
  (:require
   [view.components :as c]))

(defn index [req]
  (c/layout
   req
   [:h1 "Profile"]))
