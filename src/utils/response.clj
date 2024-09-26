(ns utils.response)

(defn redirect [path]
  {:status 303
   :headers {"Location" path}})
