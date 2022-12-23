# Java-StudIP-Telegram-Bot
![Maven](https://github.com/MrDrache333/Java-StudIP-Telegram-Bot/actions/workflows/maven.yml/badge.svg)
![Docker](https://github.com/MrDrache333/Java-StudIP-Telegram-Bot/actions/workflows/docker-image.yml/badge.svg)
[![GitHub release](https://img.shields.io/github/release/MrDrache333/Java-StudIP-Telegram-Bot?include_prereleases=&sort=semver&color=blue)](https://github.com/MrDrache333/Java-StudIP-Telegram-Bot/releases/)
[![License](https://img.shields.io/badge/License-MIT-blue)](#license)
[![issues - Java-StudIP-Telegram-Bot](https://img.shields.io/github/issues/MrDrache333/Java-StudIP-Telegram-Bot)](https://github.com/MrDrache333/Java-StudIP-Telegram-Bot/issues)

Keep track of updates and new materials for your courses on StudIP without constantly checking the platform. StudIP-Telegram is a Java-based bot that will scan your StudIP account for announcements and new/updated files in your current courses, and notify you through a Telegram chat using your own bot. It will also download new files and create a file structure similar to StudIP, making it easy to sync with a network-attached storage (NAS) device.

<img alt="Telegram_01" src="Screenshots/Screenshot_Telegram_02.jpg" width="500px"/>

## Features

### Account Management
- Logs in to your StudIP account using your stored credentials
- Grabs account information, including a list of your current courses

### Course Management
- Grabs information about your courses, including any announcements or updates
- Downloads new files and creates a file structure similar to StudIP
- Automatically formats and pushes course information to a Telegram chat

### File Management
- Downloads new files and creates a file structure similar to StudIP
- Makes it easy to sync with a network-attached storage (NAS) device

### Additional Features
- Sends updates to a Telegram chat using your own bot
- Provides a convenient way to track course updates without constantly checking StudIP
- Allows you to manage your StudIP account and courses from a single location
 
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
