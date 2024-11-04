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
   [:div.flex.justify-center
    [:div.w-90.w-50-m.w-25-l
     [:h1 "Register"]
     [:form {:method "post" :action "/register"}
      (c/csrf-token)
      (map l/form-input
           [{:label "E-Mail"
             :type "email"
             :name "email"
             :placeholder "E-Mail"}
            {:label "Password"
             :type "password"
             :name "password1"
             :placeholder "Password"}
            {:label "Password Again"
             :type "password"
             :name "password2"
             :placeholder "Password Again"}])
      [:input.input-reset.bg-moon-gray.near-black.bn.br2.pa2.w-100.ttu.b.hover-bg-mid-gray {:type "submit" :value "Register"}]]]]))
