(ns database.user
  (:require
   [database.core :as db]
   [utils.encryption :as enc]))

(defn by-id [id]
  (first (db/find-by-keys :users {:id id})))

(defn by-email [email]
  (first (db/find-by-keys :users {:email email})))

(defn all []
  (db/execute! {:select :* :from :users}))

(defn delete! [id]
  (db/delete! :users {:id id}))

(defn insert [{:keys [email password]}]
  (db/insert! :users
              {:email email
               :password (enc/hash-password password)}))

(defn correct-password? [email password]
  (let [user (by-email email)]
    (if user 
      (enc/password= (:users/password user) password)
      false)))
