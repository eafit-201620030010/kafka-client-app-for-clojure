(ns kafka-client-app-for-clojure.examples.clients.clj.transactions.producer
  (:gen-class)
  (:require
   [clojure.data.json :as json]
   [clojure.java.io :as jio]
   [clj-time.coerce :as coerce])
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

(defn generate-financial-transaction []
  (let [transaction-types ["purchase" "transfer" "withdrawal" "deposit"]
        random-type (rand-nth transaction-types)
        random-amount (+ 100 (rand-int 5000))
        current-time (coerce/to-long (clj-time.core/now))
        transaction-data {:type random-type
                          :amount random-amount
                          :timestamp current-time}]
    (json/write-str transaction-data)))

(defn produce-financial-transactions [config-fname topic]
  (let [props (build-properties config-fname)]
    (with-open [producer (KafkaProducer. props)]
      (create-topic! topic 1 3 props)
      (doseq [i (range 10)]
        (let [transaction (generate-financial-transaction)]
          (.send producer (ProducerRecord. topic "transaction" transaction))
          (printf "Producing transaction: %s\n" transaction)))))
  (printf "10 financial transactions were produced to topic %s!\n" topic))


(defn -main [& args]
  (apply produce-financial-transactions args))
