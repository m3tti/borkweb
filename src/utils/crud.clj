(ns utils.crud
  (:require
   [taoensso.timbre :as log]
   [view.core :as c]
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

(defn- table-row
  [edit-path-fn actions-fn element]
  [:tr
   (concat (map #(vec [:td [:a {:href (edit-path-fn element)} %]]) (vals element))
           [[:td (actions-fn element)]])])

(defn table-view
  [& {:keys [new-path edit-path-fn actions-fn elements]}]
  [:div.container
   [:div.d-flex.justify-content-between
    [:h1 "Posts"]
    [:a.btn.btn-primary {:href new-path} "New"]]
   [:table.table
    [:thead
     (concat (map #(vec [:th (name %)]) (-> elements first keys))
             [[:th "Actions"]])]
    [:tbody
     (map #(table-row edit-path-fn actions-fn %) elements)]]])

(defn create-update-form
  [& {:keys [save-path form-inputs]}]
  [:form {:action save-path :method "post"}
   (c/csrf-token)
   form-inputs
   [:div.mt-3.d-grid
    [:input.btn.btn-primary {:type "submit" :value "Save"}]]])
