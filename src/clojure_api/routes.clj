(ns clojure_api.routes
   (:require [compojure.core :refer :all]
             [compojure.route :as route]
             [clojure_api.handlers :as handlers])
  (:gen-class))


(defroutes app-routes
  (GET "/" [] handlers/home-handler)
  (context "/brokers" [] (defroutes brokers-routes
                           (POST "/" [] handlers/broker-handler)
                           (context "/:id" [id] (defroutes broker-routes
                                                  (POST "/quotes" {body :body} (handlers/quotes-handler id body))
                                                  (POST "/policies" {body :body} (handlers/policies-handler id body))
                                                  (context "/policies/:policy-id" [policy-id] (defroutes policies-routes
                                                                                       (GET "/" [] (handlers/policy-handler id policy-id))))))))
  (route/not-found handlers/not-found))