(ns routes
  (:require
   [ruuter.core :as ruuter]
   [static :as static]
   [utils.hotreload :as hotreload]
   [utils.session :as session]
   [utils.response :as r]
   [view.pwa :as pwa]
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

(defn restricted [response-fn]
  (fn [req]
    (if (session/current-user req)
      (response-fn req)
      (r/redirect (str "/login?url=" (:uri req))))))

;;
;; Extend your routes in here!!!
;;
(def routes
  #(ruuter/route 
    [(get "/manifest.json" pwa/manifest)
     (get "/sw.js" pwa/sw)
     (get "/static/:filename" static/serve-static)
     (get "/" index/page)
     (get "/kitchensink" sink/index)
     (get "/register" register/index)
     (post "/register" register/save)
     (get "/login" login/index)
     (post "/login" login/login)
     (get "/logout" login/logout)
     (get "/profile" profile/index)
     (get "/hotreload" hotreload/hotreload)]
    %))
