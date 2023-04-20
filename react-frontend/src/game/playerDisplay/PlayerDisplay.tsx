import styled from "styled-components";
import {Participants} from "../GamePage";


export default function PlayerDisplay({participants}: {participants: Participants}) {

    return (
        <>
            <div><RegisteredPlayers participants={participants}></RegisteredPlayers></div>
        </>
    );

}

export const RegisteredPlayer = styled.div<{ color: string }>`
  color: ${(props) => props.color}
`;

export const RegisteredPlayers = ({participants}: { participants: Participants }) => {

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