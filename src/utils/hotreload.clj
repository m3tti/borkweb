(ns utils.hotreload
  (:require
   [config :refer [hotreload?]]
   [taoensso.timbre :as log]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn last-modified
  []
  (let [lastModified-list (->>
                           (io/resource "cljs")
                           io/file
                           .listFiles
                           (map #(.lastModified %)))]
    (apply max lastModified-list)))

(defn modified?
  [last-timestamp]
  (not= (Long. last-timestamp) (last-modified)))

(defn has-changes
  ([req]
   (has-changes req 0))
  ([req loop-count]
   (let [last-timestamp (get-in req [:query-params "last-modified"])]
     (log/debug req)
     (cond
       (= 30 loop-count)
       {:status 200
        :body (str last-timestamp)}
       
       (modified? last-timestamp)
       {:status 200
        :body (str (last-modified))}

       :else
       (do
         (Thread/sleep 200)
         (recur req (inc loop-count)))))))

(defn hotreload
  [req]
  (if hotreload?
    (has-changes req)
    {:status 200
     :body "Disabled"}))
