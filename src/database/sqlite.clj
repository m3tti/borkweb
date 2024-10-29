(ns database.sqlite
  (:require
   [clojure.string :as str]
   [pod.babashka.go-sqlite3 :as sqlite]))

(defn get-connection
  [db-opts]
  (str (:dbname db-opts)))

(defn execute!
  [db sql]
  (if (str/includes? (str/lower-case sql) "select")
    (sqlite/query db sql)
    (sqlite/execute! db sql)))

(defn execute-one!
  [db sql]
  (->
   (execute! db sql)
   first))

(comment
  (sqlite/execute! "/tmp/test.sqlite" ["create table test(name varchar(255));"])
  (execute! "/tmp/test.sqlite" ["insert into test(name) values ('hanswurs2t')"])
  (execute! "/tmp/test.sqlite" ["select * from test"]))
