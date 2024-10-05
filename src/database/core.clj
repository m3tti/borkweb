(ns database.core
  (:require
   [utils.runtime :as runtime]
   [honey.sql :as sql]))

(runtime/if-bb
 (require '[pod.babashka.postgresql :as jdbc])
 (require '[next.jdbc :as jdbc]))

(defonce db (jdbc/get-connection {:dbtype "postgres"
                                  :dbname "staryou"
                                  :user "postgres"
                                  :password "test1234"
                                  :port 15432}))

(defn execute!
  ([sql]
   (execute! db sql))
  ([tx sql]
   (jdbc/execute! tx (sql/format sql))))

(defn execute-one!
  ([sql]
   (execute-one! db sql))
  ([tx sql]
   (jdbc/execute-one! tx (sql/format sql))))

(defn insert!
  ([table key-map]
   (insert! db table key-map))
  ([tx table key-map]
   (execute-one! tx {:insert-into [table]
                     :values (if (seq? key-map)
                               key-map
                               [key-map])})))

(defn where-eq [key-map]
  (concat [:and] 
          (mapv (fn [[k v]] [:= k v]) key-map)))

(defn delete!
  ([table where-params]
   (delete! db table where-params))
  ([tx table where-params]
   (execute! tx {:delete-from [table]
                 :where (where-eq where-params)})))

(defn update!
  ([table key-map where-params]
   (update! db table key-map where-params))
  ([tx table key-map where-params]
   (execute! tx {:update table
                 :set key-map
                 :where (where-eq where-params)})))

(defn find-by-keys
  ([table key-map]
   (find-by-keys db table key-map))
  ([tx table key-map]
   (execute! tx {:select :*
                 :from table
                 :where (where-eq key-map)})))

;;
;; The initialization function for your database system.
;; You can even directly call the commented function to setup
;; your system in development.
;;
(defn initialize-db []
  (jdbc/execute-one! db [(slurp "init.sql")]))

(comment (initialize-db))

(defn version []
  (jdbc/execute-one! db ["select version()"]))
