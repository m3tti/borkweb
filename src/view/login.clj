(ns view.login
  (:require
   [utils.response :as r]
   [view.core :as c]
   [view.layout :as l]
   [database.user :as user]))

(defn logout [req]
  (assoc (r/redirect "/")
         :session nil))

(defn login [req]
  (let [email (get-in req [:params "email"])]
    (if (user/correct-password?
         email
         (get-in req [:params "password"]))
      (assoc (r/redirect "/")
             :session {:user-id (:users/id (user/by-email email))})
      (r/flash-msg (r/redirect "/login") "danger" "Wrong username or password"))))

;;
;; Login form. Adjust to your needs
;;
(defn index [req]
  (l/layout
   req
   [:div.container.p-4
    [:div.row.justify-content-md-center
     [:div
      [:h1 "Login"]
      [:form {:method "post" :action "/login"}
       (c/csrf-token)
       [:div.mb-3 
        [:label.form-label "E-Mail"]
        [:input.form-control {:type "email" :name "email" :placeholder "E-Mail"}]]
       [:div.mb-3 
        [:label.form-label "Password"]
        [:input.form-control {:type "password" :name "password"}]]
       [:div.mb-3
        [:input.btn.btn-primary {:type "submit" :value "Login"}]]]]]]))
