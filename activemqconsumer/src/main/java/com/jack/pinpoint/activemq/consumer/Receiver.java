package com.jack.pinpoint.activemq.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
 
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver
{
    private final static String QUEUE_NAME = "pptest";

    public static void main( String[] args ) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        try {
	    while (true) {
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(Boolean.FALSE,
						   Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(QUEUE_NAME);
		consumer = session.createConsumer(destination);
		while (true) {
		    //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
		    TextMessage message = (TextMessage) consumer.receive(100000);
		    if (null != message) {
			System.out.println("recevie message:" + message.getText());
		    } else {
			break;
		    }
		}
	    }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
