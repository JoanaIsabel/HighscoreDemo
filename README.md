# HighscoreDemo
 A small backend application to handle Highscore Lists. I wrote this to introduce myself to Java Spring/Hibernate and the backend of Rest APIs

 
---

 # Endpoints

 The application currently has two endpoints with multiple functionalities. One endpoint to handle everything player related and another one to handle all actions regarding scores.


 # Player Endpoint

 The player endpoint allows to create a new player, find an existing player by id or change the nickname of an existing user. A player is needed to map scores. Obviously there can be no score without a player who achieved it.

 # Score Endpoint

 The Score endpoint allows to enter a new score, find scores of all players or to find scores related to a player.

---
 ## Create a new player
 This endpoint can be used to create a new player with a nickname. Nicknames don't need to be unique.

 | Url      | /player               |
 | -------- |---------------|
 | Verb     | PUT      |
 | Header   | Content-Type: application/json      |
 |Example Request | ```json { "nickname": "Bob"}```|
 |Response Status Code | 201: Created a new Player  </br> 400: json body or nickname missing|
 |Response Example | ```json { "id": 1,"nickname": "Bob"}```|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|nickname |string|yes|Bob|The Nickname of the Player.|

---

## Get a player
This endpoint can be used to get a player providing a id.
| Url      | /player/{playerId}               |
| -------- |---------------|
| Verb     | GET      |
 | Header   | no header needed      |
 |Request | no body needed, just provide the id in the URL. playerId is a long |
 |Response Status Code | 200: Created a new Player </br> 400: json body or nickname missing|
 |Response Example | ```json { "id": 1,"nickname": "Bob"}```|

 ---

 ## Update a player name
 This endpoint can be used to edit the name of an existing player.

  | Url      | /player/{playerId}               |
 | -------- |---------------|
 | Verb     | POST      |
 | Header   | Content-Type: application/json      |
 |Example Request | ```json { "nickname": "Bob"}```|
 |Response Status Code | 200: Updated name  </br> 400: json body or nickname missing </br> 404: player not found|
 |Response Example | ```json { "id": 1,"nickname": "Bob"}```|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|nickname |string|yes|Bob|New nickname of the Player.|

---

## Create a new Score
This endpoint can be used to create a new Score. Every Score needs to be related to a player. Scores can be in the range of int values. If no playerId is provided in the request the backend will create a new player with the nickname guest and return the id. Through this id the player could later still change his nickname and keep all scores made as guest.

  | Url      | /score/{playerId}               |
 | -------- |---------------|
 | Verb     | PUT      |
 | Header   | Content-Type: application/json      |
 |Example Request | ```json { "value": 100, "date": "08.06.2019 15:11", playerId:1 }```|
 |Response Status Code | 201: Created new score  </br> 400: json body, value or date missing |
 |Response Example | ```json {"id": 2, "value": 12354, "date": "14-07-2019 19:38", "player": {"id": 1,"nickname": "guest"}}```|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|value | int |yes|100|The score made by the player.|
|date|string|yes|08.06.2019 15:11|Date at which the score was achieved|
|playerId|long|no|1|Player who achieved the score, if not provider a new player will be created|

---

## Get global Highscore List

This endpoint will fetch the highest scores made by all players and return them in an array ordered in descending order starting by the highest score.

| Url      | /score               |
| -------- |---------------|
| Verb     | GET      |
| Header   | No header needed     |
|Example Request | ```json { "limit": 10 }```|
|Response Status Code | 200: OK |
|Response Example | ```json [{"id": 5,"value": 100000,"date": "14-07-2019 19:38","player": {"id": 3,"nickname": "Bob"}}]```|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|limit | int |no|10|The number scores you want to get. If not provided you will get all scores ever made. |

---

## Get global Highscore List

This endpoint will fetch the highest scores made by a given players and return them in an array ordered in descending order starting by the highest score.

| Url      | /score/{playerId}               |
| -------- |---------------|
| Verb     | GET      |
| Header   | No header needed     |
|Example Request | ```json { "limit": 10 }```|
|Response Status Code | 200: OK  404: no player found |
|Response Example | ```json [{"id": 5,"value": 100000,"date": "14-07-2019 19:38","player": {"id": 3,"nickname": "Bob"}}]```|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|limit | int |no|10|The number scores you want to get. If not provided you will get all scores ever made by this player. |
