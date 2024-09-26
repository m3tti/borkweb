(require '["https://cdn.jsdelivr.net/npm/preact-custom-element@4.3.0/+esm$default" :as register])

(defn Greeting [{:keys [name]}]
  #jsx [:p "Hello, " name])

(register Greeting "x-greeting", ["name"], {:shadow false})
