import styled from "styled-components";
import {Crown, Figure} from "./Figure";

export enum FieldType {
    ATTACKER = 1,
    DEFENDER = 2,
    KING = 3
}

export function Field({fieldValue}: { fieldValue: FieldType }) {
    const StyledField = styled.div<{ color: string; size: string }>`
    width: ${(props) => props.size};
    height: ${(props) => props.size};
    background-color: ${(props) => props.color};
    border: 1px solid white;
    display: flex;
    justify-content: center;
    align-items: center;
  `;

    const renderFigure = (color: string) => {
        return <Figure color={color}/>;
    };

    const renderCrown = () => {
        return <Crown/>;
    }
    let color;
    const renderField = (fieldContent: number) => {
        let figure;
        switch (fieldContent) {
            case FieldType.ATTACKER:
                color = "#834f87";
                figure = renderFigure("#000000");
                break;
            case FieldType.DEFENDER:
                color = "#834f87";
                figure = renderFigure("#ffffff");
                break;
            case FieldType.KING:
                color = "#169fe9";
                figure = renderCrown();
                break;
            default:
                color = "rgba(131,79,135,0.78)";
        }
        return <StyledField color={color} size={window.innerWidth > 600 ? "50px" : "30px"}>{figure}</StyledField>;
    };
    return renderField(fieldValue);
}