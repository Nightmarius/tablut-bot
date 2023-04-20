import styled from "styled-components";
import { Player, TournamentDto } from "../../shared/domain/model";
import GameCellContent from "./GameCellContent";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const Table = styled.table`
  border-collapse: collapse;
`;

const Row = styled.tr`
`;

const OuterCell = styled.th`
  /* does not have a border */
  padding: 1rem;
`;

const Cell = styled.td`
  width: 4rem;
  height: 4rem;
  border: 1px solid var(--quinary);
  text-align: center;

  &:first-child {
    font-weight: bold;
  }
`;

export default function TournamentTable({ tournament }: Props) {

    const getGame = (player1: Player, player2: Player) => {
        return tournament.state.games.find(game => {
            const player1InGame = game.players.indexOf(player1) === 0;
            const player2InGame = game.players.indexOf(player2) === 1;
            return player1InGame && player2InGame;
        });
    }

    return (
        <Container>
            <Table>
                <thead>
                <Row>
                    <OuterCell></OuterCell>
                    {tournament.players.map((player, index) => (
                        <OuterCell key={index}>{player.name.value}</OuterCell>
                    ))}
                </Row>
                </thead>
                <tbody>
                {tournament.players.map((player1, index1) => (
                    <Row key={index1}>
                        <OuterCell>{player1.name.value}</OuterCell>
                        {tournament.players.map((player2, index2) => (
                            <Cell key={index2}>
                                <GameCellContent game={getGame(player1, player2)}/>
                            </Cell>
                        ))}
                    </Row>
                ))}
                <Row>
                    <OuterCell>Total Points</OuterCell>
                    {tournament.players.map((player, index) => (
                        <OuterCell key={index}>
                            {tournament.scores.find(score => score.playerId.value === player.id.value)?.score}
                        </OuterCell>
                    ))}
                </Row>
                </tbody>
            </Table>
        </Container>
    );
}

export interface Props {
    tournament: TournamentDto;
}


