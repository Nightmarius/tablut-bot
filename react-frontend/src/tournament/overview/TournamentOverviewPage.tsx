import { useEffect, useState } from "react";
import { TournamentDto } from "../../shared/domain/model";
import remoteService from "../../services/RemoteService";
import LoadingPage from "../../shared/ui/loading/LoadingPage";
import NoTournamentsPage from "./NoTournamentsPage";
import Button from "../../shared/ui/button/Button";
import styled from "styled-components";
import TournamentRow from "./TournamentRow";

const TournamentContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    padding-left: 1rem;
    margin-right: 1rem;
    margin-bottom: 1rem;
`;

const Title = styled.h1`
    font-weight: bold;
    font-size: 3rem;
    text-align: center;
`;

export default function TournamentOverviewPage() {
    let [tournaments, setTournaments] = useState<TournamentDto[] | undefined>(undefined);

    useEffect(() => {
        const fetchTournaments = () => {
            remoteService.get<TournamentDto[]>("/api/tournaments").then((response: TournamentDto[]) => {
                setTournaments(response);
            });
        };

        fetchTournaments();

        const interval = setInterval(() => {
            fetchTournaments();
        }, 1000);

        return () => clearInterval(interval);
    }, []);

    function createTournament() {
        remoteService.post("/api/tournament", {});
    }

    if (tournaments === undefined) {
        return <LoadingPage />;
    }

    if (tournaments.length === 0) {
        return <NoTournamentsPage />;
    }

    // TODO ZTOPCHA-14: Hide button if user is not admin
    return (
        <>
            <TournamentContainer>
                <Title>Tournaments</Title>
                {tournaments.map((tournament: TournamentDto) => (
                    <TournamentRow key={tournament.id.value} tournament={tournament} />
                ))}
            </TournamentContainer>
            <Button onClick={createTournament}>Create a new tournament</Button>
        </>
    );
}
