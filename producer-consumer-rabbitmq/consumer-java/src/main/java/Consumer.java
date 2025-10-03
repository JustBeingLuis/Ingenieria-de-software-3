import com.rabbitmq.client.*;

public class Consumer {
    private final static String QUEUE_NAME = "deportes";

    public static void main(String[] argv) {
        int maxRetries = 10;   // número máximo de intentos
        int waitTime = 5000;   // tiempo entre intentos (ms)

        Connection connection = null;
        Channel channel = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("rabbitmq");   // 👈 nombre del servicio docker-compose
                factory.setPort(5672);
                factory.setUsername("user");
                factory.setPassword("password");

                connection = factory.newConnection();
                channel = connection.createChannel();

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                System.out.println("[✔] Conectado a RabbitMQ. Esperando mensajes...");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println("[x] Recibido: '" + message + "'");
                };

                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

                // 🚀 Importante: salimos del bucle si la conexión fue exitosa
                break;

            } catch (Exception e) {
                System.out.println("[!] RabbitMQ no está listo. Intento " + attempt + "/" + maxRetries);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (connection == null) {
            System.err.println("[✘] No se pudo conectar a RabbitMQ después de varios intentos.");
            System.exit(1);
        }
    }
}
