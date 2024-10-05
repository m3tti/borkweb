(ns utils.runtime)

(defmacro if-bb
  "Check if we are running on babashka or cljure jvm"
  [then else]
  (if (System/getProperty "babashka.version")
    then else))
