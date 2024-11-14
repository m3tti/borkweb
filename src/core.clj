(ns core
  (:require
   [babashka.cli :as cli]
   [routes :as ro]
   [ring.middleware.anti-forgery :as af]
   [ring.middleware.session :as s]
   [ring.middleware.params :as p]
   [ring.middleware.flash :as f]
   [taoensso.timbre :as log]
   [taoensso.timbre.appenders.core :as appenders]
   [org.httpkit.server :as srv]))

(def server (atom nil))

(def cli-options-spec {:help {:alias :h}
                       :port {:coerce :int
                              :default 8080
                              :alias :p}})

(defn show-help []
  (println (cli/format-opts {:spec cli-options-spec})))

(log/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "./application.log"})}})

(defn start-server [port]
  (log/info "Server starting up!")
  (reset! server
          (srv/run-server
           (->
            #'ro/routes
            (af/wrap-anti-forgery {:anti-forgery true
                                   :token-expiry (* 60 60 24)})
            f/wrap-flash
            s/wrap-session
            p/wrap-params)
           {:port port
            :join? false})))

(defn -main [& args]
  (let [cli-options (cli/parse-opts args {:spec cli-options-spec})
        {:keys [port help]} cli-options]
    (if help
      (show-help)
      (do (start-server port)
          (println (str "Happy coding @ http://localhost:" port))
          @(promise)))))

;;
;; Repl functions. To startup and stop the system
;;
(comment (start-server 8080))
(comment (@server))
