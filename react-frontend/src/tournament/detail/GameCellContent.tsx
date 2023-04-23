import { GameDto } from "../../shared/domain/model";
import styled from "styled-components";
import { useNavigate } from "react-router";

const MatchUpContainer = styled.span`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
`;

const NoGame = styled.span`
  opacity: 0.5;
`;

const GameContainer = styled.span`
  /* TODO */

  &:hover {
    font-weight: bold;
    cursor: pointer;
  }
`;

export default function GameCellContent({ game }: Props) {

    let navigate = useNavigate();

    const player1 = game?.players[0];
    const player2 = game?.players[1];

    const navigateToGame = () => {
        navigate(`/game/${game?.id.value}`);
    }

    // TODO ZTOPCHA-21: improve the content here
    return (
        <MatchUpContainer>
            {
                game && player1 && player2 ? (
                    <GameContainer onClick={navigateToGame}>
                        {player1.name.value} vs {player2.name.value}
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
