(ns view.core
  (:require
   [hiccup2.core :as h]
   [clojure.java.io :as io]
   [ring.middleware.anti-forgery :as af]
   [squint.compiler :as squint]))

(defn csrf-token []
  [:input {:type "hidden"
           :name "__anti-forgery-token"
           :value af/*anti-forgery-token*}])

;;
;; Helper functions to interact with the squint compile
;;
(defn ->js [form]
  (->>
   (squint/compile-string* (str form))
   :body))

(defn compile-jsx [src]
  (squint/compile-string src {:jsx-runtime {:import-source "https://esm.sh/preact@10.19.2"}}))

(defn cljs-module [filename]
  (let [full-filename (str "cljs/" filename ".cljs")]
    [:script {:type "module"}
     (->
      full-filename
      io/resource
      slurp
      compile-jsx
      (str "// corresponding cljs: /static/" full-filename)
      h/raw)]))

(defn cljs-resource [filename]
  (let [full-filename (str "cljs/" filename ".cljs")]
    [:script
     (->
      full-filename
      io/resource
      slurp
      ->js
      (str "// corresponding cljs: /static/" full-filename)
      h/raw)]))

(defn cljs->inline [filename]
  (->
   (str "cljs/" filename ".cljs")
   io/resource
   slurp
   ->js))
