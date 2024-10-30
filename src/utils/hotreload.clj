(ns utils.hotreload
  (:require
   [clojure.java.io :as io]
   [clojure.core.async :as async]
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

(defn hotreload
  [req]
  (while (not (modified? (get-in req [:query-params "last-modified"])))
    (Thread/sleep 200))
  {:status 200
   :body (str (last-modified))})
