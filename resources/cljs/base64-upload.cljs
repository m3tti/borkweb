(ns base64-upload)

(defn upload [id ev]
  (let [reader (js/FileReader.)]
    (.readAsBinaryString reader (first ev.files))
    (set! reader.onload (fn [ev] 
                          (set! (.-value (js/document.getElementById id)) 
                                (js/btoa ev.target.result))))))

(set! js/window.base64_upload upload)