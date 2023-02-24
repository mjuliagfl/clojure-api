(ns clojure_api.validators 
  (:require [clojure.string :refer [lower-case]]
            [clojure.spec.alpha :refer [int-in-range?]]
            [clj-time.format :as f])
  (:gen-class))

(defn validate-name [name]
  (string? name))

(defn validate-sex [sex] 
  (if (nil? sex)
    false)
  (let [s (lower-case sex)]
    (case s
      "m" true
      "f" true
      "n" true
      false)))

(defn validate-age [age]
  (if (nil? age)
    false)
  (int-in-range? 0 100 age))

(defn validate-uuid [uuid]
  (if (nil? uuid)
    false)
  (let [u (try
            (java.util.UUID/fromString uuid)
            (catch Exception e nil))]
    (some? u)))

(defn validate-date-of-birth [date]
  (if (nil? date)
    false)
  (def built-in-formatter (f/formatters :date))
  (let [d (try
            (f/parse built-in-formatter date)
            (catch Exception e nil))]
    (some? d)))

(defn validate-brokers [body]
  (and (validate-name (:first_name body)) (validate-name (:last_name body))))

(defn validate-quotation [body]
  (and (validate-age (:age body)) (validate-sex (:sex body))))

(defn validate-policy [body]
  (and (validate-name (:name body))
       (validate-uuid (:quotation_id body))
       (validate-sex (:sex body))
       (validate-date-of-birth (:date_of_birth body))))
