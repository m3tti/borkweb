(ns utils.response
  (:require
   [clojure.string :as str]))

(defn redirect
  "Creates redirection response for a given url"
  [path]
  {:status 303
   :headers {"Location" path}})

(defn flash-msg
  "Adds a flash message to the current session"
  [res severity msg]
  (assoc res :flash
         {:message {:severity severity
                    :message msg}}))

(defn query-params->url [params]
  (str/join "&"
            (map (fn [[k v]] (str k "=" v)) params)))
