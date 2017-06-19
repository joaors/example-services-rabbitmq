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

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "alunos.persist";
	private String replyQueueName;

	private void connect() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.99.100");
		factory.setConnectionTimeout(5000);

		connection = factory.newConnection();
		channel = connection.createChannel();

		replyQueueName = channel.queueDeclare().getQueue();		  
	}

	private String call(String message, String type) throws IOException, InterruptedException, TimeoutException {
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
		channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

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

	public String send(String message, String type) throws IOException, InterruptedException, TimeoutException {
		String response = null;
		try {
			System.out.println(" [x] Requesting "+type+" "+message);
			response = call(message, type);
			System.out.println(" [.] Got '" + response + "'");     
		} finally {
			try {
				connection.close();
			} catch (IOException _ignore) {}
		}
		return response;		
	}
}
