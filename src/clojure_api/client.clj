(ns clojure_api.client
  (:require [org.httpkit.client :as http]
            [clojure_api.helpers :as helpers]
            [clojure_api.storage :as storage]
            [clojure.data.json :as json]) 
  (:gen-class))

(def host "https://api.180s.com.br")
(def apikey "kTDRyyPpnPBrDzd")

(defn auth []
  (let [response @(http/post (str host "/api/auth")
                             {:headers {"X-Api-Key" apikey}})]
    (if (= (:status response) 200)
      (do
        (def jwt-token (:access_token (helpers/map-body (:body response))))
        (storage/save-token jwt-token)
        jwt-token))))


(defn create-quotation [reqbody]
  (def body (json/write-str reqbody))
  (let [response @(http/post (str host "/api/quotations")
                             {:headers {"Authorization" (str "bearer " @storage/token)
                                        "Content-Type" "application/json"}
                              :body body})] 
    (case (:status response)
      200 (do
            (def response-body (helpers/map-body (:body response)))
            (storage/add-quote response-body)
            (json/write-str response-body))
      400 (do
            response)
      403 (do
            (println "will request auth")
            (auth)
            (create-quotation reqbody))
      404 (do
            response)
      500 (do
            response))))

(defn create-policy [reqbody]
  (def body (json/write-str reqbody))
  (let [response @(http/post (str host "/api/policies")
                             {:headers {"Authorization" (str "bearer " @storage/token)
                                        "Content-Type" "application/json"}
                              :body body})]
    (case (:status response)
      200 (do
            (def response-body (helpers/map-body (:body response)))
            (println response)
            (json/write-str response-body))
      400 (do
            response)
      403 (do
            (auth)
            (create-policy reqbody))
      404 (do
            response)
      500 (do
            response))))

(defn get-policy [policyid]
  (let [response @(http/get (str host (str "/api/policies/" policyid))
                             {:headers {"Authorization" (str "bearer " @storage/token)}})]

    (case (:status response)
      200 (do
            (def response-body (helpers/map-body (:body response)))
            (json/write-str response-body))
      400 (do
            response)
      403 (do
            (auth)
            (get-policy policyid))
      404 (do
            response)
      500 (do
            response))))