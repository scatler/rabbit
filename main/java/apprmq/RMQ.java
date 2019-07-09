package apprmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RMQ {

    private boolean qautoDelete = false;
    private boolean qexclusive = false;
    private boolean qdurable = false;
    private ConnectionFactory fac = new ConnectionFactory();
    private Channel pch;
    private String routingKey = "";
    private String queueName = "defq1";
    private String exchangeName = "exchange1";
    private String exchangeType = "fanout";

    {
        try {
            pch = fac.newConnection().createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Receiver getReceiver() {
        Receiver r = new Receiver();

        return r;
    }

    public Sender getSender() {

        Sender s = new Sender();
        return s;
    }

    public void addQueue(String queueName, boolean qdurable, boolean qexclusive, boolean qautoDelete) {
        this.queueName = queueName;
        this.qautoDelete = qautoDelete;
        this.qexclusive = qexclusive;
        this.qdurable = qdurable;
        try {
            pch.queueDeclare(queueName, qdurable, qexclusive, qautoDelete,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addExchange (String name, String exchangeType) {
        exchangeName = name;
        this.exchangeType = exchangeType;
        try {
            pch.exchangeDeclare(name,exchangeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bindCurrent(String routingKey) {
        this.routingKey = routingKey;
        try {
            pch.queueBind(queueName,exchangeName, routingKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Client {
        Channel ch;

        public Client() {
            try {

                this.ch = fac.newConnection().createChannel();
                confChannelBasedOnCurrentOptions(ch);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            ;
        }

        private void confChannelBasedOnCurrentOptions(Channel ch) throws IOException {
            ch.queueDeclare(queueName, qdurable, qexclusive, qautoDelete,null);
            ch.exchangeDeclare(exchangeName,exchangeType);
            ch.queueBind(queueName,exchangeName,routingKey);
        }

    }

    public class Sender extends Client {

        public void sendMessage(String m) throws IOException {

            System.out.println("Sending:" + m);
            ch.basicPublish(exchangeName, routingKey, null, m.getBytes());
        }
    }

    public class Receiver extends Client {


        public void receive() {
            DeliverCallback deliverCallback  = (consumerTag, message) -> System.out.println("Received: " + new String(message.getBody(),"UTF-8"));
            receive(deliverCallback);
        }

        public void receive(DeliverCallback deliverCallback ) {
            try {
                ch.basicConsume(queueName,deliverCallback,consumerTag -> {});
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
