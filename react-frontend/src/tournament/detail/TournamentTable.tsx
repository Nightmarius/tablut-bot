import styled from "styled-components";
import { Player, TournamentDto } from "../../shared/domain/model";
import GameCellContent from "./GameCellContent";
import Chip, { ChipStyle } from "../../shared/ui/chip/Chip";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const Table = styled.table`
  border-collapse: collapse;
`;

const Row = styled.tr`
`;

const OuterCell = styled.th`
  /* does not have a border */
  padding: 1rem;
  font-size: 2rem;
`;

const Cell = styled.td`
  width: 20rem;
  height: 10rem;
  border: 1px solid var(--quinary);
  text-align: center;

  &:first-child {
    font-weight: bold;
  }
`;

export default function TournamentTable({ tournament }: Props) {

    const getGame = (player1: Player, player2: Player) => {
        return tournament.state.games.find(game => {
            if (game.players.length < 2) return false;
            const player1InGame = game.players.at(0)?.id.value === player1.id.value;
            const player2InGame = game.players.at(1)?.id.value === player2.id.value;
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
                        <OuterCell key={index}>
                            <Chip style={ChipStyle.WHITE}>{player.name.value}</Chip>
                        </OuterCell>
                    ))}
                </Row>
                </thead>
                <tbody>
                {tournament.players.map((player1, index1) => (
                    <Row key={index1}>
                        <OuterCell><Chip style={ChipStyle.BLACK}>{player1.name.value}</Chip></OuterCell>
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


