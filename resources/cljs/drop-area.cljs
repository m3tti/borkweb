(require '["https://esm.sh/preact@10.19.2" :as react])
(require '["https://esm.sh/preact@10.19.2/hooks" :as hooks])

(defn drophandler [event]
  (js/console.log event))

(defn dragOverHandler [event]
  (js/console.log event))

(defn DropArea []
  #jsx [:<>
        [:div {:ondrop drophandler
               :ondragover dragOverHandler}
         [:p "Upload"]]])

(defonce el (js/document.getElementById "upload"))
(react/render #jsx [DropArea] el)
