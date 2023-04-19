import styled from "styled-components";
import {Field} from "./Field";

type TablutBoardProps = {
    board: number[][];
};

export default function TablutBoard({board}: TablutBoardProps) {
    const Row = styled.div`
    display: flex;
    `;
    const TablutRow = styled.div`
    display: flex;
    margin: 0 auto;
    `;
    const RowIndex = styled.span`
    margin: 0.5em;
    align-self: center;
    `;
    const ColIndex = styled.span`
    margin: 0.5em;
    align-self: center;
    `;
    const ColumnIndexContainer = styled.div`
    display: flex;
    margin: 0 auto;
    flex-direction: row;
    justify-content: evenly;
    width: 100%;
    `;
    const TablutBoardContainer = styled.div`
    flex-wrap: wrap;
    margin: 0 auto;
    padding: 10px;
    width: auto;
    `;

    return (
        <TablutBoardContainer>
            <ColumnIndexContainer>
                {board[0].map((_, colIndex) => (
                    <ColIndex>
                        {String.fromCharCode(65 + colIndex)}
                    </ColIndex>
                ))}
            </ColumnIndexContainer>
            {board.map((row, rowIndex) => (
                <TablutRow>
                    <RowIndex>{rowIndex + 1}</RowIndex>
                    <Row key={rowIndex}>
                        {row.map((field, colIndex) =>
                            <Field fieldValue={field} key={`${rowIndex}${colIndex}`}/>
                        )}
                    </Row>
                </TablutRow>
            ))}
        </TablutBoardContainer>
    );
}