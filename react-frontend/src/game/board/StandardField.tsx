import styled from "styled-components";
import { Circle, Crown, Figure } from "./Figure";
import { FieldState } from "../../shared/domain/model";

export interface PixelOffset {
    x: number;
    y: number;
}

interface FieldProps {
    fieldValue: FieldState;
    id: string;
    isKingField?: boolean;
    animateTo?: PixelOffset;
}

export function KingField({ fieldValue, isKingField = true, animateTo, id }: FieldProps) {
    return <StandardField fieldValue={fieldValue} isKingField={isKingField} animateTo={animateTo} id={id} />;
}

const StyledField = styled.div<{ color: string; size: string; animateTo?: PixelOffset }>`
    width: ${(props) => props.size};
    height: ${(props) => props.size};
    background-color: ${(props) => props.color};
    border: 1px solid white;
`;

export function StandardField({ fieldValue, isKingField = false, animateTo, id }: FieldProps) {
    const renderFigure = (color: string) => {
        return (
            <Figure id={id} animateTo={animateTo}>
                <Circle color={color} />
            </Figure>
        );
    };

    const renderCrown = () => {
        return (
            <Figure id={id} animateTo={animateTo}>
                <Crown />;
            </Figure>
        );
    };
    let color;
    const renderField = (fieldContent: FieldState, isKingField = false) => {
        let figure;
        switch (fieldContent) {
            case FieldState.ATTACKER:
                color = "var(--pieceField)";
                figure = renderFigure("var(--attacker)");
                break;
            case FieldState.DEFENDER:
                color = "var(--pieceField)";
                figure = renderFigure("var(--defender)");
                break;
            case FieldState.KING:
                color = "var(--pieceField)";
                figure = renderCrown();
                break;
            default:
                color = "var(--emptyField)";
        }
        if (isKingField) {
            color = "var(--kingField)";
        }
        return (
            <StyledField id={id} color={color} size={window.innerWidth > 600 ? "3.125rem" : "1.875rem"}>
                {figure}
            </StyledField>
        );
    };
    return renderField(fieldValue, isKingField);
}
