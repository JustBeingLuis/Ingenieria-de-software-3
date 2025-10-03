const amqp = require('amqplib');

async function startConsumer() {
    const queue = 'educacion';
    const maxRetries = 10;
    const waitTime = 5000; // ms

    for (let attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            const connection = await amqp.connect({
                protocol: 'amqp',
                hostname: 'rabbitmq',
                port: 5672,
                username: 'user',
                password: 'password'
            });
            const channel = await connection.createChannel();
            await channel.assertQueue(queue, { durable: false });

            console.log("[✔] Conectado a RabbitMQ (Node.js). Esperando mensajes...");

            channel.consume(queue, (msg) => {
                console.log("[x] Recibido:", msg.content.toString());
            }, { noAck: true });

            break; // salimos del bucle si conectó
        } catch (err) {
            console.log(`[!] RabbitMQ no está listo. Intento ${attempt}/${maxRetries}`);
            await new Promise(res => setTimeout(res, waitTime));
        }
    }
}

startConsumer();
