(ns kafka-client-app-for-clojure.examples.clients.clj.transactions.consumer
  (:gen-class)
  (:require
   [clojure.data.json :as json]
   [clojure.java.io :as jio]
   [clj-time.coerce :as coerce])
  (:import
   (java.time Duration)
   (java.util Properties)
   (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)))

(defn- build-properties [config-fname]
  (with-open [config (jio/reader config-fname)]
    (doto (Properties.)
      (.putAll {ConsumerConfig/GROUP_ID_CONFIG                 "transaction-processing-group"
                ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringDeserializer"
                ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"})
      (.load config))))

(defn process-financial-transaction [transaction]
  (let [transaction-data (json/read-str transaction)
        transaction-type (get transaction-data "type")
        transaction-amount (get transaction-data "amount")]
    (condp = transaction-type
      "purchase" (println (str "Purchase made: $" transaction-amount))
      "transfer" (println (str "Transfer received: $" transaction-amount))
      "withdrawal" (println (str "Withdrawn: $" transaction-amount))
      "deposit" (println (str "Deposited: $" transaction-amount)))))

  (defn consume-transactions [config-fname topic]
    (with-open [consumer (KafkaConsumer. (build-properties config-fname))]
      (.subscribe consumer [topic])
      (loop [tc 0
             records []]
        (let [new-tc (reduce
                      (fn [tc record]
                        (let [value (.value record)]
                          (process-financial-transaction value)
                          tc))
                      tc
                      records)]
          (println "Waiting for message in KafkaConsumer.poll")
          (recur new-tc
                 (seq (.poll consumer (Duration/ofSeconds 1))))))))

  (defn -main [& args]
    (apply consume-transactions args))
