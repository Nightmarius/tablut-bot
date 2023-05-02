import styled from "styled-components";
import { Crown, Figure } from "./Figure";

interface FieldProps {
    fieldValue: FieldType;
    isKingField?: boolean;
}

export enum FieldType {
    ATTACKER = 1,
    DEFENDER = 2,
    KING = 3,
}

export function KingField({ fieldValue }: { fieldValue: FieldType }) {
    return <Field fieldValue={fieldValue} isKingField={true} />;
}

export function Field({ fieldValue, isKingField = false }: FieldProps) {
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
        return <Figure color={color} />;
    };

    const renderCrown = () => {
        return <Crown />;
    };
    let color;
    const renderField = (fieldContent: number, isKingField = false) => {
        let figure;
        switch (fieldContent) {
            case FieldType.ATTACKER:
                color = "var(--pieceField)";
                figure = renderFigure("var(--attacker)");
                break;
            case FieldType.DEFENDER:
                color = "var(--pieceField)";
                figure = renderFigure("var(--defender)");
                break;
            case FieldType.KING:
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
            <StyledField color={color} size={window.innerWidth > 600 ? "3.125rem" : "1.875rem"}>
                {figure}
            </StyledField>
        );
    };
    return renderField(fieldValue, isKingField);
}
