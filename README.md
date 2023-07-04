# Zühlke Coding Challenge 2023

## Setup

If your setup already has some of the required tools installed, please adjust accordingly.

1. Install a new version of IntelliJ
2. Clone the master branch with `git clone https://github.com/Nightmarius/tablut-bot.git`
3. Open the project with IntelliJ and let gradle build the project
4. If prompted, install java (17.0.5)

### Recommended IntelliJ plugins:

- Lombok (usually already installed)
- Spring (usually already installed)

## How to run

1. Edit the name of your bot and add your access token in the properties file
   `bot/src/main/resources/application.properties`
2. Read the rules and familiarize yourself with the game
   https://challenge.zuehlke.com/#/rules
3. Adjust your bots logic by editing the BetterBot class
   Alternatively, you may implement the bot interface in your own class
4. Start your bot by running the BetterBot run configuration
5. Only stop the bot when the tournament is over


## Flow of events

1. On application startup the bot application joins the lobby by sending a `JoinRequest` to `https://challenge.zuehlke.com/api/lobby/join`.
   This is used as a first confirmation that your bot is ready to play.
   The admin will then create a tournament which will determine the games your bot will be assigned to.
   When these games are created the server will publish updates on the game topic.
2. On application startup the application subscribes to the websocket `/topic/game/`. This is where you will receive game updates as a `GameDto` object.
3. Next, the game updates are filtered 
   1. Only running games are considered
   2. Only games where your bot needs to make a move are considered
   3. Only requests which have not been processed yet are considered
4. Your bot calculates its next `GameAction`. The provided `GameDto` already contains a list of possible actions in the `GameState` object, next to other useful information like the current `Board` and list of past moves. 
This will ensure that you can only pick legal moves. 
5. Once you have decided on a move to play, your application sends its `Move` to `https://challenge.zuehlke.com/api/game/{gameId}/play`

## If you want to build the project yourself (e.g. in another language)

We highly recommend to use this template as a starting point for your bot project. \
If you decide to use another language or framework, you can use the following information to build your own bot.

### Requirements

Here are some of the requirements your bot needs to fulfill:
- A bot needs to be able to play multiple games at once
- The bot needs to send the token in the header for every play request
- Make sure you follow the flow of events as described above
- The bot should send a hearbeat every second so that we can track failing bots during the event
- Your bot will have to follow the API contracts defined in https://challenge.zuehlke.com/swagger-ui/index.html
  - You can ignore the admin controller section. This is only used during the event to organize the tournament

## FAQ

### How long does a game last?
Each bot gets a thinking time of two minutes per game. If you use more, you will loose the game. If the game lasts for more than 200 moves it will be declared a draw.

### Am I allowed to use external tools/applications?
Yes, we encourage you to be creative. Just follow the rules of the game and don't abuse the endpoints.

### Will I have to bring my machine to the event?
Yes, please bring a laptop where you can run the code. You don't need to send us your code beforehand.

### What kind of tournament mode are we playing?
This will be announced at the event. Expect to play any and every opponent possible.



