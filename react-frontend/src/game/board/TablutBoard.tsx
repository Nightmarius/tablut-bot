import styled from "styled-components";
import {KingField, StandardField} from "./StandardField";
import {Board, Field, FieldState} from "../../shared/domain/model";

type TablutBoardProps = {
    board: Board;
};

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
      flex-direction: row;
      justify-content: space-evenly;
      width: 100%;
      margin-left: 0.5em;
    `;
const TablutBoardContainer = styled.div`
      flex-wrap: wrap;
      padding: 10px;
      width: fit-content;
    `;

export default function TablutBoard({board}: TablutBoardProps) {

    function columnCoordinateOf(colIndex: number) {

        return (
            String.fromCharCode(65 + colIndex)
        );
    }
    console.log(board)
    return (
        <TablutBoardContainer>
            <ColumnIndexContainer>
                {board.fields[0].map((_, colIndex) => (
                    <ColIndex>
                        {columnCoordinateOf(colIndex)}
                    </ColIndex>
                ))}
            </ColumnIndexContainer>
            {board.fields.map((row, rowIndex) => (
                <TablutRow>
                    <RowIndex>{rowIndex + 1}</RowIndex>
                    <Row key={rowIndex}>
                        {
                            row.map((field, colIndex) => {
                                if (rowIndex === 4 && colIndex === 4) {
                                    return <KingField fieldValue={field.state} key={`${rowIndex}${colIndex}`}/>
                                } else {
                                    return <StandardField fieldValue={field.state} key={`${rowIndex}${colIndex}`}/>
                                }
                            })
                        }
                    </Row>
                </TablutRow>
            ))}
        </TablutBoardContainer>
    );
}

export function createBoard(b: number[][]): Board {
    const board: Board = {fields: []};
    for (let y = 0; y < b.length; y++) {
        const row: Field[] = [];
        for (let x = 0; x < b[y].length; x++) {
            row.push({state: Object.values(FieldState)[b[y][x]]} as Field);
        }
        board.fields.push(row);
    }
    return board;
}
