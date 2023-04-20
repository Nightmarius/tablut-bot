import TournamentTable from "./TournamentTable";
import { TournamentDto, TournamentStatus } from "../../shared/domain/model";
import { useParams } from "react-router";
import remoteService from "../../services/RemoteService";
import { useEffect, useState } from "react";
import LoadingPage from "../../shared/loading/LoadingPage";
import Lobby from "./Lobby";

export default function TournamentDetailPage() {

    let { tournamentId } = useParams();

    let [tournament, setTournament] = useState<TournamentDto>();

    useEffect(() => {
        const fetchTournament = () => {
            remoteService.get<TournamentDto>("/api/lobby/tournament/" + tournamentId)
                .then((response: TournamentDto) => {
                    setTournament(response);
                });
        };

        // Initial fetch
        fetchTournament();

        // Polling
        const intervalId = setInterval(() => {
            fetchTournament();
        }, 1000);

        // Cleanup
        return () => {
            clearInterval(intervalId);
        };
    }, [tournamentId]);

    if (tournament === undefined) {
        return <LoadingPage/>;
    }

    // TODO ZTOPCHA-21: improve these messages
    if (!tournament) {
        return <div>This tournament does not exist</div>;
    }

    if (tournament.status === TournamentStatus.NOT_STARTED) {
        return <Lobby players={tournament.players}/>
    }

    if (tournament.status === TournamentStatus.DELETED) {
        return <div>Deleted</div>;
    }

    if (tournament.status === TournamentStatus.FINISHED) {
        return <div>Finished</div>;
    }


    return <TournamentTable tournament={tournament}/>;

}
