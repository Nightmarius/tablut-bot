import styled from "styled-components";
import { KingField, PixelOffset, StandardField } from "./StandardField";
import { Board, GameAction } from "../../shared/domain/model";
import { useEffect, useRef, useState } from "react";

type TablutBoardProps = {
    board: Board;
    animation?: BoardAnimation;
};

export interface BoardAnimation {
    action: GameAction;
}

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

export default function TablutBoard({ board, animation }: TablutBoardProps) {
    const [fieldPositions, setFieldPositions] = useState<{ [key: string]: { x: number; y: number } }>({});

    function columnCoordinateOf(colIndex: number) {
        return String.fromCharCode(65 + colIndex);
    }

    const boardRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const calculateFieldPositions = () => {
            const newFieldPositions: { [key: string]: { x: number; y: number } } = {};
            const boardContainer = boardRef.current;
            if (!boardContainer) {
                return;
            }
            const fieldElements = boardContainer.querySelectorAll("[id^='field-']");
            fieldElements.forEach((fieldElement) => {
                const { top, left } = fieldElement.getBoundingClientRect();
                const fieldId = fieldElement.getAttribute("id");
                if (fieldId) {
                    newFieldPositions[fieldId] = { x: left, y: top };
                }
            });
            setFieldPositions(newFieldPositions);
        };
        calculateFieldPositions();
        window.addEventListener("resize", calculateFieldPositions);
        return () => {
            window.removeEventListener("resize", calculateFieldPositions);
        };
    }, [boardRef]);

    function getTargetPixelPosition(rowIndex: number, colIndex: number): PixelOffset | undefined {
        if (!animation) {
            return;
        }
        const action = animation.action;
        if (action?.from.y !== rowIndex || action?.from.x !== colIndex) {
            return;
        }
        const currentPosition = getPixelPosition(action.from.y, action.from.x);
        const targetPosition = getPixelPosition(action.to.y, action.to.x);

        return {
            x: targetPosition.x - currentPosition.x,
            y: targetPosition.y - currentPosition.y,
        };
    }

    function getPixelPosition(rowIndex: number, colIndex: number): { x: number; y: number } {
        const fieldId = `field-${rowIndex}-${colIndex}`;
        if (!fieldPositions[fieldId]) {
            return { x: 0, y: 0 };
        }
        return fieldPositions[fieldId];
    }

    return (
        <TablutBoardContainer ref={boardRef}>
            <ColumnIndexContainer>
                {board.fields[0].map((_, colIndex) => (
                    <ColIndex key={colIndex}>{columnCoordinateOf(colIndex)}</ColIndex>
                ))}
            </ColumnIndexContainer>
            {board.fields.map((row, rowIndex) => (
                <TablutRow key={rowIndex}>
                    <RowIndex>{rowIndex + 1}</RowIndex>
                    <Row>
                        {row.map((field, colIndex) => {
                            if (rowIndex === 4 && colIndex === 4) {
                                return (
                                    <KingField
                                        fieldValue={field.state}
                                        key={`${rowIndex}${colIndex}`}
                                        animateTo={getTargetPixelPosition(rowIndex, colIndex)}
                                        id={`field-${rowIndex}-${colIndex}`}
                                    />
                                );
                            } else {
                                return (
                                    <StandardField
                                        fieldValue={field.state}
                                        key={`${rowIndex}${colIndex}`}
                                        animateTo={getTargetPixelPosition(rowIndex, colIndex)}
                                        id={`field-${rowIndex}-${colIndex}`}
                                    />
                                );
                            }
                        })}
                    </Row>
                </TablutRow>
            ))}
        </TablutBoardContainer>
    );
}
