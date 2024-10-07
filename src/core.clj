(ns core
  (:require
   [routes :as ro]
   [middleware.auth :as auth]
   [ring.middleware.anti-forgery :as af]
   [ring.middleware.session :as s]
   [ring.middleware.params :as p]
   [ring.middleware.flash :as f]
   [taoensso.timbre :as log]
   [taoensso.timbre.appenders.core :as appenders]
   [org.httpkit.server :as srv]))

(def server (atom nil))

(log/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "./application.log"})}})

(defn start-server []
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

(defn -main [& args]
  (start-server)
  (println "You will be assimilated @ https://localhost:8080")
  @(promise))

;;
;; Repl functions. To startup and stop the system
;;
(comment (start-server))
(comment (@server))
