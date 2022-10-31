package servico_clojure.java;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaConnector {

    public KafkaConnector(String host) {
        this.properties = configure(host);
    }

    private Properties properties;

    public Properties configure(String host) {
        //"127.0.0.1:9092"
        properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka_consumer_group");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        return properties;
    }

    public String consume(String topic) {
        var consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (var record : records) {
                    return record.value().toString();
                }
            }
        }
    }

    public String produce(String topic, String payload) {
        try {
            var producer = new KafkaProducer<String, String>(properties);
            var record = new ProducerRecord<>(topic, payload, payload);
            System.out.println(properties);

            Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                System.out.println("Mensagem enviada com sucesso:\nmensagem: " + payload  + data.topic() + "::: " + data.partition() + "/" + data.offset());
            };

            producer.send(record, callback);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return "produce ok: " + topic + " " + payload;
    }



}
