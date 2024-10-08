(ns routes
  (:require
   [ruuter.core :as ruuter]
   [static :as static]
   [view.index :as index]
   [view.kitchensink :as sink]
   [view.login :as login]
   [view.profile :as profile]
   [view.register :as register]))

(defn route [path method response-fn]
  {:path path
   :method method
   :response (fn [req]
               (let [resp (response-fn req)]
                 (if (string? resp)
                   {:status 200
                    :body resp}
                   resp)))})

(defn get [path response-fn]
  (route path :get response-fn))

(defn post [path response-fn]
  (route path :post response-fn))

(defn put [path response-fn]
  (route path :put response-fn))

(defn delete [path response-fn]
  (route path :delete response-fn))

(defn option [path response-fn]
  (route path :option response-fn))

(defn pwa [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/encode
          {:name "Borkweb"
           :icons
           [{:src "/static/img/icon.png"
             :type "image/png"
             :sizes "512x512"}]})})

;;
;; Extend your routes in here!!!
;;
(def routes
  #(ruuter/route 
    [(get "/manifest.json" pwa)
     (get "/static/:filename" static/serve-static)
     (get "/" index/page)
     (get "/kitchensink" sink/index)
     (get "/register" register/index)
     (post "/register" register/save)
     (get "/login" login/index)
     (post "/login" login/login)
     (get "/logout" login/logout)
     (get "/profile" profile/index)]
    %))
