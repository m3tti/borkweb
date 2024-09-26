(ns core
  (:require
   [routes :as ro]
   [middleware.auth :as auth]
   [ring.middleware.anti-forgery :as af]
   [ring.middleware.session :as s]
   [ring.middleware.params :as p]
   [ring.middleware.flash :as f]
   [org.httpkit.server :as srv]))

(def server (atom nil))

(defn -main [& args]
  (reset! server
          (srv/run-server
           (->
            #'ro/routes
            auth/wrap-auth
            (af/wrap-anti-forgery {:anti-forgery true
                                   :token-expiry (* 60 60 24)})
            f/wrap-flash
            s/wrap-session
            p/wrap-params)
           {:port 8080
            :join? false})))

(comment (-main))
(comment (@server))
