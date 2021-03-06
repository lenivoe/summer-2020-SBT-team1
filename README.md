# API Gateway and Service Discovery microservices
[![Build Status](https://travis-ci.org/lenivoe/summer-2020-SBT-team1.svg?branch=master)](https://travis-ci.org/lenivoe/summer-2020-SBT-team1)
[![Coverage Status](https://coveralls.io/repos/github/lenivoe/summer-2020-SBT-team1/badge.svg?branch=master)](https://coveralls.io/github/lenivoe/summer-2020-SBT-team1?branch=master)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2d9deeb1ccbc48a7bfd8364f8a8f9c9f)](https://www.codacy.com/manual/lenivoe/summer-2020-SBT-team1?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lenivoe/summer-2020-SBT-team1&amp;utm_campaign=Badge_Grade)
![GitHub last commit (branch)](https://img.shields.io/github/last-commit/lenivoe/summer-2020-SBT-team1/master)
![GitHub contributors](https://img.shields.io/github/contributors/lenivoe/summer-2020-SBT-team1)

Summer internship at SberTech of team 1.

## All microservices
* [API Gateway + Service Discovery](https://github.com/lenivoe/summer-2020-SBT-team1)

* [Content and Metadata management](https://github.com/ASurtaev/SummerSberPractice)

* [Collections and Rating](https://github.com/BorZzzenko/SummerPractice_SBT2020)

* [Auth/Validation and user permissions](https://github.com/D076/summer-practice-2020-SBT)

## API Documentation
[Swagger](https://lenivoe.github.io/summer-2020-SBT-team1/index.html)

## Installation
0.  It is necessary to install [JDK](https://jdk.java.net/archive/) version 11 or higher, [PostgreSQL](https://www.postgresql.org/download/).
1.  You need create database with some name (for example, "gateway") like [here](https://www.guru99.com/postgresql-create-database.html).
2.  Download "gateway-version.jar" and "application.properties" files from [releases](https://github.com/lenivoe/summer-2020-SBT-team1/releases).
3.  Open config file "application.properties" and set your port, database name, postgres password.
```properties
server.port=<port>

spring.datasource.url=jdbc:postgresql://localhost/<database name>
spring.datasource.username=postgres
spring.datasource.password=<postgres password>
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop

ping.interval=10000
ping.check_time=30000
```
4.  Launch app with parameters:
```bash
java -jar ./gateway-<version>.jar --spring.config.location=./application.properties
```
