# Kafka-Database-Replication
Приложение, использующее инструмент Change Data Capture для репликации данных между двумя экземплярами REST-full web-сервиса
При изменении данных в любом из этих web-приложений (данные находятся на локальной базе данных),
данные меняются и в другой базе данных. Два экземпляра приложения находятся на разных портах, имеют SSL-подключение,
аутентификацию, авторизацию пользователей. За репликацию данных отвечают два экземпляра приложения consumer.
Всё приложение собрано в Docker, использует Kafka и Debezium.

## Содержание
- [Технологии](#технологии)
- [Использование](#использование)
- [To do](#to-do)
- [Разработчик проекта](#разработчик-проекта)

## Технологии
- [Spring](https://spring.io/)
- [Java 17](https://www.java.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [Hibernate](https://hibernate.org/)
- [Apache Kafka](https://kafka.apache.org/)
- [Debezium](https://debezium.io/)
- [Apache Zookiper](https://zookeeper.apache.org/)
- [Docker](https://www.docker.com/)
- [Thymeleaf](https://www.thymeleaf.org/)
- HTML
- JavaScript

## Использование
Развёртывание приложения (находясь в директории проекта):

1. Выполните сборку web-сервиса в jar: 

   mvn -f students-marks-app/pom.xml package

3. Переместите в students-marks-app/target файлы keystore.jks и server.cer (Или поместите туда свои сертификаты)

4. Выполните сборку consumer в jar:

   mvn -f consumer/pom.xml package

5. Выполните сборку docker-образов и компоновку контейнеров:

   docker-compose up -d

7. После того, как все контейнеры запустились, выполните команды для помещения в debezium коннекторов:
   
   curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @connector1.json
   
   curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @connector2.json

8. Проверить успешность операций с debezium можно перейдя на: localhost:8080, там должны быть запущены два коннектора

9. Проверить состояние Kafka можно перейдя на: localhost:8090, там должны быть созданы шесть топиков (кроме служебных)

10. Если все контейнеры запустились, оба контейнера consumer-1 и consumer-2 присоединились к топикам, то можно протестировать приложение, перейдя на:
    https://localhost:8443
    https://localhost:7443

11. В форме логина нужно указать логин - admin1 и пароль - secret

## To do
- [ ] Исправить ошибки в main.js

## Разработчик проекта
- [Карпов Никита](t.me/karpoffN) — Front-End Engineer
