(ns e2e.main
  (:require
   [config :as config]
   [etaoin.api :as e]
   [etaoin.keys :as k]
   [clojure.test :refer [deftest is use-fixtures run-tests]]))

(def driver (atom nil))

(defn warmup []
  (reset! driver (e/chrome)))

(defn teardown []
  (e/quit @driver)
  (reset! driver nil))

(defn with-browser [f]
  (warmup)
  (f)
  (teardown))

(use-fixtures :each with-browser)

;;
;; Your tests
;;
(defn go-to-index []
  (e/go @driver config/base-url)
  (is (= (e/get-title @driver) config/base-url)))

(deftest index-shown
  (go-to-index))

(comment
  ;; test
  (warmup)
  ;; Your code goes here
  (e/go @driver config/base-url)
  (teardown))

(comment
  ;; run all tests in main
  (run-tests 'e2e.main))
