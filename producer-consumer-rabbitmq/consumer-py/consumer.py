import pika, time

QUEUE_NAME = "tareas"

def connect_rabbitmq(max_retries=10, wait_time=5):
    for attempt in range(max_retries):
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host="rabbitmq",
                    port=5672,
                    credentials=pika.PlainCredentials("user", "password")
                )
            )
            print("[✔] Conectado a RabbitMQ (Python Consumer)")
            return connection
        except pika.exceptions.AMQPConnectionError:
            print(f"[!] RabbitMQ no está listo. Intento {attempt+1}/{max_retries}")
            time.sleep(wait_time)
    raise Exception("No se pudo conectar a RabbitMQ después de varios intentos")

def callback(ch, method, properties, body):
    print(f"[x] Recibido: {body.decode()}")

connection = connect_rabbitmq()
channel = connection.channel()
channel.queue_declare(queue=QUEUE_NAME)
channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback, auto_ack=True)

print("[✔] Esperando mensajes...")
channel.start_consuming()
