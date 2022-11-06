package com.shah.fse.queue;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shah.fse.entity.EngineerDetails;

@Component
public class ActiveMQMessageProducer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQMessageProducer.class);
	
	//@Autowired
	//Queue messageQueue;
	
	@Autowired
	Session producerSession;
	
	
	public void sendProducerMessage(EngineerDetails engineerDetails) {
		Queue destinationQueue = null;
		MessageProducer messageProducer = null;
		ObjectMessage engineerProfileMessage = null;
		try {
			destinationQueue = producerSession.createQueue("engineerProfileQueue");
			messageProducer = producerSession.createProducer(destinationQueue);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			engineerProfileMessage = producerSession.createObjectMessage();
			engineerProfileMessage.setObject(engineerDetails);
			messageProducer.send(engineerProfileMessage);
			LOGGER.info("Message sent to MQ successfully.");
		} catch (JMSException jmsException) {
			LOGGER.error("Error occured while sending message to AWS MQ  ", jmsException);
		}
	}
	
}
