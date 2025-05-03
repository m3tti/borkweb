(ns api.core
  (:require
   [cheshire.core :as json]))

;;
;; Apis can look like that. Make it your own!
;;
(defn ping [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body
   (json/encode {:hello "world"})})
