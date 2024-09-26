(ns view.profile
  (:require
   [view.components :as c]))

(defn index [req]
  {:status 200
   :body
   (c/layout
    req
    [:h1 "Profile"])})
