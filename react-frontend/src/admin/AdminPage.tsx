import { useEffect, useState } from "react";
import remoteService from "../services/RemoteService";
import { useNavigate } from "react-router";
import { BotDto } from "../shared/domain/model";

export function AdminPage() {
    interface GameId {
        value: number;
    }

    useEffect(() => {
        getBots();
    }, []);

    const [gameIds, setGameIds] = useState(new Array<number>());
    const [bots, setBots] = useState<BotDto[]>(new Array<BotDto>());
    const [name, setName] = useState("");
    const [warning, setWarning] = useState("");
    const handleCreateGame = () => {
        remoteService.post<GameId>("/api/game").then((response: GameId) => {
            const idToAdd: number = response.value;
            setGameIds((listToAdd: number[]) => [...listToAdd, idToAdd]);
        });
    };

    function generateToken() {
        remoteService
            .post("/admin/bot/generate", {
                value: name,
            })
            .then(getBots);
    }

    function getBots() {
        remoteService.get<BotDto[]>("/admin/bots").then((response: BotDto[]) => {
            setBots(response);
        });
    }

    function handleNameChange(name: string) {
        setName(name);
        remoteService.get<BotDto>("/admin/bot/" + name).then((bot: BotDto) => {
            if (bot.name !== undefined) {
                setWarning(name + " already exists try using " + name + "1 instead.");
            } else {
                setWarning("");
            }
        });
    }

    return (
        <>
            <button onClick={handleCreateGame}>New Game</button>
            <GameList gameIds={gameIds} />
            {warning != null && <p>{warning}</p>}
            <input type="text" placeholder="Name" value={name} onChange={(e) => handleNameChange(e.target.value)} />
            <button onClick={generateToken}>Generate Access token</button>
            {bots.map((bot) => (
                <button onClick={() => navigator.clipboard.writeText(bot.token.value)}>
                    <li>
                        {bot.name.value}: {bot.token.value}
                    </li>
                </button>
            ))}
        </>
    );
}

export interface GameListProps {
    gameIds: number[];
}

export function GameList({ gameIds }: GameListProps) {
    const navigate = useNavigate();

    const gameIdListElements = gameIds.map((gameId: number) => (
        <button key={gameId} onClick={() => navigate(`/game/${gameId}`)}>
            <li>
                Game created with ID:
                {gameId}
            </li>
        </button>
    ));

    return (
        <ul>
            {gameIdListElements.map((gameIdListElement) => (
                <div>{gameIdListElement}</div>
            ))}
        </ul>
    );
}
