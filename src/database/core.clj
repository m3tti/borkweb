(ns database.core
  (:require
   [utils.runtime :as runtime]
   [clojure.string :as str]
   [clojure.edn :as edn]
   [honey.sql :as sql])
  (:import
   (java.util Locale)))

(runtime/if-bb
 ;; if you want postgres
 ;;(require '[pod.babashka.postgresql :as jdbc])
 (require '[pod.babashka.hsqldb :as jdbc])
 (require '[next.jdbc :as jdbc]))

;; Postgress
;;(def db-opts
;;  {:dbtype "postgres"
;;   :dbname "jobstop"
;;   :user "postgres"
;;   :password "test1234"
;;   :port 15432}

;; Hsql
(def db-opts
  {:dbtype "hsqldb"
   :dbname "./changeme"
   ;; set postgres dialect
   :sql.syntax_pgs true})

(defonce db
  (jdbc/get-connection db-opts))

(defn to-lower-case-keys [arr]
  (into {}
        (for [[k v] arr]
          (hash-map
           (keyword (str/lower-case (str (namespace k) "/" (name k))))
           v))))

(defn execute!
  ([sql]
   (execute! db sql))
  ([tx sql]
   (->> (jdbc/execute! tx (sql/format sql))
       (map to-lower-case-keys))))

(defn execute-one!
  ([sql]
   (execute-one! db sql))
  ([tx sql]
   (-> (jdbc/execute-one! tx (sql/format sql))
       to-lower-case-keys)))

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
  (jdbc/with-transaction [tx db]
    (for [q (edn/read-string (slurp "initsql.edn"))]
      (jdbc/execute-one! tx (sql/format q)))))

(comment (initialize-db))
