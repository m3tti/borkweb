(ns utils.encode
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