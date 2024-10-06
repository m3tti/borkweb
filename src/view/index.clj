(ns view.index
  (:require
   [view.components :as c]
   [database.core :as db]
   [database.user :as user]))

(defn page [req]
  (c/layout
   req
   [:div.container
    [:h1 "My Service"]
    [:table     
     [:tr [:td "User"] [:td (get (user/by-id
                                  (Integer.
                                   (get-in req [:session :user-id] -1)))
                                 :users/email
                                 "No user")]]]]))
