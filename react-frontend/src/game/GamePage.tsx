import {useParams} from "react-router";
import TablutBoard from "./board/TablutBoard";
import styled from "styled-components";
import remoteService from "../services/RemoteService";
import Button from "../shared/button/Button";
import {presentSuccessToast} from "../common/ToastComponent";
import {useGamePolling} from "../shared/hooks/GameStatePollingHook";
import ErrorPage from "../error/ErrorPage";
import PlayerDisplay from "./playerDisplay/PlayerDisplay";
import {Player} from "../shared/domain/model";

export interface Participants{
    attacker: Player | null;
    defender: Player | null;
}
export default function GamePage() {

    let {gameId} = useParams();
    const {cachedGame, cachedParticipants, isLoading} = useGamePolling(gameId!, 1000);

    const startingBoard = [
        [0, 0, 0, 1, 1, 1, 0, 0, 0],
        [0, 0, 0, 0, 1, 0, 0, 0, 0],
        [0, 0, 0, 0, 2, 0, 0, 0, 0],
        [1, 0, 0, 0, 2, 0, 0, 0, 1],
        [1, 1, 2, 2, 3, 2, 2, 1, 1],
        [1, 0, 0, 0, 2, 0, 0, 0, 1],
        [0, 0, 0, 0, 2, 0, 0, 0, 0],
        [0, 0, 0, 0, 1, 0, 0, 0, 0],
        [0, 0, 0, 1, 1, 1, 0, 0, 0],
    ];

    const GameStateContainer = styled.div`
      margin-left: 1.0em;
    `;

    const BoardContainer = styled.div`
      margin-left: 0.5em;
    `;

    const GameContainer = styled.div`
      margin-left: 0.5em;
      display: flex;
    `;

    const handleStartGame = (gameId: string) => {

        remoteService.post(`/api/lobby/game/${gameId}/start`)
            .then(() => {

                presentSuccessToast(`Game "${gameId}" has started`)

            });
    }

    if (!isLoading && !cachedGame) {

        return <ErrorPage/>

    } else if (isLoading) {

        return <></>;

    }

    return (
        <>
            <h2>Game</h2>
            <GameContainer>
                <BoardContainer>
                    <h3> Game ID: {gameId} </h3>
                    <TablutBoard board={startingBoard}/>
                    <Button text={'Start Game'} onClick={() => handleStartGame(gameId!)}/>
                </BoardContainer>
                <GameStateContainer>
                    <h3>Players</h3>
                    <PlayerDisplay participants={cachedParticipants}/>
                </GameStateContainer>
            </GameContainer>

        </>
    )
}
