(ns kafka-client-app-for-clojure.examples.clients.clj.orders.consumer
  (:require
   [clojure.data.json :as json]
   [clojure.java.io :as jio])
  (:import
   (java.time Duration)
   (java.util Properties)
   (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)))

(defn- build-properties [config-fname]
  (with-open [config (jio/reader config-fname)]
    (doto (Properties.)
      (.putAll {ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092"
                ConsumerConfig/GROUP_ID_CONFIG        "order-processing-group"
                ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringDeserializer"
                ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"})
      (.load config))))

(defn process-order [order]
  (println "Received message: " order))




(defn consume-orders [config-fname topic]
  (with-open [consumer (KafkaConsumer. (build-properties config-fname))]
    (.subscribe consumer [topic])
    (loop [processed-orders 0]
      (let [records (.poll consumer (Duration/ofMillis 100))]
        (doseq [record records]
          (let [order-data (.value record)]
            (process-order order-data))
          (inc processed-orders)))
      (recur processed-orders))))

(defn -main [& args]
  (apply consume-orders args))
