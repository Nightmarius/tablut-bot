import { useParams } from "react-router";

export default function GamePage() {
    let { gameId } = useParams();
    return (
        <div>
            <h2>Game</h2>
            <h3> Game ID: {gameId} </h3>
        </div>
    )
}
