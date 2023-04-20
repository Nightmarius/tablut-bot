import styled from "styled-components";
import { Player } from "../../shared/domain/model";

const Title = styled.h1`
  font-size: 3rem;
  text-align: center;
  margin: 2rem 0 2rem 0;
`;

const PlayerContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  flex-wrap: wrap;
  justify-content: space-evenly;
  margin: 3rem 1rem 1rem;
`;

const PlayerChip = styled.div`
  font-weight: bold;
  font-size: 1.5rem;
  background-color: var(--tertiary);
  color: white;
  align-items: center;
  display: inline-flex;
  justify-content: center;
  margin: 1rem 1rem 1rem;
  border-radius: 9999px;
  padding: 0.5rem 1rem;
  animation: pulsate 3s ease-in-out infinite;
`

export default function Lobby({ players }: Props) {
    console.log(players);
    return (
        <>
            <Title>Waiting for players...</Title>

            <PlayerContainer>
                {players.map(player =>
                    <PlayerChip key={player.id.value}>{player.name.value}</PlayerChip>)
                }
            </PlayerContainer>

        </>
    )
}

interface Props {
    players: Player[];
}
