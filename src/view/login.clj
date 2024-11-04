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
   [:div.flex.justify-center
    [:div.w-90.w-50-m.w-25-l
     [:h1 "Login"]
     [:form {:method "post" :action "/login"}
      (c/csrf-token)
      (l/form-input
       :label "E-Mail"
       :type "email"
       :name "email"
       :placeholder "E-Mail")
      (l/form-input
       :label "Password"
       :type "password"
       :name "password"
       :placeholder "Password")
      
      [:input.input-reset.bg-moon-gray.near-black.bn.br2.pa2.w-100.ttu.b.hover-bg-mid-gray
       {:type "submit" :value "Login"}]]]]))
