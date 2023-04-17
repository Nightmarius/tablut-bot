import { useParams } from "react-router";

export default function GamePage() {
    let { gameId } = useParams();
    return (
        <div>
            <h1>Game</h1>
            <h2> Game ID: {gameId} </h2>
        </div>
    )
}
