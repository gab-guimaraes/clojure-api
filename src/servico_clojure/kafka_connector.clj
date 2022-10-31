(ns servico-clojure.kafka-connector
  (:import (servico_clojure.java KafkaConnector)))

;(kafka-connector produce "TESTE_CLOJURE_KAFKA" "testando123")
;(.produce (new KafkaConnector "127.0.0.1:9092") "TESTE_CLOJURE_KAFKA" "testando")
;(-> (new KafkaConnector "127.0.0.1:9092")
;    (.produce "TEST_CLOJURE_KAFKA" "testando"))

(defn produce-kafka [topic data]
  (-> (new KafkaConnector "127.0.0.1:9092")
      (.produce topic data)))

(defn consumer-kafka [topic]
  (-> (new KafkaConnector "127.0.0.1:9092")
      (.consume topic))
  )

(def kafka-connector-obj (new KafkaConnector "127.0.0.1:9092"))

(defn consumer-kafka-loop [topic]
  (.consume kafka-connector-obj topic)
  (recur topic))

;(produce-kafka "LOJA_NOVO_PEDIDO" "COMPUTADOR")
;(produce-kafka "LOJA_NOVO_PEDIDO" "CELULAR")
;(produce-kafka "LOJA_NOVO_PEDIDO" "MOUSE")
;(produce-kafka "LOJA_NOVO_PEDIDO" "")



;(-> (new Car "Fiat")
;    .drive)