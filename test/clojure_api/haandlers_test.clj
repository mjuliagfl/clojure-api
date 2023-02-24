(ns clojure_api.haandlers-test
  (:use clojure.test 
        clojure_api.handlers
        clojure_api.routes
        )
  (:require [ring.mock.request :as mock]))


(deftest test-app
  (testing "home"
    (let [response (app-routes (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response)
             "clojure-api version 1.0.0"))))
  
  (testing "quotes"
    (let [response (app-routes (mock/request :post "/brokers/059ea0cd-f310-4b9f-8eac-fca01da13f42/quotes" "{\"first_name\": \"jane\",\"last_name\": \"doe\"}"))]
      (is (= (:status response) 400))
      (is (= (:body response)
             "{\"error\":\"Invalid argument(s)\"}"))))
  
  (testing "brokers"
      (let [response (app-routes (mock/request :post "/brokers" "{\"age\": 33,\"sex\": \"f\"}"))]
        (is (= (:status response) 400))
        (is (= (:body response)
               "{\"error\":\"Invalid argument(s)\"}"))))
  
  (testing "policies"
    (let [response (app-routes (mock/request :post "/brokers/059ea0cd-f310-4b9f-8eac-fca01da13f42/policies" "{\"age\": 33,\"sex\": \"f\"}"))]
      (is (= (:status response) 400))
      (is (= (:body response)
             "{\"error\":\"Invalid argument(s)\"}")))) 

  (testing "not-found"
    (let [response (app-routes (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))