(ns view.layout
  (:require
   [config :refer [hotreload?]]
   [cheshire.core :as json]
   [hiccup2.core :as h]
   [utils.session :as s]
   [utils.htmc :as hc]
   [view.style :as sty]
   [view.core :as c]))


(def squint-cdn-path "https://cdn.jsdelivr.net/npm/squint-cljs@0.8.114")

(defn autocomplete-input [& {:keys [label name value list required]}]
  [:div.mb-3
   [:label.form-label label]
   [:input.form-control {:type "input" :list (str name "list")
            :name name :value value :required required
            :autocomplete "off"}]
   [:datalist {:id (str name "list")}
    (map (fn [e] [:option {:value e}]) list)]])

(defn form-input [& {:keys [label type name value placeholder required]
                     :as opts
                     :or {required false}}]
  (cond
    (= type "textarea")
    [:div.mb3
     [:label.db label]
     [:textarea.w-100.pa2.input-reset.ba.br1.b--moon-gray.bg-mid-gray.white.bn
      {:type type :name name :required required} value]]

    (= type "autocomplete")
    (autocomplete-input opts)
    
    :else
    [:div.mb3
     [:label.db label]
     [:input.w-100.pa2.input-reset.ba.br1.b--moon-gray.bg-mid-gray.white.bn
      {:type type :value value :name name :required required :placeholder placeholder}]]))

;;
;; Extend importmap. This enables you to load other libraries in your
;; js files. The key is the libraries name in your app if you require it
;;
(defn global-importmap []
  [:script {:type "importmap"}
   (h/raw
    (json/encode 
     {:imports
      {:squint-cljs/core.js (str squint-cdn-path "/src/squint/core.js")
       :squint-cljs/string.js (str squint-cdn-path "/src/squint/string.js")
       :squint-cljs/src/squint/string.js (str squint-cdn-path "/src/squint/string.js")
       :squint-cljs/src/squint/set.js (str squint-cdn-path "/src/squint/set.js")
       :squint-cljs/src/squint/html.js (str squint-cdn-path "/src/squint/html.js")}}))])

(defn navbar [req]
  (let [user (s/current-user req)]
    [:nav.fixed.w-100.top-0.bg-dark-gray
     [:div.flex.items-center.justify-between.pa2
      [:a.link.moon-gray.b.f3 {:href "/"} "bork·web"]
      [:div
       (when (not user)
         [:div
          [:a.link.moon-gray.ph2.hover-gray {:href "/login"} "Login"]          
          [:a.link.moon-gray.ph2.hover-gray {:href "/register"} "Register"]
          [:a.link.moon-gray.ph2.hover-gray {:href "/kitchensink"} "Kitchensink"]])
       (when user
         [:div
          [:a.link.moon-gray.ph2.hover-gray {:href "/profile"} "Profile"]
          [:a.link.moon-gray.ph2.hover-gray {:href "/logout"} "Logout"]])]]]))

(defn alert [req]
  (let* [msg (get-in req [:flash :message])
         severity (:severity msg)
         msg (:message msg)]
    (when msg
      [:div {:class (str "bg-dark-red pa2 o-70 near-black b danger br2 mh2 " severity) :role "alert"}
       msg])))

(defn layout [req & body]
  (str
   (h/html
       [:html
        [:head
         [:meta {:charset "utf-8"}]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1"}]
         [:link {:rel "manifest" :href "/manifest.json"}]
         [:link {:href "https://unpkg.com/tachyons@4.12.0/css/tachyons.min.css"
                 :rel "stylesheet"}]
         (global-importmap)
         (c/cljs-module "register-sw")
         (when hotreload?
           (c/cljs-module "hotreload"))
         [:style (h/raw sty/*style*)]]
        [:body.helvetica.bg-near-black.moon-gray {:id "body"}
         (hc/htmc)
         (navbar req)
         [:div.mt5
          (alert req)
          body]]])))
