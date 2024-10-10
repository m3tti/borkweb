(ns view.index
  (:require
   [view.components :as c]
   [database.user :as user]))

(defn page [req]
  (let [user (try
               (user/by-id
                (Integer.
                 (get-in req [:session :user-id] -1)))
               (catch Exception _ {}))]
    (c/layout
     req
     [:div.container
      [:h1 "My Service"]
      [:table     
       [:tr [:td "User"]
        [:td (get user
                  :users/email
                  "No user")]]]])))
