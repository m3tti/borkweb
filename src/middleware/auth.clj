(ns middleware.auth
  (:require
   [config :as config]
   [utils.response :as r]
   [clojure.string :as str]
   [utils.session :as session]))

(defn path-restricted? [path]
  (some? (first (filter #(str/includes? path %) config/restricted-pages))))

(comment (path-restricted? "/prof"))

(defn wrap-auth [handler]
  (fn [req]
    (if (path-restricted? (:uri req))
      (if (session/current-user req)
        (handler req)
        (r/redirect (str "/login?url=" (:uri req))))
      (handler req))))
