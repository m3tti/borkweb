(ns utils.session
  (:require
   [database.user :as user]))

(defn current-user 
  "Get the current user id from the session and return the user object from the database"
  [req]
  (try
    (user/by-id (get-in req [:session :user-id] -1))
    (catch Exception _ nil)))
