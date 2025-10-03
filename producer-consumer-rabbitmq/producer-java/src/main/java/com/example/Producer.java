package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    private static final String[] QUEUES = {"tareas", "deportes", "educacion"};
    private static final String HOST = "rabbitmq";
    private static final String USER = "user";
    private static final String PASS = "password";
    private static final int RETRY_DELAY = 5000; // ms

    public static void main(String[] argv) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USER);
        factory.setPassword(PASS);

        while (true) {
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                System.out.println("[✔] Conectado a RabbitMQ desde Producer Java");

                // Asegurar colas
                for (String queue : QUEUES) {
                    channel.queueDeclare(queue, false, false, false, null);
                }

                int counter = 0;
                while (true) {
                    for (String queue : QUEUES) {
                        String message = "Mensaje " + counter + " desde Producer Java a " + queue;
                        channel.basicPublish("", queue, null, message.getBytes());
                        System.out.println("[x] Enviado: " + message);
                    }
                    counter++;
                    Thread.sleep(3000);
                }

            } catch (Exception e) {
                System.err.println("[!] Error conectando o enviando: " + e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ignored) {
                }
                System.out.println("[*] Reintentando conexión...");
            }
        }
    }
}
