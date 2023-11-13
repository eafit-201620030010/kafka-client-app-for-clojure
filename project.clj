(defproject kafka-client-app-for-clojure "0.1.0-SNAPSHOT"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"]
                 [org.apache.kafka/kafka-clients "2.8.0"]
                 [clj-time "0.14.2"]]
  :aliases {"consume-orders" ["run" "-m" "kafka-client-app-for-clojure.examples.clients.clj.orders.consumer"]
            "produce-orders" ["run" "-m" "kafka-client-app-for-clojure.examples.clients.clj.orders.producer"]
            "consume-transactions" ["run" "-m" "kafka-client-app-for-clojure.examples.clients.clj.transactions.consumer"]
            "produce-transactions" ["run" "-m" "kafka-client-app-for-clojure.examples.clients.clj.transactions.producer"]}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
