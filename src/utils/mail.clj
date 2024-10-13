(ns utils.mail
  (:require
   [utils.runtime :as runtime]))

(runtime/if-bb
 (require '[pod.tzzh.mail :as m])
 (require '[postal.core :as p]))

(def settings
  {:host "smtp.gmail.com"
   :port 587
   :username "kylian.mbappe@gmail.com"
   :password "kylian123"})

;;{:subject "Subject of the email"
;; :from "kylian.mbappe@gmail.com"
;; :to ["somebody@somehwere.com"]
;; :cc ["somebodyelse@somehwere.com"]
;; :text "aaa" ;; for text body
;; :html "<b> kajfhajkfhakjs </b>" ;; for html body
;; :attachments ["./do-everything.clj"] ;; paths to the files to attch
;;}
(defn send-mail [& {:keys []
                    :as mail}]
  (runtime/if-bb
   (m/send-mail
    (into {}
          settings
          mail))
   (m/send-message settings mailq)))
