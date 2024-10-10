(ns view.register
  (:require
   [database.user :as user]
   [view.layout :as l]
   [view.core :as c]
   [utils.response :as r]))

(defn save-user [req]
  (user/insert
   {:email (get-in req [:params "email"])
    :password (get-in req [:params "password1"])})
  (r/redirect "/"))

(defn save [req]
  (if (= (get-in req [:params "password1"])
         (get-in req [:params "password2"]))
    (save-user req)
    (r/flash-msg
     (r/redirect "/register")
     "danger" "Password don't match")))

;;
;; Register form. Adjust to your needs
;; 
(defn index [req]
  (l/layout
   req
   [:div.container.p-4
    [:div.row.justify-content-md-center
     [:div
      [:h1 "Register"]
      [:form {:method "post" :action "/register"}
       (c/csrf-token)
       [:div.mb-3 
        [:label.form-label "E-Mail"]
        [:input.form-control {:type "email" :name "email" :placeholder "E-Mail"}]]
       [:div.mb-3 
        [:label.form-label "Password"]
        [:input.form-control {:type "password" :name "password1"}]]
       [:div.mb-4
        [:label.form-label "Password again"]
        [:input.form-control {:type "password" :name "password2"}]]
       [:div.mb-3
        [:input.btn.btn-primary {:type "submit" :value "Register"}]]]]]]))
