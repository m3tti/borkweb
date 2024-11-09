(ns utils.error)

(defn stacktrace->str [t]
  (.getMessage t))
