package br.com.senai.authorization.principal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Application {

	private static final String RPC_QUEUE_NAME = "authorization";	
	private static final String BROKER_HOST = System.getenv("BROKER_HOST");

	private Connection connection;

	private Channel createChannel() throws IOException {
		Channel channel = connection.createChannel();		
		return channel;
	}	

	public void run() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		this.connection = null;
		try {
			connection      = factory.newConnection();			
			Channel channel = this.createChannel();

			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
			System.out.println(" [x] Awaiting RPC requests");

			Consumer consumer = createConsumer(channel);
			channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

			//loop to prevent reaching finally block
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException _ignore) {}
			}
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		finally {
			if (connection != null)
				try {
					connection.close();
				} catch (IOException _ignore) {}
		}
	}

	private DefaultConsumer createConsumer(Channel channel) {
		return new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties
						.Builder()
						.correlationId(properties.getCorrelationId())
						.build();

				String response = "";
				try {						
					String message = new String(body,"UTF-8");
					System.out.println(message);
					Application app = new Application();
					response = app.send(message, "GET_BY_LOGIN");
				} catch (Exception e){
					e.printStackTrace();
				} finally {
					try {
						channel.basicAck(envelope.getDeliveryTag(), false);
						channel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	private String call(String message, String type) throws IOException, InterruptedException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		this.connection = null;
		connection      = factory.newConnection();
		String corrId = UUID.randomUUID().toString();
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", type);
		Channel channel = this.createChannel();
		String replyQueueName = channel.queueDeclare().getQueue();

		AMQP.BasicProperties props = new AMQP.BasicProperties
				.Builder()
				.correlationId(corrId)
				.headers(headers)
				.replyTo(replyQueueName)
				.build();		
		channel.basicPublish("", "alunos.persist", props, message.getBytes("UTF-8"));

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

	private String send(String message, String type) throws IOException, InterruptedException, TimeoutException {
		String response = null;
		try {
			System.out.println(" [x] Requesting "+type+" "+message);
			response = call(message, type);
			System.out.println(" [.] Got '" + response + "'");     
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (IOException _ignore) {}
		}
		return response;		
	}	


}
