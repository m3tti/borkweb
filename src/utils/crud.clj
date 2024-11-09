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

(defn create!
  [& {:keys [req create-fn normalized-data
             redirect-path does-already-exist?]
      :or {does-already-exist? false}}]
  (try 
    (if does-already-exist?
      (throw (Exception. "Item already exists!"))
      (do
        (create-fn normalized-data)
        (r/redirect redirect-path)))
    (catch Exception e
      (log/error e)
      (r/flash-msg (r/redirect redirect-path)
                   "danger" (e/stacktrace->str e)))))

(defn update!
  [& {:keys [req update-fn normalized-data
             redirect-path]}]
  (try
    (if (get-in req [:params "id"])
      (do 
        (update-fn normalized-data)
        (r/redirect redirect-path))
      (throw (Exception. "Item is new could not update!")))
    (catch Exception e
      (log/error e)
      (r/flash-msg (r/redirect redirect-path)
                   "danger" (e/stacktrace->str e)))))

(defn upsert!
  [& {:keys [req create-fn update-fn normalized-data
             redirect-path does-already-exist?]
      :or {does-already-exist? false}
      :as opts}]
  (if (get-in req [:params "id"])
    (update! opts)
    (create! opts)))
