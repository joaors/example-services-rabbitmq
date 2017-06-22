package br.com.senai.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class RabbitMQService {

	public static final String ALUNOS_PERSIST = "alunos.persist";
	public static final String AUTHORIZATION = "authorization";
	public static final String ALUNOS_LOG = "alunos.log";
	public static final String RABBIT_HOST = "192.168.99.100";
	
	private Connection connection;
	private Channel channel;	
	
	private String replyQueueName;

	private void connect() throws IOException, TimeoutException {
		createConnection();
		channel = connection.createChannel();

		replyQueueName = channel.queueDeclare().getQueue();		  
	}

	private void createConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBIT_HOST);
		factory.setConnectionTimeout(5000);

		connection = factory.newConnection();
	}

	private String call(String message, String type, String destination) throws IOException, InterruptedException, TimeoutException {
		String corrId = UUID.randomUUID().toString();
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", type);
		connect();		
		AMQP.BasicProperties props = new AMQP.BasicProperties
				.Builder()
				.correlationId(corrId)
				.headers(headers)
				.replyTo(replyQueueName)
				.build();
		channel.basicPublish("", destination, props, message.getBytes("UTF-8"));

		BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

		channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				try {
					if (properties.getCorrelationId().equals(corrId)) {
						response.offer(new String(body, "UTF-8"));
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return response.take();
	}

	public String send(String message, String type, String destination) throws IOException, InterruptedException, TimeoutException {
		String response = null;
		try {
			System.out.println(" [x] Requesting "+type+" "+message);
			response = call(message, type, destination);
			System.out.println(" [.] Got '" + response + "'");     
		} finally {
			try {
				connection.close();
			} catch (IOException _ignore) {}
		}
		return response;		
	}
	
	public void sendAsincrono(String message) throws IOException, TimeoutException {
	    createConnection();
		Channel channel = connection.createChannel();

	    channel.queueDeclare(ALUNOS_LOG, false, false, false, null);	    
	    channel.basicPublish("", ALUNOS_LOG, null, message.getBytes("UTF-8"));
	    System.out.println(" [x] Sent '" + message + "'");

	    channel.close();	
	}
}
