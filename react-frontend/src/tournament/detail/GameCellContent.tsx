import { GameDto, GameStatus, Player } from "../../shared/domain/model";
import styled from "styled-components";
import Chip, { ChipStyle } from "../../shared/ui/chip/Chip";
import { getPlayerForNextTurn, getWinner } from "../../shared/domain/helper";
import Button, { ButtonStyle } from "../../shared/ui/button/Button";
import remoteService from "../../services/RemoteService";
import { useNavigate } from "react-router";

const MatchUpContainer = styled.span`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
`;

const GameIdTitle = styled.div`
  font-size: 2rem;


  &:hover {
    cursor: pointer;
    font-weight: bold;
  }
`;

const NoGame = styled.span`
  font-size: 1rem;
  opacity: 0.5;
`;

const GameContainer = styled.span`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;

`;

export default function GameCellContent({ game }: Props) {

    const navigate = useNavigate();

    const player1 = game?.players[0];
    const player2 = game?.players[1];
    const playerForNextTurn = getPlayerForNextTurn(game);
    const winner = getWinner(game);

    const getChipStyleForPlayer = (player: Player): ChipStyle => {
        return player === player1 ? ChipStyle.WHITE : ChipStyle.BLACK;
    }

    function startGame() {
        remoteService.post(`/api/lobby/game/${game?.id.value}/start`, {});
    }

    function navigateToGame() {
        navigate(`/game/${game?.id.value}`)
    }

    // TODO ZTOPCHA-21: improve the content here
    return (
        <MatchUpContainer>
            {
                game && player1 && player2 ? (
                    <GameContainer>
                        <GameIdTitle onClick={navigateToGame}>Game: {game.id.value}</GameIdTitle>

                        {game.status === GameStatus.NOT_STARTED &&
                            <Button text="Start game" onClick={startGame} style={ButtonStyle.PURPLE}></Button>
                        }
                        {game.status === GameStatus.IN_PROGRESS &&
                            <div>
                                Turn {game.state.moves.length + 1}
                                {playerForNextTurn &&
                                    <div> Next turn:
                                        <Chip text={playerForNextTurn.name.value}
                                              style={getChipStyleForPlayer(playerForNextTurn)}></Chip>
                                    </div>
                                }
                            </div>
                        }
                        {game.status === GameStatus.DELETED &&
                            <div>Deleted game</div>
                        }
                        {game.status === GameStatus.FINISHED &&
                            <div>
                                {game.state.moves.length} moves played
                                {winner ?
                                    <div> Winner:
                                        <Chip text={winner.name.value}
                                              style={getChipStyleForPlayer(winner)}></Chip>
                                    </div> :
                                    <div>Draw</div>
                                }
                            </div>
                        }
                    </GameContainer>
                ) : (
                    <NoGame>No matchup</NoGame>)
            }
        </MatchUpContainer>
    );
}

export interface Props {
    game?: GameDto;
}
