package br.com.senai.alunos.principal;

import javax.inject.Inject;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import br.com.senai.alunos.cdi.Message;
import br.com.senai.alunos.cdi.MessageFactory;
import br.com.senai.alunos.cdi.MessageType;
import br.com.senai.alunos.persist.AlunoRepository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {

    private static final String RPC_QUEUE_NAME = "alunos.persist";
    private static final String BROKER_HOST = System.getenv("BROKER_HOST");

    @Inject
    private MessageFactory mf;

    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(BROKER_HOST);
        Connection connection = null;
        try {
        	System.out.println(BROKER_HOST);
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(0);
            
            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = createConsumer(channel);

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            //loop to prevent reaching finally block
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException _ignore) {
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }

    private DefaultConsumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();

                String response = "";
                try {
                    String message = new String(body, "UTF-8");
                    MessageType executor = mf.getMessage(Message.Type.valueOf(properties.getHeaders().get("type").toString()));
                    response = executor.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

}
