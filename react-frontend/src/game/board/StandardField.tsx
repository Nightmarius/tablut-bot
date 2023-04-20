import styled from "styled-components";
import {Crown, Figure} from "./Figure";
import {FieldState} from "../../shared/domain/model";

interface FieldProps {
    fieldValue: FieldState;
    isKingField?: boolean;
}


export function KingField({fieldValue}: { fieldValue: FieldState }) {
    return <StandardField fieldValue={fieldValue} isKingField={true}/>;
}

export function StandardField({fieldValue, isKingField = false}: FieldProps) {
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
    const renderField = (fieldContent: FieldState, isKingField: boolean = false) => {
        let figure;
        switch (fieldContent) {
            case FieldState.ATTACKER:
                color = 'var(--pieceField)';
                figure = renderFigure('var(--attacker)');
                break;
            case FieldState.DEFENDER:
                color = 'var(--pieceField)';
                figure = renderFigure('var(--defender)');
                break;
            case FieldState.KING:
                color = 'var(--pieceField)';
                figure = renderCrown();
                break;
            default:
                color = 'var(--emptyField)';
        }
        if (isKingField) {
            color = 'var(--kingField)';
        }
        return <StyledField color={color} size={window.innerWidth > 600 ? "3.125rem" : "1.875rem"}>{figure}</StyledField>;
    };
    return renderField(fieldValue, isKingField);
}