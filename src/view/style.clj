(ns view.style
  (:require
   [gaka.core :as gaka]))

(def *style*
  (gaka/css
   [:.title
    :margin "20px"]))
