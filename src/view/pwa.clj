(ns view.pwa
  (:require
   [view.core :as c]
   [cheshire.core :as json]))

(defn sw [req]
  {:status 200
   :headers {"Content-Type" "application/javascript"}
   :body (c/cljs->inline "sw")})

(defn manifest [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/encode
          {:name "borkweb"
           :short_name "borkweb"
           :theme_color "#2b3035"
           :background_color "#2b3035"
           :start_url "./"
           :scope "./"
           :display "standalone"
           :prefer_related_applications false
           :icons
           [{:src "https://raw.githubusercontent.com/m3tti/borkweb/refs/heads/master/logo/borkweb.png"
             :type "image/png"
             :sizes "512x512"}]})})
