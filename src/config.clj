(ns config)

(def hotreload? true)

;; Postgress
;(def db-opts
;  {:dbtype "postgres"
;   :dbname "testdb"
;   :user "postgres"
;   :password "test1234"
;   :port 15432})

;; Hsql
(def db-opts
  {:dbtype "hsqldb"
   :dbname "./staryou"
   ;; set postgres dialect
   :sql.syntax_pgs true})

(def base-url "http://localhost:8080")
