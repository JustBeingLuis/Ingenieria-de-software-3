import pika
import time

def connect_rabbitmq(max_retries=10, wait_time=5):
    """Intenta conectarse a RabbitMQ con reintentos."""
    for attempt in range(max_retries):
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host="rabbitmq",
                    port=5672,
                    credentials=pika.PlainCredentials("user", "password")
                )
            )
            print("[✔] Conectado a RabbitMQ")
            return connection
        except pika.exceptions.AMQPConnectionError:
            print(f"[!] RabbitMQ no está listo, reintentando en {wait_time} segundos... (intento {attempt+1}/{max_retries})")
            time.sleep(wait_time)
    raise Exception("❌ No se pudo conectar a RabbitMQ después de varios intentos")

# Conectar
connection = connect_rabbitmq()
channel = connection.channel()

# Declarar colas
queues = ["tareas", "deportes", "educacion"]
for q in queues:
    channel.queue_declare(queue=q, durable=False)

# Publicar mensajes
for i in range(10):
    channel.basic_publish(exchange="", routing_key="tareas", body=f"Tarea número {i}")
    channel.basic_publish(exchange="", routing_key="deportes", body=f"Noticia deportiva {i}")
    channel.basic_publish(exchange="", routing_key="educacion", body=f"Contenido educativo {i}")
    print(f"[x] Mensajes {i} enviados a las colas")

connection.close()
