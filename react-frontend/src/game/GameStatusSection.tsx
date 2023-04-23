import { GameDto, GameStatus, Player } from "../shared/domain/model";
import Button from "../shared/ui/button/Button";
import Chip, { ChipStyle } from "../shared/ui/chip/Chip";
import { getWinner } from "../shared/domain/helper";
import remoteService from "../services/RemoteService";
import { presentSuccessToast } from "../common/ToastComponent";
import styled from "styled-components";


const Section = styled.div`
  font-size: 1.5rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
`;

const WinnerSection = styled.div`
  font-size: 3rem;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 1rem;
`;


export default function GameStatusSection({ game }: Props) {

    const winner = getWinner(game);

    const getChipStyleForPlayer = (player: Player): ChipStyle => {
        return player === game?.players[0] ? ChipStyle.BLACK : ChipStyle.WHITE;
    }

    const handleStartGame = () => {

        remoteService.post(`/api/lobby/game/${game.id.value}/start`)
            .then(() => {
                presentSuccessToast(`Game ${game.id.value} has started`)
            });
    }

    // TODO ZTOPCHA-14: Hide start game button if user is not admin
    return (
        <div>
            {game.status === GameStatus.NOT_STARTED &&
                <Section>
                    <div>Game has not started yet</div>
                    <Button onClick={handleStartGame}>Start game</Button>
                </Section>

            }
            {game.status === GameStatus.IN_PROGRESS &&
                <Section>
                    Turn {game.state.moves.length + 1}
                </Section>
            }
            {game.status === GameStatus.DELETED &&
                <Section>Deleted game</Section>
            }
            {game.status === GameStatus.FINISHED &&
                <Section>
                    <div>{game.state.moves.length} moves played</div>

                    {winner ?
                        <WinnerSection>
                            <div>Winner:</div>
                            <Chip style={getChipStyleForPlayer(winner)}>{winner.name.value}</Chip>
                        </WinnerSection> :
                        <div>Draw</div>
                    }
                </Section>
            }
        </div>
    )
}

export interface Props {
    game: GameDto;
}
