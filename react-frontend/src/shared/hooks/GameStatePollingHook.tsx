import {useEffect, useState} from "react";
import remoteService from "../../services/RemoteService";
import {GameDto, Player} from "../domain/model";
import {presentErrorToast} from "../../common/ToastComponent";

export function useGamePolling(gameId: string, interval: number) {

    const [game, setGame] = useState<GameDto | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const playerRoles = game ? mapPlayersToRole(game?.players) : defaultPlayerRoles


    useEffect(() => {
        const intervalId = setInterval(() => {
            remoteService.get<GameDto>(`/api/game/${gameId}`)
                .then((foundGame: GameDto) => {

                    if (foundGame) {
                        setGame((prevGame) => {
                            if (prevGame && !hasGameStateChange(prevGame, foundGame)) {
                                return prevGame
                            }
                            return foundGame;
                        })

                    } else {
                        presentErrorToast(`Game with ID: ${gameId} could not be found`);
                        clearInterval(intervalId);
                    }

                    setIsLoading(false);

                });
        }, interval);
        return () => clearInterval(intervalId);
    }, [gameId, interval]);


    return {game, playerRoles, isLoading};
}

export function hasGameStateChange(oldGame: GameDto, newGame: GameDto) {
    if (newGame.status !== oldGame.status) {
        return true
    }
    if (newGame.players.length !== oldGame.players.length) {
        return true
    }
    return oldGame.state.moves.length < newGame.state.moves.length;

}

const mapPlayersToRole = (players: Player[]) => {
    let attacker = null
    let defender = null

    if (players.length > 0) {
        attacker = players[0];
    }
    if (players.length > 1) {
        defender = players[1]
    }
    return ({attacker, defender})
}

const defaultPlayerRoles = {attacker: null, defender: null}
