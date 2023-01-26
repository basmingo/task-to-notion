# task-to-notion
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Telegram_logo.svg/2048px-Telegram_logo.svg" alt="git" width="40" height="40"/>
Service for linking of tasks from chats to the tracker, WIP service for an Architectural company
Lightweight application on pure Java.

<b>Deployment instructions:</b>
- edit java/resources/application.properties file
- add your Telegram Token
- add Telegram bot ID
- add your Notion Token
- ! make sure, your database has a text field "Name" 
and a multi-select field "Assignee". !

Optional:
- edit java/resource/chatDB.json
- add a telegram chatname and linking board from Notion


<b>How to use:</b>
- send a following message to register teamchat:
#register CHAT_NAME = NOTION_DATABASE_ID
- send a following message to send a message in notion:
#task @USERNAME *Any message*
you also can attach photos and files.


<b>General commands:</b>
- #task @USERNAME *Any message*
- #register CHAT_NAME = NOTION_DATABASE_ID
- #reregister CHAT_NAME = NOTION_DATABASE_ID
- #delete CHAT_NAME
