FROM node:18-slim
WORKDIR /app
COPY consumer.js .
RUN npm install amqplib
CMD ["node", "consumer.js"]
