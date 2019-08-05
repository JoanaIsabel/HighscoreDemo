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
 |Response Example | { "id": 1,"nickname": "Bob"}|

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
 |Response Example | { "id": 1,"nickname": "Bob"}|

 ---

 ## Update a player name
 This endpoint can be used to edit the name of an existing player.

  | Url      | /player/{playerId}               |
 | -------- |---------------|
 | Verb     | POST      |
 | Header   | Content-Type: application/json      |
 |Example Request | { "nickname": "Bob"}|
 |Response Status Code | 200: Updated name  </br> 400: json body or nickname missing </br> 404: player not found|
 |Response Example | { "id": 1,"nickname": "Bob"}|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|nickname |string|yes|Bob|New nickname of the Player.|

---

## Create a new Score
This endpoint can be used to create a new Score. Every Score needs to be related to a player. Scores can be in the range of int values. If no playerId is provided in the request the backend will create a new player with the nickname guest and return the id. Through this id the player could later still change his nickname and keep all scores made as guest.
If a playerId is provided, but there is no corresponding player in the database it will return a 404.

  | Url      | /score/{playerId}               |
 | -------- |---------------|
 | Verb     | PUT      |
 | Header   | Content-Type: application/json      |
 |Example Request | {</br>  "value": 100, </br>  "date": "08.06.2019 15:11", </br> playerId:1 </br> }|
 |Response Status Code | 201: Created new score  </br> 400: json body, value or date missing </br> 404: player not found|
 |Response Example | {</br> "id": 2,</br>  "value": 12354,</br>  "date": "14-07-2019 19:38",</br>  "player":  {</br>"id": 1,</br> "nickname": "guest"</br> }</br> }|

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
|Example Request | { "limit": 10 }|
|Response Status Code | 200: OK |
|Response Example | [</br> {</br> "id": 5,</br> "value": 100000,</br> "date": "14-07-2019 19:38",</br> "player": {</br> "id": 3,</br> "nickname": "Bob"</br> }</br> }</br> ]|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|limit | int |no|10|The number scores you want to get. If not provided you will get all scores ever made. |

---

## Get Highscore List for a specific player

This endpoint will fetch the highest scores made by a given players and return them in an array ordered in descending order starting by the highest score.

| Url      | /score/{playerId}               |
| -------- |---------------|
| Verb     | GET      |
| Header   | No header needed     |
|Example Request |{ "limit": 10 }|
|Response Status Code | 200: OK  </br> 404: no player found |
|Response Example | [</br> {</br> "id": 5,</br> "value": 100000,</br> "date": "14-07-2019 19:38",</br> "player": {</br> "id": 3,</br> "nickname": "Bob"</br> }</br> }</br> ]|

### Request Parameters
|Parameter|Type|Mandatory|Example|Description|
|---------|----|---------|-------|-----------|
|limit | int |no|10|The number scores you want to get. If not provided you will get all scores ever made by this player. |
