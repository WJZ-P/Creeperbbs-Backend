package me.wjz.creeperhub.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {
    @Value("${rocketmq.name-server}")
    private String nameServer;
    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Bean
    public DefaultMQProducer mqProducer() {
        DefaultMQProducer producer = new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        return producer;
    }

    @Bean
    public RocketMQTemplate rocketMQTemplate(DefaultMQProducer mqProducer) {
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(mqProducer);
        return template;
    }
}
