(ns kafka-client-app-for-clojure.examples.clients.clj.orders.producer
  (:require
   [clojure.data.json :as json]
   [clojure.java.io :as jio])
  (:import
   (java.util Properties)
   (org.apache.kafka.clients.admin AdminClient NewTopic)
   (org.apache.kafka.clients.producer Callback KafkaProducer ProducerConfig ProducerRecord)
   (org.apache.kafka.common.errors TopicExistsException)))

(defn- build-properties [config-fname]
  (with-open [config (jio/reader config-fname)]
    (doto (Properties.)
      (.putAll
       {ProducerConfig/BOOTSTRAP_SERVERS_CONFIG "localhost:9092"
        ProducerConfig/KEY_SERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringSerializer"
        ProducerConfig/VALUE_SERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringSerializer"})
      (.load config))))

(defn- create-topic! [topic partitions replication cloud-config]
  (let [ac (AdminClient/create cloud-config)]
    (try
      (.createTopics ac [(NewTopic. ^String topic  (int partitions) (short replication))])
      (catch TopicExistsException e nil)
      (finally
        (.close ac)))))

(defn create-order [order-id product quantity]
  {:order_id order-id
   :product product
   :quantity quantity})

(defn produce-orders [config-fname topic]
  (let [props (build-properties config-fname)]
    (with-open [producer (KafkaProducer. props)]
      (create-topic! topic 1 3 props)
      (doseq [order-id (range 1 11)]
        (let [order (create-order order-id "Product-1" (rand-int 10))]
          (let [key (str "order-" order-id)
                value (json/write-str order)]
            (printf "Producing order: %s - %s\n" key value)
            (.send producer (ProducerRecord. topic key value)))))
      (.flush producer)
      (printf "10 orders were produced to topic %s!\n" topic))))

(defn -main [& args]
  (apply produce-orders args))
