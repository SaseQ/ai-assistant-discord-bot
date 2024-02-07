FROM openjdk:11 AS build
ADD target/ai-assistant-discord-bot-0.0.1-SNAPSHOT.jar .
ADD .env .env
ADD prompt.json prompt.json
ENTRYPOINT ["java", "-jar", "ai-assistant-discord-bot-0.0.1-SNAPSHOT.jar"]
