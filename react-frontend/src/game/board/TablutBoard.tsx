import styled from "styled-components";

type TablutBoardProps = {
    board: number[][];
};

export default function TablutBoard({board}: TablutBoardProps) {
    const Square = styled.div<{ color: string; size: string }>`
    width: ${(props) => props.size};
    height: ${(props) => props.size};
    background-color: ${(props) => props.color};
    border: 1px solid black;
  `;
    const Row = styled.div`
    display: flex;
  `;
    const TablutBoardContainer = styled.div`
    flex-wrap: wrap;
    margin: 0 auto;
    padding: 10px;
  `;

    enum Field {
        ATTACKER = 1,
        DEFENDER = 2,
        KING = 3
    }

    const renderSquare = (squareContent: number, rowIndex: number, colIndex: number, size: string) => {
        let color;
        switch (squareContent) {
            case Field.ATTACKER:
                color = "#a9a5a5";
                break;
            case Field.DEFENDER:
                color = "#fff";
                break;
            case Field.KING:
                color = "#af0";
                break;
            default:
                color = "#a47449";
        }
        if(rowIndex == 4 && colIndex == 4) {
            color = "#ffd700";
        }
        return <Square key={`${rowIndex}${colIndex}`} color={color} size={size}/>;
    };

    return (
        <TablutBoardContainer>
            {board.map((row, rowIndex) => (
                <Row key={rowIndex}>
                    {row.map((square, colIndex) =>
                        renderSquare(
                            row[colIndex],
                            rowIndex,
                            colIndex,
                            window.innerWidth > 600 ? "50px" : "30px"
                        )
                    )}
                </Row>
            ))}
        </TablutBoardContainer>
    );
}