import styled from "styled-components";
import {PlayerRoles} from "../GamePage";

interface Props {
    players: PlayerRoles;
}

export default function PlayerDisplay({players}: Props) {
    return (
        <>
            <div><RegisteredPlayers participants={players}/></div>
        </>
    );

}

export const RegisteredPlayer = styled.div<{ color: string }>`
  color: ${(props) => props.color}
`;

export const RegisteredPlayers = ({participants}: { participants: PlayerRoles }) => {

    const attackerDisplayText =
        (participants.attacker)
            ? `Attacker: ${participants.attacker.name.value}`
            : 'Attacker: -';

    const defenderDisplayText =
        (participants.defender)
            ? `Defender: ${participants.defender.name.value}`
            : 'Defender: -';

    return (
        <>
            <RegisteredPlayer color={'var(--secondary)'}>{attackerDisplayText}</RegisteredPlayer>
            <RegisteredPlayer color={'var(--tertiary)'}>{defenderDisplayText}</RegisteredPlayer>
        </>
    )
}
