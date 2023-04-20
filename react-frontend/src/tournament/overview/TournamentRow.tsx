import { TournamentDto, TournamentStatus } from "../../shared/domain/model";
import LinkButton from "../../shared/button/LinkButton";

import styled from "styled-components";
import { Style } from "../../shared/button/Button";

const StyledRow = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 3rem;
  width: 100%;
  padding-bottom: 4px;
  padding-top: 4px;
  border-bottom: 1px solid var(--quinary);
`

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
                return "Ready to start";
        }
    }

    return (
        <StyledRow>
            <Text>{tournament.id.value}</Text>
            <Text>{getStatus(tournament.status)}</Text>
            <Text>{tournament.players.length} Players</Text>
            <LinkButton style={Style.PURPLE} text="Spectate" linkTarget={"/tournament/" + tournament.id.value}/>
        </StyledRow>
    )
}

export interface Props {
    tournament: TournamentDto;
}
