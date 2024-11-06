(ns utils.crud
  (:require
   [taoensso.timbre :as log]
   [utils.response :as r]
   [utils.error :as e]))

(defn delete!
  [& {:keys [req delete-fn redirect-path]}]
  (try
    (delete-fn (Integer. (get-in req [:params "id"])))
    (r/redirect redirect-path)
    (catch Exception e
      (log/error e)
      (r/flash-msg (r/redirect redirect-path)
                   "danger" "Couldn't delete!"))))

(defn update!
  [& {:keys [req update-fn insert-fn normalized-data
             redirect-path does-already-exist?]
      :or {does-already-exist? false}}]
  (try
    (if (get-in req [:params "id"])
      (update-fn normalized-data)
      (if does-already-exist?
        (throw (Exception. "Item already exists!"))
        (insert-fn normalized-data)))
    (r/redirect redirect-path)
    (catch Exception e
      (log/error e)
      (r/flash-msg (r/redirect redirect-path)
                   "danger" (e/stacktrace->str e)))))
