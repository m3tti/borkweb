(ns utils.runtime)

(defmacro if-bb [then else]
  (if (System/getProperty "babashka.version")
    then else))
