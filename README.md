# Java-StudIP-Telegram-Bot

Don't want to keep checking StudIP to see if there are any new announcements? Or checking every course to see if there
are already new lecture slides, exercises or other files that you have to download manually afterwards?
Here is the solution!
Let your computer scan regularly for announcements and new/updated files of your courses you are currently studying and
notify you immediately afterwards, and use the time you save more wisely.

<img alt="Telegram_01" src="Screenshots/Screenshot_Telegram_02.jpg" width="500px"/>

## Overview

*What will this Bot do?*

- It will simply login to your StudIP-Account using your Credentials (Stored locally)
- It will grab your Accountinformation, Courses and Information about the Courses
- It will verify that there are Updates avaiable (News or Files)
- It will download new Files and create a Filesturcture analog to StudIP (Easy to Sync to NAS for example)
- It will automatically format and push these Informations to a Telegram Chat using your own Bot
 
 ### FAQ

Coming soon

## Installation without Docker

1. Download this Bot
2. Verify that Java 8 JRE is installed on your machine
3. Create a Telegram Bot (Simply create a Chat with [@BotFather](https://t.me/BotFather))
4. Copy the API-Key
5. Rename config-sample.yml to config.yml in data folder
6. Edit config.yml and add your credentials
7. Start the Bot via Terminal with `java -jar StudIP-Telegram.jar`
8. ...
9. Profit?

## Installation with Docker

1. Make sure you've installed Docker on your machine
2. Create a Telegram Bot (Simply create a Chat with [@BotFather](https://t.me/BotFather))
3. Copy the API-Key
4. Download this repo
5. Rename config-sample.yml to config.yml in data folder
6. Edit config.yml and add your credentials
7. Start this Bot using `docker-compose -d up`

## Compatibility tested

- University of Oldenburg: 07.10.2022
