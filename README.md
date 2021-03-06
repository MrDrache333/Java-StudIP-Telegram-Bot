# Java-StudIP-Telegram-Bot
Proof of Concept Java StudIP Telegram Bot

![Telegram_01](Screenshots/Screenshot_Telegram_02.jpg)

## Overview

*What will this Bot do?*
 - It will simply login to your StudIP-Account using your Credentials (Stored localy and secure)
 - It will grab your Accountinformations, Courses and Informations about the Courses
 - It will verify that there are Updates avaiable (News or Files)
 - It will download new Files and create a Filesturcture analog to StudIP (Easy to Sync to NAS for example)
 - It will automatically format and push these Informations to a Telegram Chat using your own Bot
 
 ### FAQ
 *Our StudI-Api is not activated. Will it still work?*
 
Yes. This Bot works using a virtual Browser instance to comunicate with StudIP. So no API is needet :)

 *Can i use Webhooks too?*
 
 Yes you can.

## Installation

1. Download this Bot
2. Verify that Java 8 JRE is installed on your machine
3. Create a Telegram Bot (Simply create a Chat with [@BotFather](https://t.me/BotFather))
4. Copy the API-Key
5. Start the Bot via Terminal with `java -jar StudIP-Telegram.jar INIT`
6. Fill out your Credentials
7. ...
8. Profit?

## WebHooks
You can use WebHooks/ HTTP-POST Requests with this Bot too. Simply edit the `modulelist.stud` File and change the following Attributes:

`
moduleId.sendType:
1 = Only Telegram, 2 = Only WebHook, 3 = Both
`
`moduleId.webhook: WebHook-URL`

Data would be send as UTF-8 encoded json Object.

`{"content":"Content as Text"
}`

## Compatibility tested

- University of Oldenburg: 15.04.2021


## Roadmap

- More settings, customisation
- Better commandline Interface
- Optimise Code a Lot
- Add a Ton of Documentation etc.

MORE WILL FOLLOW
