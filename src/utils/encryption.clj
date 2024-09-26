(ns utils.encryption
  (:require [clojure.string :as str]))

(import java.security.SecureRandom)
(import javax.crypto.SecretKeyFactory)
(import javax.crypto.spec.PBEKeySpec)
(import java.security.MessageDigest)

(defn bytes->hex [byt]
  (apply str (map #(format "%02x" %) byt)))

(defn hex->bytes [hex]
  (.toByteArray (BigInteger. hex 16)))

(defn md5 [str]
  (bytes->hex (.digest (MessageDigest/getInstance "MD5") (.getBytes str "UTF-8"))))

(defn hash-password
  ([password]
   (let [salt (byte-array 4)]
     (.nextBytes (SecureRandom.) salt)
     (hash-password password salt)))

  ([password salt]
   (let [spec (PBEKeySpec. (char-array password) salt 65536 128)
         factory (SecretKeyFactory/getInstance "PBKDF2WithHmacSHA256")
         byte-data (.getEncoded (.generateSecret factory spec))]
     (str/join "$"
               [(bytes->hex byte-data)
                (bytes->hex salt)]))))

(defn get-salt [password-hash]
  (second (str/split password-hash #"\$")))

(defn password= [password-hash given-password]
  (let [salt (get-salt password-hash)]
    (= password-hash (hash-password given-password (hex->bytes salt)))))
