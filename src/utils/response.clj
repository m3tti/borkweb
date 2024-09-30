(ns utils.response)

(defn redirect [path]
  {:status 303
   :headers {"Location" path}})

(defn flash-msg [res severity msg]
  (assoc res :flash
         {:message {:severity severity
                    :message msg}}))
