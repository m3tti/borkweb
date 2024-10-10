(defn on-activate [ev]
  (js/console.log "Send claim")
  (.waitUntil ev (-> self .-clients .claim)))

(defn on-fetch [ev]
  (.respondWith ev (js/fetch (.-request ev)) ))

(defn on-install [ev]
  (js/console.log "Install"))

(.addEventListener self "activate" on-activate)
(.addEventListener self "fetch" on-fetch)
(.addEventListener self "install" on-install)
