import { PlayerRoles } from "./GamePage";
import Chip, { ChipStyle } from "../shared/ui/chip/Chip";
import styled from "styled-components";

const ChipContainer = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 2rem;
    font-size: 2rem;
`;

interface Props {
    players: PlayerRoles;
}

export default function PlayerDisplay({ players }: Props) {
    return (
        <>
            <div>
                <RegisteredPlayers participants={players} />
            </div>
        </>
    );
}

export const RegisteredPlayers = ({ participants }: { participants: PlayerRoles }) => {
    return (
        <ChipContainer>
            <Chip style={ChipStyle.BLACK}>{participants.attacker?.value || "Attacker"}</Chip>
            <div>versus</div>
            <Chip style={ChipStyle.WHITE}>{participants.defender?.value || "Attacker"}</Chip>
        </ChipContainer>
    );
};
