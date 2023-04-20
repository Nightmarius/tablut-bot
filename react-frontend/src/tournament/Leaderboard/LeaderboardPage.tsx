import Leaderboard from "./Leaderboard";

export default function LeaderboardPage() {

    const bot1 = {name: "Bot1", points: 4};
    const bot2 = {name: "Bot2", points: 6};
    const bot3 = {name: "TheBestBotEver", points: 10};
    const bot4 = {name: "TheWorse", points: 1};
    const bot5 = {name: "Bot5", points: 2};
    const bot6 = {name: "Bot6", points: 3};
    const bot7 = {name: "Bot7", points: 4};
    const bot8 = {name: "Bot8", points: 5};
    const bot9 = {name: "Bot9", points: 4};
    const bot10 = {name: "Bot10", points: 3};
    const tournamentScores = [bot1, bot2, bot3, bot4, bot5, bot6, bot7, bot8, bot9, bot10];

    return (
        <div>
            <Leaderboard scores={tournamentScores}/>
        </div>
    )
}
