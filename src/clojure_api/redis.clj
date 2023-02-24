(ns clojure_api.redis
  (:require [taoensso.carmine :as car :refer (wcar)])
  (:gen-class))

(def redis-connection {:pool {} :spec {:uri "redis://redis:6379/"}})
(defmacro wcar* [& body] `(car/wcar redis-connection ~@body))
