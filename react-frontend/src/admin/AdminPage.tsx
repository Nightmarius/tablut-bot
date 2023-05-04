import { useEffect, useState } from "react";
import remoteService from "../services/RemoteService";
import { useNavigate } from "react-router";
import { BotDto } from "../shared/domain/model";
import Button from "../shared/ui/button/Button";
import styled from "styled-components";
import Input from "../shared/ui/input/input";

const StyledRow = styled.div`
    display: grid;
    grid-auto-flow: column;
    grid-template-columns: 1fr auto;
    height: 4%;
    width: 100%;
    padding-bottom: 4px;
    padding-top: 4px;
    border-bottom: 1px solid var(--quinary);
`;

const Title = styled.h2`
    font-size: 2rem;
    background: linear-gradient(90deg, #985b9c, #a6e0fe);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
`;

const StyledButton = styled.button`
    width: 50px;
    margin-left: 10px;
`;

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
            <Title>Access Tokens</Title>
            {bots.map((bot) => (
                <StyledRow>
                    <div>{bot.name.value}</div>
                    <div>{bot.token.value}</div>
                    <StyledButton onClick={() => navigator.clipboard.writeText(bot.token.value)}>Copy</StyledButton>
                </StyledRow>
            ))}
            {warning != null && <p>{warning}</p>}
            <Input type="text" placeholder="Name" value={name} onChange={(e) => handleNameChange(e.target.value)} />
            <Button onClick={generateToken}>Generate Access token</Button>
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
