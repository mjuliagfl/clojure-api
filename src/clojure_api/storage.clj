(ns clojure_api.storage
  (:require [clojure_api.redis :as redis]
            [taoensso.carmine :as car :refer (wcar)]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.data.json :as json]
            [clojure.string :refer [lower-case]])
  (:gen-class))

(def token (atom ""))

(defn add-broker [first-name last-name]
  (def full-name (str first-name " " last-name))
  (def uuid (.toString (java.util.UUID/randomUUID)))
  (redis/wcar* (car/set (str "brokers:" uuid) full-name))
  uuid)

(defn calculate-expiration [date]
  (def exp (f/parse date))
  (t/in-millis (t/interval (t/now) exp)))

(defn add-quote [quote] 
  (def key (str "quotes:" (:age quote) ":" (lower-case (:sex quote))))
  (def value (json/write-str {:id (:id quote) :price (:price quote) :expire_at (:expire_at quote)})) 
  (def exp-time (calculate-expiration (:expire_at quote)))
  (redis/wcar* (car/setex key exp-time value)))

(defn broker-exists? [id]
  (def broker (redis/wcar* (car/get (str "brokers:" id))))
  (if broker
    true
    false))

(defn get-quote [age sex]
  (def key (str "quotes:" age ":" (lower-case sex)))
  (redis/wcar* (car/get key)))

(defn save-token [jwttoken]
  (reset! token jwttoken))
