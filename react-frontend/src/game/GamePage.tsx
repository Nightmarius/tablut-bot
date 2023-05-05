import { useParams } from "react-router";
import TablutBoard from "./board/TablutBoard";
import styled from "styled-components";
import { useGamePolling } from "../shared/hooks/GameStatePollingHook";
import PlayerDisplay from "./PlayerDisplay";
import { PlayerName } from "../shared/domain/model";
import LoadingPage from "../shared/ui/loading/LoadingPage";
import GameStatusSection from "./GameStatusSection";
import MoveList from "./MoveList";

export interface PlayerRoles {
    attacker: PlayerName | null;
    defender: PlayerName | null;
}

const BoardContainer = styled.div`
    margin-left: 0.5em;
`;

const GameContainer = styled.div`
    margin-left: 0.5em;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    flex-wrap: wrap;
`;

const Title = styled.h1`
    font-size: 3rem;
    text-align: center;
`;

const GameStateContainer = styled.div`
    margin-top: 10vh;
    margin-left: 1em;
    width: 14rem;
    font-size: 1.5rem;
`;

export default function GamePage() {
    const { gameId } = useParams();
    const { game, playerRoles, isLoading } = useGamePolling(1000, gameId);

    if (isLoading) {
        return <LoadingPage />;
    } else if (!game) {
        return <h2>Game does not exist :(</h2>;
    }

    return (
        <>
            <GameContainer>
                <BoardContainer>
                    <Title> Game {gameId} </Title>
                    <PlayerDisplay players={playerRoles} />
                    <TablutBoard board={game.state.board} />
                    <GameStatusSection game={game} />
                </BoardContainer>
                <GameStateContainer>
                    <MoveList game={game} />
                </GameStateContainer>
            </GameContainer>
        </>
    );
}
