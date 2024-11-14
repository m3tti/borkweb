(ns utils.encode
  (:require
   [clojure.java.io :as io])
  (:import 
   (java.util Base64)))

(defn b64-encode
  "Needs bytes to encode to base64"
  [payload-bytes]
  (.encodeToString (Base64/getEncoder) payload-bytes))

(defn b64-decode 
  "Returns the byte representation of the base64 string"
  [b64]
  (.decode (Base64/getDecoder) b64))

(comment 
  (->
   "Hello World"
   (.getBytes "UTF-8")
   b64-encode
   b64-decode
   String.))

(defn decode->file
  "Decode base64 as file"
  [b64 filename]
  (let [file (io/file filename)]
    (io/copy
     (b64-decode b64)
     file)
    file))
