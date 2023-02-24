(ns clojure_api.handlers
  (:require [clojure.pprint :as pp] 
            [clojure_api.helpers :as helpers] 
            [clojure_api.storage :as storage]
            [clojure_api.client :as client]
            [clojure.data.json :as json]
            [clojure_api.validators :as v])
  (:gen-class))

(defn home-handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "clojure-api version 1.0.0"})

(defn broker-handler [req]
  (def body (helpers/parse-body (:body req)))
  (if (v/validate-brokers body)
    (do
      (def broker-id (storage/add-broker (:first_name body) (:last_name body)))
      {:status  200
       :headers {"Content-Type" "text/json"}
       :body    (json/write-str {:broker_id broker-id})})
    
    (do (def err "Invalid argument(s)")
        {:status  400
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:error err})})))

(defn quotes-handler [id request-body]
  (def body (helpers/parse-body request-body))
  (if (and (v/validate-uuid id) (v/validate-quotation body))
    (do (if (storage/broker-exists? id)
          (do
            (def quote (storage/get-quote (:age body) (:sex body)))
            (if quote
              (do
                (def quote-info (helpers/map-body quote))
                (def resp-body {:id (:id quote-info)
                               :age (:age body)
                               :sex (:sex body)
                               :price (:price quote-info)
                               :expire_at (:expire_at quote-info)})
                {:status  200
                 :headers {"Content-Type" "text/json"}
                 :body    (json/write-str resp-body)})
              (do
                (client/create-quotation body))))

          (do
            (def err (str "The broker id (" id ") was not found."))
            {:status  404
             :headers {"Content-Type" "text/json"}
             :body    (json/write-str {:error err})})))

    (do
        (def err "Invalid argument(s)")
        {:status  400
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:error err})})))

(defn policies-handler [id request-body]
  (def body (helpers/parse-body request-body))
  (if (and (v/validate-uuid id) (v/validate-policy body))
    (do (if (storage/broker-exists? id) 
          (client/create-policy body)

          (do
            (def err (str "The broker id (" id ") was not found."))
            {:status  404
             :headers {"Content-Type" "text/json"}
             :body    (json/write-str {:error err})})))
    
    (do
        (def err "Invalid argument(s)")
        {:status  400
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:error err})})))

(defn policy-handler [id policy-id]
  (if (and (v/validate-uuid policy-id) (v/validate-uuid id))
    (do (if (storage/broker-exists? id)
          (client/get-policy policy-id)

          (do
            (def err (str "The broker id (" id ") was not found."))
            {:status  404
             :headers {"Content-Type" "text/json"}
             :body    (json/write-str {:error err})})))
    (do 
        (def err "Invalid argument(s)")
        {:status  400
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:error err})})))

(defn not-found [req]
  {:status  404
   :headers {"Content-Type" "text/json"}
   :body    "not found"})