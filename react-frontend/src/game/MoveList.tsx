import { GameDto } from "../shared/domain/model";
import styled from "styled-components";
import Chip, { ChipStyle } from "../shared/ui/chip/Chip";
import { getMoveNotation } from "../shared/domain/helper";

const MoveDiv = styled.div`
    flex: 50%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    float: left;
`;

const Container = styled.div`
    margin-top: 1rem;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
`;

export default function MoveList({ game }: Props) {
    return (
        <Container>
            {game.state.moves.map((move, index) => (
                <MoveDiv key={index}>
                    <div>{index + 1}:</div>
                    <Chip style={index % 2 === 0 ? ChipStyle.BLACK : ChipStyle.WHITE}>
                        {getMoveNotation(move.action)}
                    </Chip>
                </MoveDiv>
            ))}
        </Container>
    );
}

export interface Props {
    game: GameDto;
}
