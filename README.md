## Test task telegram bot for MSocial
Выполненное тестовое задание на вакансию Java Web Developer
## Technologies used
* Java 11
* Maven
* Spring Boot 2.7.17
* PostgreSQL
* Liquibase
* Logback
* Java TelegramBots API
## Functional
* Запись пользователей в бд при первом обращении
* Обновление поля last_message_at при каждом сообщении
* Запись всех входящих и отправленных сообщений
* Раз в сутки удаление всех доменов и добавление новых в бд из https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50
* Раз в сутки отправка всем пользователям сообщения о количестве добавленных доменов
## Database Structure
![image](https://github.com/exxss/Test-Task-Telegram-Bot-For-MSocial/assets/91610178/3d8da5a0-4fc9-43d3-bcff-516a54941309)
