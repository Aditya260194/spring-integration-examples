package com.study.springintegration.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
@EnableIntegration
public class DatabaseIntegrationConfig {

    private String selectQuery="select * from Test where id=1";
    private String updateQuery="update Test set Name=:payload where id=1";

    public MessageChannel databaseChannel(){
        return new DirectChannel();
    }

    public MessageChannel databaseChannel2(){
        return new DirectChannel();
    }

    @Value("${spring.datasource.url}") String dbUrl;
    @Value("${spring.datasource.username}") String username;
    @Value("${spring.datasource.password}") String password;
    @Value("${spring.datasource.driver-class-name}") String dbClass;

    @InboundChannelAdapter(value = "databaseChannel", poller = @Poller(fixedDelay = "10000"))
    public Message<Object> jdbcPollingChannelAdapter(){
        JdbcPollingChannelAdapter source = new JdbcPollingChannelAdapter(
                dataSource(),selectQuery);
        source.setRowMapper(((resultSet, rowNum) -> {
            String name = resultSet.getString(2);
            return name;
        }));
        source.setMaxRows(1);
        return source.receive();
    }

    @Bean
    @Transformer(inputChannel = "databaseChannel", outputChannel = "databaseChannel2")
    public DatabaseTransformer databaseTransformer(){
        return new DatabaseTransformer();
    }

    @Bean
    @ServiceActivator(inputChannel = "databaseChannel2")
    public MessageHandler messageHandler(){
        JdbcMessageHandler jdbcMessageHandler = new JdbcMessageHandler(dataSource(),updateQuery);
        return jdbcMessageHandler;
    }

    @Bean
    public DataSource dataSource(){
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(dbUrl);
        mysqlDataSource.setPassword(password);
        mysqlDataSource.setUser(username);
        return mysqlDataSource;
    }

}
