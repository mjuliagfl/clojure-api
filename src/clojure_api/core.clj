(ns clojure_api.core
  (:require [org.httpkit.server :as server] 
            [compojure.core :refer :all] 
            [ring.middleware.defaults :refer :all]
            [clj-jwt.core  :refer :all]
            [clojure_api.routes :as routes])
  (:gen-class))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8090"))]
    (server/run-server (wrap-defaults #'routes/app-routes api-defaults) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
