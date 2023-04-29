import TournamentTable from "./TournamentTable";
import { Player, PlayerId, PlayerName, TournamentDto, TournamentStatus } from "../../shared/domain/model";
import { useParams } from "react-router";
import remoteService from "../../services/RemoteService";
import { useEffect, useState } from "react";
import LoadingPage from "../../shared/ui/loading/LoadingPage";
import Leaderboard from "../Leaderboard/Leaderboard";
import Lobby from "./Lobby";

function getPlayerName(players: Player[], playerId: PlayerId) {
    const player = players.find((player) => player.id.value === playerId.value);
    return player ? player.name : "Unknown Player";
}

function mapScores(tournament: TournamentDto) {
    return tournament.scores.map((score) => {
        return {
            name: getPlayerName(tournament.players, score.playerId) as PlayerName,
            points: score.score,
        };
    });
}

export default function TournamentDetailPage() {
    let { tournamentId } = useParams();

    let [tournament, setTournament] = useState<TournamentDto>();

    useEffect(() => {
        const fetchTournament = () => {
            remoteService.get<TournamentDto>("/api/tournament/" + tournamentId).then((response: TournamentDto) => {
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
        return <LoadingPage />;
    }

    // TODO ZTOPCHA-21: improve these messages
    if (!tournament) {
        return <div>This tournament does not exist</div>;
    }

    if (tournament.status === TournamentStatus.NOT_STARTED) {
        return <Lobby players={tournament.players} />;
    }

    if (tournament.status === TournamentStatus.DELETED) {
        return <div>Deleted</div>;
    }

    if (tournament.status === TournamentStatus.FINISHED) {
        return (
            <>
                <Leaderboard scores={mapScores(tournament)} />
                <TournamentTable tournament={tournament} />
            </>
        );
    }

    return <TournamentTable tournament={tournament} />;
}
