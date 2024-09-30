[<img src="logo/borkweb.svg" alt="Borkweb" width="425px">](https://github.com/m3tti/borkweb)

Is a small web template with as little dependencies as possible to get you of the ground fast. It supports a structure for your next web app and integrates a simple user login and register form. Furthermore ClojureScript on your fingertips with squint and preact. You can write web components and plain Preact components. 
borkweb's goal is 'start small grow big' if babashka doesn't fit your needs switch to jvm clojure. Borkweb grows with you.

## Dependencies
- [ruuter](https://github.com/askonomm/ruuter)
- [squint](https://github.com/squint-cljs/squint)
- [babashka-sql-pod](https://github.com/babashka/babashka-sql-pods) <- depending on runtime
- [next.jdbc](https://github.com/seancorfield/next-jdbc) <- depending on runtime
- [http-kit](https://github.com/http-kit/http-kit)
- [cheshire](https://github.com/dakrone/cheshire)
- [ring](https://github.com/ring-clojure/ring)
- [honeysql](https://github.com/seancorfield/honeysql)
- [gaka](https://github.com/cdaddr/gaka)

## Get started
borkweb only needs babashka to get started. To run the template just do a `bb -m core` and you are good to go.
Make sure you have a postgres database at hand find documentation for that down below. If you need a postgres db there is a docker-compose.yaml there. Start it up as always `docker-compose up -d`.

### CLJS
borkweb provides already everything you need to get started with cljs no need for any bundler or anything else.
Get to `resources/cljs` drop your cljs code that is squint compliant and you are good to go. borkweb allready includes some examples for preact and preact web components. There are helper functions to compile cljs code in your hiccup templates. You can find them in `view/components.clj`
`cljs-module` to generate js module code and `cljs-resource` to create plain javascript code. there is even `->js` which can be used to trigger squint/cljs code inline.

``` clojure
;; resources/cljs/counter.cljs
(require '["https://esm.sh/preact@10.19.2" :as react])
(require '["https://esm.sh/preact@10.19.2/hooks" :as hooks])

(defn Counter []
  (let [[counter setCounter] (hooks/useState 0)]
    #jsx [:<>
          [:div "Counter " counter]
          [:div {:role "group" :class "btn-group"}
           [:button {:class "btn btn-primary" :onClick #(setCounter (inc counter))} "+"]
           [:button {:class "btn btn-primary" :onClick #(setCounter (dec counter))} "-"]]]))

(defonce el (js/document.getElementById "cljs"))

(react/render #jsx [Counter] el)
```

``` clojure
;; view/some_page.clj
(require '[view.components :as c])

(defn some-page [req]
  [:h1 "My fancy component page"]
  [:div#cljs]
  ;; just use the filename without cljs. the function will search in the resource/cljs folder.
  (c/cljs-module "counter"))
```

### Routing
Routing can be done in the `routes.clj` file found in the base folder. There are already premade helper functions to generate routes in a compojuresque style.

``` clojure
(route path method (fn [req] body))
(get path (fn [req] body))
(post path (fn [req] body))
```

### Database
The initialization of the database is currently done with an init.sql which you can trigger either with `bb -m database.core/initialize-db`.
Or by a tool of your choice. The database connection parameters are available in `database/core.cljs` just replace as you like.
Currently postgres is used for the database backend but you can basically use any sql database that you like and which is supported by your runtime. If you want to use a diffrent db like datalevin you'll lose the registration and login functionality and have to adjust some stuff.

tbd
