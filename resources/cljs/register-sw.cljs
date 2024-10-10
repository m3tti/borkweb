(when (get js/navigator "serviceWorker")
  (.addEventListener
   js/self "load"
   (^:async fn []
     (let [container (.-serviceWorker js/navigator)]
       (if (nil? (.-controller container))
         (do
           (js/console.log "Try to register new sw")
           (js-await (.register container "sw.js"))
           (js/console.log "SW installed"))
         (js/console.log "SW already registered"))))))
