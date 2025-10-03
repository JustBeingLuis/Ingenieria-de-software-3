const amqp = require("amqplib");

const QUEUES = ["tareas", "deportes", "educacion"];
const RABBITMQ_HOST = "rabbitmq";
const RABBITMQ_USER = "user";
const RABBITMQ_PASS = "password";
const RETRY_DELAY = 5000; // 5 segundos

async function connectRabbitMQ() {
  while (true) {
    try {
      console.log("[*] Intentando conectar a RabbitMQ...");
      const connection = await amqp.connect({
        protocol: "amqp",
        hostname: RABBITMQ_HOST,
        port: 5672,
        username: RABBITMQ_USER,
        password: RABBITMQ_PASS,
      });
      console.log("[✔] Conectado a RabbitMQ desde Producer JS");
      return connection;
    } catch (err) {
      console.error("[!] RabbitMQ no disponible. Reintentando en 5s...");
      await new Promise((resolve) => setTimeout(resolve, RETRY_DELAY));
    }
  }
}

async function publishMessages() {
  try {
    const connection = await connectRabbitMQ();
    const channel = await connection.createChannel();

    // Asegurar todas las colas
    for (const queue of QUEUES) {
      await channel.assertQueue(queue, { durable: false });
    }

    let counter = 0;
    setInterval(() => {
      for (const queue of QUEUES) {
        const msg = `Mensaje ${counter} desde Producer JS a ${queue}`;
        channel.sendToQueue(queue, Buffer.from(msg));
        console.log("[x] Enviado:", msg);
      }
      counter++;
    }, 3000);

    // Manejo si RabbitMQ se cae
    connection.on("close", () => {
      console.error("[!] Conexión a RabbitMQ perdida. Reconectando...");
      setTimeout(publishMessages, RETRY_DELAY);
    });
    connection.on("error", (err) => {
      console.error("[!] Error en conexión RabbitMQ:", err.message);
    });

  } catch (err) {
    console.error("Error crítico en Producer JS:", err);
    setTimeout(publishMessages, RETRY_DELAY);
  }
}

publishMessages();
