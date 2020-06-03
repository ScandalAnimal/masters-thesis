# Intelligent Manager of Fantasy Premier League Game - User Manual

## Contents
This is a list of all the contents of this directory:

- code-sources/backend-application: source code of the server
- code-sources/frontend-application: source code of the web application
- thesis-sources/: LaTeX source code files
- thesis.pdf: the text of the thesis
- README.md: this document

## About
This application can be used to help and support user while playing the [Fantasy Premier League](https://fantasy.premierleague.com/) official application.
User selects a team of 15 players every game round, with some game restrictions and maximum budget available and then he waits for the outcome of the real football matches.
Every player in the game receives points based on various action on the pitch, such as scoring a goal, getting a yellow card and many more.
The goal of the game is to select a squad, that will get as much points as possible. To see more detailed rules of the game, see [FPL Rules](https://fantasy.premierleague.com/help/rules).

This is where this application comes in hand. You can use it to get predictions about how will players perform in the next few rounds.
With these predictions, you can optimize your team and therefore get more points in the actual game. This application should help improve user's overall score in the game.

## How to use this application
You need to have a working Fantasy Premier League account to use this application properly.
For testing purposes, this account can be used:

    email: mvasilisin@gmail.com
    password: 0p9J2O75jRkWjVW
    
Both backend and frontend application must be running. How to do that, see "Requirements" section at the end of this document. 
After logging into the application, you have an option to see your current team.    
You can manipulate players in the team, see their details and historical statistics.
There are two options how to improve the current squad:

1. Manual - you can click on Transfer Market and browse for new players there and you can see their predicted points, when you add them to your squad.
You can also see all player predictions, when you select the option in the main menu.
2. Automatic - you can go to the "Optimize" section in the main menu, and select the optimization parameters.
After that, you should be presented with squad improvement suggestions. When you select one of the options, you can see the changed squad in the squad screen.

When you are happy with your changed squad, and the potential point improvement is satisfactory, you can log into the Fantasy Premier League application and make transfers and changes there.

There is also an option to use the application without the Fantasy Premier League account, you can select manual option on the login screen. You will then have to manually select all the players in your squad.

----
The application should be used before each game round starts, to see updated predictions for that particular round.

## Main features

- Squad view: you can see your squad and make the changes that you want.
- Transfer market: you can add players to your squad, see their prices and statistics.
- Points predictions: you can see predicted points for your current squad. These predictions are provided by the trained neural network that is part of the application.
- Optimizing the squad: you can select the desired options and my prediction algorithm will provide few options on how to improve the squad.
- Statistics: you can see all past player statistics
- Injuries and suspensions: you can see, which players will be unavailable for the next round.
- After every round is finished, the predictions will be updated with the new data.


## Requirements

To run this application, you will need to install and run both backend and frontend part of the application on the same local machine. Application is not deployed online yet.
These backend and frontend parts are located in code/sources folder, and they both have their own README file with installation instructions.
