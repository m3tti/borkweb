(ns utils.database
  (:require
   [taoensso.timbre :as log])
  (:import
   (java.time LocalDate LocalDateTime)
   (java.time.format DateTimeFormatter)))

(defn remove-csrf [data]
  (dissoc data "__anti-forgery-token"))

(defn remove-empty-vals [values]
  (apply dissoc values (keys (filter (fn [[k v]] (= v "")) values))))

(defn parse-date [kv-map key]
  (let [val (get kv-map key)]
    (if (empty? val)
      (dissoc kv-map key)
      (assoc kv-map key (->
                         val
                         LocalDate/parse
                         .atStartOfDay)))))

(defn parse-float [kv-map key]
  (let [val (get kv-map key)]
    (if val
      (assoc kv-map key (Float. val))
      (dissoc kv-map key))))

(defn parse-int [kv-map key]
  (let [val (get kv-map key)]
    (if val
      (assoc kv-map key (Integer. val))
      (dissoc kv-map key))))

(defn ->date-string [date]
  (try 
    (.format (LocalDateTime/parse
              (str date)
              (DateTimeFormatter/ofPattern "EEE MMM dd HH:mm:ss zzz yyyy"))
             (DateTimeFormatter/ofPattern "yyyy-MM-dd"))
    (catch Exception e
      (log/error e)
      nil)))
