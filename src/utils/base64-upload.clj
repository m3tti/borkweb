(ns utils.base64-upload
  (:require 
   [utils.encode :as encode]
   [clojure.string :as str]))

(defn get-file 
  [req id path]
  (let* [file-data (str/split (get-in req [:params id]) #";")
         filename (first file-data)
         b64 (second file-data)]
    {:name filename
     :ext (last (str/split filename #"\."))
     :file (encode/decode->file b64 (str path filename))}))
