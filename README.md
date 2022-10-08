# Java-StudIP-Telegram-Bot
Proof of Concept Java StudIP Telegram Bot

![Telegram_01](Screenshots/Screenshot_Telegram_02.jpg)

## Overview

*What will this Bot do?*

- It will simply login to your StudIP-Account using your Credentials (Stored locally)
- It will grab your Accountinformations, Courses and Informations about the Courses
 - It will verify that there are Updates avaiable (News or Files)
 - It will download new Files and create a Filesturcture analog to StudIP (Easy to Sync to NAS for example)
 - It will automatically format and push these Informations to a Telegram Chat using your own Bot
 
 ### FAQ

Comming soon

## Installation without Docker

1. Download this Bot
2. Verify that Java 8 JRE is installed on your machine
3. Create a Telegram Bot (Simply create a Chat with [@BotFather](https://t.me/BotFather))
4. Copy the API-Key
5. Rename config-smple.yml to config.yml in data folder
6. Edit config.yml and add your credentials
7. Start the Bot via Terminal with `java -jar StudIP-Telegram.jar`
8. ...
9. Profit?

## Installation with Docker

1. Make sure you've installed Docker on your machine
2. Create a Telegram Bot (Simply create a Chat with [@BotFather](https://t.me/BotFather))
3. Copy the API-Key
4. Download this repo
5. Rename config-smple.yml to config.yml in data folder
6. Edit config.yml and add your credentials
7. Start this Bot using `docker-compose -d up`

## Compatibility tested

- University of Oldenburg: 07.10.2022
