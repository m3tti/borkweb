;; kudos to https://kalabasa.github.io/htmz/
(defn htmc []
  (let [frame (js/document.querySelector "#htmc")]
    ;; Your extension here
    (js/setTimeout
     #(let [el# (.querySelector js/document (or frame.contentWindow.location.hash nil))]
        (el#.replaceWith.apply el# frame.contentDocument.body.childNodes)))))

(->
 (js/document.getElementById "htmc")
 (.addEventListener "load" htmc))
