![](https://media.discordapp.net/attachments/1184934424438132806/1199790581245820988/ai_assistant_discord_bot.png?ex=66201c0d&is=660da70d&hm=b8abdb9534c8cb626ae1f82454e46785f0346dccba2c2f92baabb4b258791966&=&format=webp&quality=lossless&width=1100&height=348)

#### Discord Bot that acts as your personal, executive assistnant. Not only it gives you answers, but perform actions in apps you use daily!

## Features

- Based on your queries, it learns information and uses it in subsequent queries so that they are as personalized to you as possible.
- **(Soon)** Bot can work with your calendar to manage meetings and invitations.
- **(Soon)** Bot can aggregate many sources with your tasks such as todo apps, Gmail and more and display all tasks in one place.
- **(Soon)** Bot can connect to your favourite apps to bring you current

## Technologies Used

- Java `11`
- Spring Boot `2.7.15`
- <a href="//github.com/DV8FromTheWorld/JDA">JDA</a> ``5.0.0-beta.15``
- <a href="//github.com/Chew/JDA-Chewtils">JDA Chewtils</a> ``2.0-SNAPSHOT``
- DotEnv `3.0.0`
- <a href="//github.com/nocodb/nocodb">NocoDB</a>
- <a href="//github.com/qdrant/qdrant">Qdrant</a>

## Setup
#### Clone the repository
```
git clone https://github.com/SaseQ/ai-assistant-discord-bot
```

#### Build the project
```
mvn clean package
```

#### Run the project
```
docker-compose up --build
```

#### NocoDB configuration

- Sign up local account: `http://localhost:6335/dashboard`.
- Create Base: `AI Assistant`.
- Create 2 tables: `Resources`, `Conversation`.
- Import table structures form csv files.

#### Qdrant configuration

- Create Base:
```
curl -X POST http://localhost:9595/api/v1/qdrant/crate-collection?name=Ai-Assistant
```

## Environment Variables
File: ``.env``
```
TOKEN=
OWNER_ID=
FORCE_GUILD=
PRIVATE_CHANNEL_ID=
ACTIVITY='AI Assistant'
OPENAI_API_KEY=
NOCODB_API_KEY=
```
| Field Name             | Usage                               |
|------------------------|-------------------------------------|
| ``TOKEN``              | Discord Bot Token                   |
| ``OWNER_ID``           | Your Discord Account ID             |
| ``FORCE_GUILD``        | Discord Server To Test Bot          |
| ``PRIVATE_CHANNEL_ID`` | Message Private Channel With Bot ID |
| ``OPENAI_API_KEY``     | OpenAI API Key                      |
| ``NOCODB_API_KEY``     | NocoDB API Key                      |
