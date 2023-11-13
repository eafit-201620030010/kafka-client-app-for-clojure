# kafka-client-app-for-clojure

Event-Driven Microservices with Apache Kafka and Clojure

## Prerequisites

- Java 8 or Java 11
- Clojure/Leiningen
- Kafka Cluster with Confluent Cloud

## Setup

```sh
git clone https://github.com/eafit-201620030010/kafka-client-app-for-clojure.git
```

- Configuration parameters to connect to your Kafka Cluster. for example: $HOME/.confluent/clojure.config

- Template configuration file for Confluent Cloud

```sh
# Required connection configs for Kafka producer, consumer, and admin
bootstrap.servers={{ BROKER_ENDPOINT }}
security.protocol=SASL_SSL
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='{{ CLUSTER_API_KEY }}' password='{{ CLUSTER_API_SECRET }}';
sasl.mechanism=PLAIN
# Required for correctness in Apache Kafka clients prior to 2.6
client.dns.lookup=use_all_dns_ips

# Best practice for higher availability in Apache Kafka clients prior to 3.0
session.timeout.ms=45000

# Best practice for Kafka producer to prevent data loss
acks=all
```

`

## Run examples

1. Order Producer & Consumer

```sh

clojure/kafka-client-app-for-clojure$ lein produce-orders $HOME/.confluent/clojure.config producing-order

# You should see:

Producing order: order-1 - {"order_id":1,"product":"Product-1","quantity":4}
Producing order: order-2 - {"order_id":2,"product":"Product-1","quantity":1}
Producing order: order-3 - {"order_id":3,"product":"Product-1","quantity":7}
Producing order: order-4 - {"order_id":4,"product":"Product-1","quantity":9}
Producing order: order-5 - {"order_id":5,"product":"Product-1","quantity":8}
Producing order: order-6 - {"order_id":6,"product":"Product-1","quantity":5}
Producing order: order-7 - {"order_id":7,"product":"Product-1","quantity":7}
Producing order: order-8 - {"order_id":8,"product":"Product-1","quantity":1}
Producing order: order-9 - {"order_id":9,"product":"Product-1","quantity":9}
Producing order: order-10 - {"order_id":10,"product":"Product-1","quantity":5}
```

`

```sh
kafka-client-app-for-clojure$ lein consume-transactions $HOME/.confluent/clojure.config producing-transaction

# You should see:

Received message:  {"order_id":1,"product":"Product-1","quantity":4}
Received message:  {"order_id":2,"product":"Product-1","quantity":1}
Received message:  {"order_id":3,"product":"Product-1","quantity":7}
Received message:  {"order_id":4,"product":"Product-1","quantity":9}
Received message:  {"order_id":5,"product":"Product-1","quantity":8}
Received message:  {"order_id":6,"product":"Product-1","quantity":5}
Received message:  {"order_id":7,"product":"Product-1","quantity":7}
Received message:  {"order_id":8,"product":"Product-1","quantity":1}
Received message:  {"order_id":9,"product":"Product-1","quantity":9}
Received message:  {"order_id":10,"product":"Product-1","quantity":5}
```

2. Transaction Producer and Consumer

```sh
clojure/kafka-client-app-for-clojure$ lein produce-transactions $HOME/.confluent/clojure.config producing-transaction

# You should see:

Producing transaction: {"type":"purchase","amount":2854,"timestamp":1699834328128}
Producing transaction: {"type":"withdrawal","amount":2566,"timestamp":1699834329772}
Producing transaction: {"type":"purchase","amount":622,"timestamp":1699834329772}
Producing transaction: {"type":"transfer","amount":988,"timestamp":1699834329773}
Producing transaction: {"type":"deposit","amount":3041,"timestamp":1699834329773}
Producing transaction: {"type":"transfer","amount":1525,"timestamp":1699834329773}
Producing transaction: {"type":"withdrawal","amount":1212,"timestamp":1699834329773}
Producing transaction: {"type":"deposit","amount":739,"timestamp":1699834329774}
Producing transaction: {"type":"transfer","amount":2642,"timestamp":1699834329774}
Producing transaction: {"type":"withdrawal","amount":1108,"timestamp":1699834329774}
10 financial transactions were produced to topic producing-transaction!
```

```sh
clojure/kafka-client-app-for-clojure$ lein consume-transactions $HOME/.confluent/clojure.config producing-transaction

# You should see:

Waiting for message in KafkaConsumer.poll
Waiting for message in KafkaConsumer.poll

Purchase made: $2854
Withdrawn: $2566
Purchase made: $622
Transfer received: $988
Deposited: $3041
Transfer received: $1525
Withdrawn: $1212
Deposited: $739
Transfer received: $2642
Withdrawn: $1108

Waiting for message in KafkaConsumer.poll

```

## License

Copyright © 2023 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
