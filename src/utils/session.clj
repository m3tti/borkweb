(ns utils.session
  (:require
   [database.user :as user]))

(defn current-user [req]
  (user/by-id (get-in req [:session :user-id] -1)))
