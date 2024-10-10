(ns view.profile
  (:require
   [view.layout :as l]))

(defn index [req]
  (l/layout
   req
   [:h1 "Profile"]))
