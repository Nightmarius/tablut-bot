import {useEffect, useState} from "react";
import remoteService from "../../services/RemoteService";
import {GameDto, Player} from "../domain/model";
import {presentErrorToast} from "../../common/ToastComponent";
import {Participants} from "../../game/GamePage";

export function useGamePolling(gameIdOfInterest: string, interval: number) {

    const [game, setGame] = useState<GameDto | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [participants, setParticipants] = useState<Participants>({attacker:null, defender:null})
    useEffect(() => {
        const intervalId = setInterval(() => {
            remoteService.get<GameDto[]>('/api/lobby/games')
                .then((allGames: GameDto[]) => {

                    const foundGame = allGames
                        .find(
                            (given: GameDto) => given.id.value === Number(gameIdOfInterest)
                        );

                    function extractParticipants(players: Player[]) {
                        let attacker = null
                        let defender = null
                        if (players.length > 0){
                            attacker = players[0];
                        }
                        if(players.length > 1) {
                            defender = players[1]
                        }
                        setParticipants({attacker, defender})}

                    if (foundGame) {
                        setGame(foundGame);
                        extractParticipants(foundGame.players)

                    } else {

                        presentErrorToast(`Game with ID: ${gameIdOfInterest} could not be found`);
                        clearInterval(intervalId);
                    }

                    setIsLoading(false);

                });
        }, interval);
        return () => clearInterval(intervalId);
    }, [interval]);

    return {game, participants, isLoading};
}