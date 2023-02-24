(ns clojure_api.helpers
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn map-body [body]
  (json/read-str body :key-fn keyword))

(defn parse-body [requestbody]
  (let [body (slurp requestbody)]
    (map-body body)))