;; kudos to https://kalabasa.github.io/htmz/
(defn htmc [frame]
  ;; Your extension here
  (js/setTimeout
   #(let [el# (.querySelector js/document (or frame.srcElement.contentWindow.location.hash nil))]
     (el#.replaceWith.apply el# frame.srcElement.contentDocument.body.childNodes))))

(->
 (js/document.getElementById "htmc")
 (.addEventListener "load" htmc))
