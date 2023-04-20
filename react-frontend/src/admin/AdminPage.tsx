import {useState} from "react";
import remoteService from "../services/RemoteService";
import {useNavigate} from "react-router";

export function AdminPage() {

    interface GameId {
        value: number;
    }

    const [gameIds, setGameIds] = useState(new Array<number>);
    const handleCreateGame = () => {

        remoteService.post<GameId>('/api/lobby/game')
            .then((response: GameId) => {

                const idToAdd: number = response.value;
                setGameIds((listToAdd: number[]) => [...listToAdd, idToAdd]);

            })
    };

    return (
        <>
            <button onClick={handleCreateGame}>New Game</button>
            <GameList gameIds={gameIds}/>
        </>
    );
}

export interface GameListProps {
    gameIds: number[]
}

export function GameList({gameIds}: GameListProps) {

    const navigate = useNavigate();

    const gameIdListElements = gameIds
        .map((gameId: number) =>

            <a key={gameId} onClick={() => navigate(`/game/${gameId}`)}>
                <li>Game created with ID:
                    {gameId}
                </li>
            </a>
        );

    return (
        <ul>{gameIdListElements}</ul>
    );
}