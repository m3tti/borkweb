(ns api.main
  (:require
   [config :as config]
   [clojure.test :refer [deftest is use-fixtures run-tests]]
   [org.httpkit.client :as http]
   [cheshire.core :as json]))

(defn request-json
  ([uri]
   (request-json :get uri nil))

  ([method uri]
   (request-json method uri nil))

  ([method uri options]
   (let [resp @(http/request (merge {:method method
                                     :url uri}
                                    options))]
     (when (:body resp)
       (merge resp
              {:json (json/parse-string (:body resp))})))))

(deftest ping-endpoint
  (let [resp (request-json :get (str config/base-url "/api/ping"))]
    (println (:json resp))
    (is (= (:json resp) {"hello" "world"}))))
