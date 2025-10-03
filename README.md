# Productor-Consumidor con RabbitMQ

Este proyecto demuestra el patrón Productor-Consumidor usando RabbitMQ como sistema de colas y múltiples implementaciones en Python, Java y JavaScript (Node.js) para productores y consumidores. Todo se orquesta con Docker y docker-compose.

## Arquitectura

- **RabbitMQ**: Servicio central de colas, expuesto con interfaz de administración en el puerto 15672.
- **Productores**:
  - `producer-python`: Productor en Python (`producer.py`)
  - `producer-java`: Productor en Java (`Producer.java`)
  - `producer-js`: Productor en Node.js (`producer.js`)
- **Consumidores**:
  - `consumer-py`: Consumidor en Python (`consumer.py`)
  - `consumer-java`: Consumidor en Java (`Consumer.java`)
  - `consumer-js`: Consumidor en Node.js (`consumer.js`)

Cada productor envía mensajes a las colas `tareas`, `deportes` y `educacion`. Cada consumidor está suscrito a una cola específica:
- `consumer-py` → `tareas`
- `consumer-java` → `deportes`
- `consumer-js` → `educacion`

## Estructura del proyecto

```
docker-compose.yml
consumer-java/
    Dockerfile
    pom.xml
    src/main/java/Consumer.java
consumer-js/
    consumer.js
    Dockerfile
consumer-py/
    consumer.py
    Dockerfile
producer/
    Dockerfile
    producer.py
producer-java/
    Dockerfile
    pom.xml
    src/main/java/com/example/Producer.java
producer-js/
    Dockerfile
    package.json
    producer.js
```

## Ejecución

1. **Requisitos**: Tener Docker y docker-compose instalados.
2. **Levantar todos los servicios**:
   ```sh
   docker-compose up --build
   ```
3. **Acceso a RabbitMQ**:
   - Interfaz web: [http://localhost:15672](http://localhost:15672)
   - Usuario: `user` | Contraseña: `password`

## Detalles de cada servicio

### Productores
- **Python**: Envía 10 mensajes a cada cola y termina.
- **Java** y **Node.js**: Envían mensajes periódicamente a cada cola.

### Consumidores
- Cada consumidor se conecta a RabbitMQ y muestra los mensajes recibidos de su cola asignada.

## Personalización
- Puedes modificar los nombres de las colas o el contenido de los mensajes en los archivos fuente de cada productor/consumidor.

## Créditos
- Ejemplo educativo para el patrón Productor-Consumidor con RabbitMQ y Docker.
