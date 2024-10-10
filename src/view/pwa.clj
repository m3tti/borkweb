(ns view.pwa
  (:require
   [view.components :as c]
   [cheshire.core :as json]))

(defn sw [req]
  {:status 200
   :body (c/cljs->inline "sw")})

(defn manifest [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/encode
          {:name "StarYou"
           :display "standalone"
           :prefer_related_applications false
           :icons
           [{:src "/static/img/icon.png"
             :type "image/png"
             :sizes "512x512"}]})})
