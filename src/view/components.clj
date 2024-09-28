(ns view.components
  (:require
   [hiccup2.core :as h]
   [utils.session :as s]
   [clojure.java.io :as io]
   [ring.middleware.anti-forgery :as af]
   [squint.compiler :as squint]
   [cheshire.core :as json]))

(def squint-cdn-path "https://cdn.jsdelivr.net/npm/squint-cljs@0.8.114")

(defn csrf-token []
  [:input {:type "hidden"
           :name "__anti-forgery-token"
           :value af/*anti-forgery-token*}])

(defn ->js [form]
  (->>
   (squint/compile-string* (str form))
   :body))

(defn compile-jsx [src]
  (squint/compile-string src {:jsx-runtime {:import-source "https://esm.sh/preact@10.19.2"}}))

(defn cljs-module [filename]
  [:script {:type "module"}
   (->
    (str "cljs/" filename ".cljs")
    io/resource
    slurp
    compile-jsx
    h/raw)])

(comment (cljs-module "custom-element"))

(defn cljs-resource [filename]
  [:script
   (->
    (str "cljs/" filename ".cljs")
    io/resource
    slurp
    ->js
    h/raw)])

(defn navbar [req]
  (let [user (s/current-user req)]
    [:nav.navbar.sticky-top.navbar-expand-lg.navbar-bg-body-tertiary
     [:div.container-fluid
      [:a.navbar-brand.fw-bold {:href "/"} "bork·web"]
      [:button.navbar-toggler {:type "button" :data-bs-toggle "collapse" :data-bs-target "#navbar"}
       [:span.navbar-toggler-icon]]
      [:div#navbar.collapse.navbar-collapse
       (when (not user)
         [:ul.navbar-nav
          [:li.nav-item
           [:a.nav-link {:href "/login"} "Login"]]
          [:li.nav-item
           [:a.nav-link {:href "/register"} "Register"]]])
       (when user
         [:ul.navbar-nav
          [:li.nav-item
           [:a.nav-link {:href "/profile"} "Profile"]]
          [:li.nav-item
           [:a.nav-link {:href "/logout"} "Logout"]]])]]]))

(defn alert [req]
  (let* [msg (get-in req [:flash :message])
         severity (:severity msg)
         msg (:message msg)]
    [:div.alert {:class (str "alert-" severity) :role "alert"}
     msg]))

(defn flash-msg [res severity msg]
  (assoc res :flash
         {:message {:severity severity
                    :message msg}}))

(defn layout [req & body]
  (str
   (h/html
       [:html
        [:head
         [:meta {:charset "utf-8"}]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1"}]
         [:link {:href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                 :rel "stylesheet"
                 :integrity "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                 :crossorigin "anonymous"}]
         [:script {:type "importmap"}
          (h/raw
           (json/encode 
            {:imports
             {:squint-cljs/core.js (str squint-cdn-path "/src/squint/core.js")
              :squint-cljs/string.js (str squint-cdn-path "/src/squint/string.js")
              :squint-cljs/src/squint/string.js (str squint-cdn-path "/src/squint/string.js")
              :squint-cljs/src/squint/set.js (str squint-cdn-path "/src/squint/set.js")
              :squint-cljs/src/squint/html.js (str squint-cdn-path "/src/squint/html.js")}}))]
         [:script {:src "https://unpkg.com/htmx.org@2.0.2"
                   :integrity "sha384-Y7hw+L/jvKeWIRRkqWYfPcvVxHzVzn5REgzbawhxAuQGwX1XWe70vji+VSeHOThJ"
                   :crossorigin "anonymous"}]
         (cljs-module "counter")
         (cljs-module "custom-element")]
        [:body {:hx-boost "true" :data-bs-theme "dark"}
         (navbar req)
         (alert req)
         body
         [:div.container
          [:h3.fw-bold "CLJS Components"]
          [:div#cljs.mb-3]
          [:x-greeting {:name "test"}]]        
         [:script {:src "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                   :integrity "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                   :crossorigin "anonymous"}]]])))
