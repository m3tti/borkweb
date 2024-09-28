[<img src="logo/borkweb.svg" alt="Borkweb" width="425px">](https://github.com/m3tti/borkweb)

Is a small web template with as little dependencies as possible to get you of the ground fast. It supports a structure for your next web app and integrates a simple user login and register form.

## Dependencies
- [ruuter](https://github.com/askonomm/ruuter)
- [squint](https://github.com/squint-cljs/squint)
- [babashka-sql-pod](https://github.com/babashka/babashka-sql-pods)
- [next.jdbc](https://github.com/seancorfield/next-jdbc)
- [http-kit](https://github.com/http-kit/http-kit)
- [cheshire](https://github.com/dakrone/cheshire)
- [ring](https://github.com/ring-clojure/ring)
- [honeysql](https://github.com/seancorfield/honeysql)
- [gaka](https://github.com/cdaddr/gaka)

## Get started
borkweb only needs babashka to get started. To run the template just do a `bb -m core` and you are good to go.

### CLJS
borkweb provides already everything you need to get started with cljs no need for any bundler or anything else.
Get to `resources/cljs` drop your cljs code that is squint compliant and you are good to go. borkweb allready includes some examples for preact and preact web components. There are helper functions to compile cljs code in your hiccup templates. You can find them in `view/components.clj`
`cljs-module` to generate js module code and `cljs-resource` to create plain javascript code. there is even `->js` which can be used to trigger squint/cljs code inline.

### Routing
Routing can be done in the `routes.clj` file found in the base folder. There are already premade helper functions to generate routes in a compojuresque style.

``` clojure
(route path method (fn [req] body))
(get path (fn [req] body))
(post path (fn [req] body))
```

tbd
