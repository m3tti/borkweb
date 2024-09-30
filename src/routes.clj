(ns routes
  (:require
   [ruuter.core :as ruuter]
   [static :as static]
   [view.index :as index]
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

(def routes
  #(ruuter/route 
    [(get "/static/:filename" static/serve-static)
     (get "/" index/page)
     (get "/register" register/index)
     (post "/register" register/save)
     (get "/login" login/index)
     (post "/login" login/login)
     (get "/logout" login/logout)
     (get "/profile" profile/index)]
    %))
