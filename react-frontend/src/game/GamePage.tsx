import {useParams} from "react-router";
import TablutBoard from "./board/TablutBoard";
import styled from "styled-components";
import remoteService from "../services/RemoteService";
import Button, {Style} from "../shared/button/Button";
import {presentSuccessToast} from "../common/ToastComponent";
import {useGamePolling} from "../shared/hooks/GameStatePollingHook";
import PlayerDisplay from "./playerDisplay/PlayerDisplay";
import {Player} from "../shared/domain/model";
import LoadingPage from "../shared/loading/LoadingPage";

export interface PlayerRoles {
    attacker: Player | null;
    defender: Player | null;
}

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

export default function GamePage() {

    let {gameId} = useParams();
    const {game, playerRoles, isLoading} = useGamePolling(gameId!, 1000);

    if (isLoading) {
        return <LoadingPage/>;
    } else if (!isLoading && !game){
        return <h2>Game does not exist :(</h2>
    }

    return (
        <>
            <h2>Game</h2>
            <GameContainer>
                <BoardContainer>
                    <h3> Game ID: {gameId} </h3>
                    <TablutBoard board={game?.state.board!}/>
                    <Button text={'Start Game'} onClick={() => handleStartGame(gameId!)} style={Style.PURPLE}/>
                </BoardContainer>
                <GameStateContainer>
                    <h3>Players</h3>
                    <PlayerDisplay players={playerRoles}/>
                </GameStateContainer>
            </GameContainer>

        </>
    )
}

const handleStartGame = (gameId: string) => {

    remoteService.post(`/api/lobby/game/${gameId}/start`)
        .then(() => {

            presentSuccessToast(`Game "${gameId}" has started`)

        });
}
