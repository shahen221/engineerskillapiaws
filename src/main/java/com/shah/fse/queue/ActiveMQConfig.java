package com.shah.fse.queue;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQConfig.class);
	
	@Value("${activemq.broker.url}")
	private String activeMQBokerURL;
	
	@Value("${activemq.user}")
	private String mqUser;
	
	@Value("${activemq.password}")
	private String mqPassword;
	
	@Value("${activemq.pool.max-connections}")
	private int maxConnections;
	
	@Bean
	public Session producerSession() {
		Connection producerConnection = null;
		Session producerSession = null;
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(activeMQBokerURL);
		activeMQConnectionFactory.setUserName(mqUser);
		activeMQConnectionFactory.setPassword(mqPassword);
		pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
		pooledConnectionFactory.setMaxConnections(maxConnections);
		try {
			producerConnection = pooledConnectionFactory.createConnection();
			producerConnection.start();
			producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
		} catch (JMSException jmsException) {
			LOGGER.error("Error occured while getting MQ connection/session ", jmsException);
		}
		return producerSession;
	}
	
	//@Bean
	public Queue createQueue() {
		Queue destinationQueue = null;
		try {
			Session producerSession = producerSession();
			destinationQueue = producerSession.createQueue("engineerProfileQueue");
		}
		catch (JMSException jmsException) {
			LOGGER.error("Error occured while getting MQ connection ", jmsException);
		}
		return destinationQueue;
	}
	
	/*private void cleanupResources() {
		try {
			if(!ObjectUtils.isEmpty(messageProducer)) {
				messageProducer.close();
			}
			if(!ObjectUtils.isEmpty(producerSession)) {
				producerSession.close();
			}
			if(!ObjectUtils.isEmpty(producerConnection)) {
				producerConnection.close();
			}
		} catch (JMSException jmsException) {
			LOGGER.error("Error occured while closing MQ resources ", jmsException);
		}
	}*/
}
