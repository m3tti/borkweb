(ns view.layout
  (:require
   [config :refer [hotreload?]]
   [cheshire.core :as json]
   [hiccup2.core :as h]
   [utils.session :as s]
   [utils.response :as r]
   [utils.htmc :as hc]
   [view.style :as sty]
   [view.core :as c]))


(def squint-cdn-path "https://cdn.jsdelivr.net/npm/squint-cljs@0.8.114")

(defn paginator [req current-page pages base-url]
  (let* [q (:query-params req)
         next (when (not= current-page pages)
                (str base-url "?"
                     (r/query-params->url
                      (merge q {"page" (+ current-page 1)}))))
         previous (when (not= current-page 1)
                    (str base-url "?"
                         (r/query-params->url
                          (merge q {"page" (- current-page 1)}))))]
    [:div.d-flex.justify-content-center.mb-2
     [:div.btn-group
      [:a.btn.btn-primary
       (if (nil? previous)
         {:disabled true}
         {:href previous}) "Previous"]
      [:a.btn.btn-outline-primary {:href "#"} current-page " / " pages ]
      [:a.btn.btn-primary
       (if (nil? next)
         {:disabled true}
         {:href next}) "Next"]]]))

(defn autocomplete-input [& {:keys [label name value list required]}]
  [:div.mb-3
   [:label.form-label label]
   [:input.form-control {:type "input" :list (str name "list")
            :name name :value value :required required
            :autocomplete "off"}]
   [:datalist {:id (str name "list")}
    (map (fn [e] [:option {:value e}]) list)]])

(defn form-input [& {:keys [id label type name value required]
                     :as opts
                     :or {required false}}]
  (cond
    (= type "textarea")
    [:div.mb-3
     [:label.form-label label]
     [:textarea.form-control {:type type :name name :required required} value]]

    (= type "autocomplete")
    (autocomplete-input opts)

    (= type "base64-upload")
    [:div.mb-3
     [:label.form-label label]
     (c/cljs-module "base64-upload")
     [:input.form-control {:type "file" :required required :onchange (str "base64_upload(\"" id "\", this)")}]
     [:input {:type "hidden" :name name :id (if id id label)}]]

    :else
    [:div.mb-3
     [:label.form-label label]
     [:input.form-control {:type type :value value :name name :required required}]]))

;;
;; Extend importmap. This enables you to load other libraries in your
;; js files. The key is the libraries name in your app if you require it
;;
(defn global-importmap []
  [:script {:type "importmap"}
   (h/raw
    (json/encode
     {:imports
      {"squint-cljs/core.js" (str squint-cdn-path "/src/squint/core.js")
       "squint-cljs/string.js" (str squint-cdn-path "/src/squint/string.js")
       "squint-cljs/src/squint/string.js" (str squint-cdn-path "/src/squint/string.js")
       "squint-cljs/src/squint/set.js" (str squint-cdn-path "/src/squint/set.js")
       "squint-cljs/src/squint/html.js" (str squint-cdn-path "/src/squint/html.js")}}))])

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
           [:a.nav-link {:href "/register"} "Register"]]
          [:li.nav-item
           [:a.nav-link {:href "/kitchensink"} "Kitchensink"]]])
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

(defn layout [req & body]
  (str
   (h/html
       [:html
        [:head
         [:meta {:charset "utf-8"}]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1"}]
         [:link {:rel "manifest" :href "/manifest.json"}]
         [:link {:href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                 :rel "stylesheet"
                 :integrity "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                 :crossorigin "anonymous"}]
         (global-importmap)
         (c/cljs-module "register-sw")
         (when hotreload?
           (c/cljs-module "hotreload"))
         [:style (h/raw sty/*style*)]]
        [:body {:data-bs-theme "dark" :id "body"}
         (hc/htmc)
         (navbar req)
         (alert req)
         body
         [:script {:src "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                   :integrity "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                   :crossorigin "anonymous"}]]])))

(defn modal [& {:keys [id title content actions]}]
  [:div.modal.fade {:tabindex -1 :id id}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header
      [:h5.modal-title title]
      [:button.btn-close {:type "button" :data-bs-dismiss "modal"}]]
     [:div.modal-body content]
     [:div.modal-footer actions]]]])
