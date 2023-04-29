import styled from "styled-components";
import Button from "../../shared/ui/button/Button";
import remoteService from "../../services/RemoteService";

const Title = styled.h1`
    font-weight: bold;
    font-size: 3rem;
    text-align: center;
`;

const Description = styled.p`
    font-size: 1.5rem;
    text-align: center;
`;

const CenterSpan = styled.span`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
`;

export default function NoTournamentsPage() {
    function handleCreateTournament() {
        remoteService.post("/api/tournament", {});
    }

    // TODO ZTOPCHA-14: Hide button if user is not admin
    return (
        <div>
            <Title>Coming soon!</Title>
            <Description>There are no tournaments at the moment. Come back later at the day of the event!</Description>
            <CenterSpan>
                <Button onClick={handleCreateTournament}>Create a new tournament</Button>
            </CenterSpan>
        </div>
    );
}
