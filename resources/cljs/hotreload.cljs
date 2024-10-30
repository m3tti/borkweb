(def timestamp (atom 0))

(defn fetch-timestamp
  []
  (->
   (js/fetch (str "/hotreload?last-modified=" @timestamp))
   (.then #(.text %))))

(defn reload
  [new-timestamp]
  (js/console.log new-timestamp)
  (when (not= @timestamp new-timestamp)
    (js/location.reload)))

(defn repl
  []
  (->
   (fetch-timestamp)
   (.then reload)
   (.finally #(js/setTimeout repl 600))))

(js/console.log "Hotreload running")
(->
 (fetch-timestamp)
 (.then #(reset! timestamp %))
 (.then repl))
