import { TournamentDto, TournamentStatus } from "../../shared/domain/model";
import LinkButton from "../../shared/ui/button/LinkButton";

import styled from "styled-components";
import Button, { ButtonStyle } from "../../shared/ui/button/Button";
import remoteService from "../../services/RemoteService";

const StyledRow = styled.div`
    display: grid;
    grid-auto-flow: column;
    grid-auto-columns: 1fr;
    height: 4%;
    width: 100%;
    padding-bottom: 4px;
    padding-top: 4px;
    border-bottom: 1px solid var(--quinary);
`;

const Column = styled.div`
    padding-right: 1px;
`;

const Text = styled.p`
    font-size: 1.5rem;
    margin: 0;
`;

export default function TournamentRow({ tournament }: Props) {
    function getStatus(tournamentStatus: TournamentStatus) {
        switch (tournamentStatus) {
            case TournamentStatus.IN_PROGRESS:
                return "Tournament is in progress";
            case TournamentStatus.DELETED:
                return "Deleted";
            case TournamentStatus.FINISHED:
                return "Finished";
            case TournamentStatus.NOT_STARTED:
                return "Waiting for players";
        }
    }

    function startTournament(): void {
        remoteService.post("/api/tournament/" + tournament.id.value + "/start", {});
    }

    // TODO ZTOPCHA-21: Disable start button if tournament is not ready to start
    // TODO ZTOPCHA-14: Hide start button if user is not admin
    return (
        <StyledRow>
            <Column>
                <Text>{tournament.id.value}</Text>
            </Column>
            <Column>
                <Text>{getStatus(tournament.status)}</Text>
            </Column>
            <Column>
                <Text>{tournament.players.length} Players</Text>
            </Column>
            <Column>
                {tournament.status === TournamentStatus.NOT_STARTED && tournament.players.length > 1 ? (
                    <Button style={ButtonStyle.PURPLE} onClick={startTournament}>
                        Start
                    </Button>
                ) : (
                    <div></div>
                )}
            </Column>
            <Column>
                <LinkButton style={ButtonStyle.PURPLE} linkTarget={"/tournament/" + tournament.id.value}>
                    Spectate
                </LinkButton>
            </Column>
        </StyledRow>
    );
}

export interface Props {
    tournament: TournamentDto;
}
