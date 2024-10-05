(ns view.style
  (:require
   [gaka.core :as gaka]))

;;
;; The CSS of your app. This is included in the `view/components.clj` 
;;
(def *style*
  (gaka/css
   [:.title
    :margin "20px"]))
