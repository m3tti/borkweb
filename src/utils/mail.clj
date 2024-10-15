(ns utils.mail
  (:require
   [clojure.set :as s]
   [utils.runtime :as runtime]))

(runtime/if-bb
 (require '[pod.tzzh.mail :as m])
 (require '[postal.core :as p]))

(def settings
  {:host "localhost"
   :port 1025
   :username "kylian.mbappe@gmail.com"
   :password "kylian123"})

;;{:subject "Subject of the email"
;; :from "kylian.mbappe@gmail.com"
;; :to ["somebody@somehwere.com"]
;; :cc ["somebodyelse@somehwere.com"]
;; :text "aaa" ;; for text body
;; :html "<b> kajfhajkfhakjs </b>" ;; for html body <- not supported yet
;; :attachments ["./do-everything.clj"] ;; paths to the files to attch
;;}
(defn send-mail [& {:keys []
                    :as mail}]
  (runtime/if-bb
   (m/send-mail
    (merge settings mail))
   (p/send-message settings (s/rename-keys mail {:text :body}))))
