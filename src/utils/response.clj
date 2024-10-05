(ns utils.response)

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
