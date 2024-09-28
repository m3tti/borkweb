(ns middleware.auth
  (:require
   [utils.response :as r]
   [clojure.string :as str]
   [utils.session :as session]))

(def restricted-pages
  ["/profile"
   "/wurst"])

(defn path-restricted? [path]
  (some? (first (filter #(str/includes? path %) restricted-pages))))

(comment (path-restricted? "/prof"))

(defn wrap-auth [handler]
  (fn [req]
    (if (path-restricted? (:uri req))
      (if (session/current-user req)
        (handler req)
        (r/redirect (str "/login?url=" (:uri req))))
      (handler req))))
