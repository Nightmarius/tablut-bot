import styled from "styled-components";
import { ReactNode } from "react";

export enum ChipStyle {
    WHITE,
    BLACK,
}

const StyledChip = styled.div<StyleProps>`
    background-color: ${({ chipBackgroundColor }) =>
        (chipBackgroundColor === ChipStyle.BLACK && "var(--primary)") || "white"};
    font-weight: bold;
    color: ${({ chipBackgroundColor }) => (chipBackgroundColor !== ChipStyle.BLACK && "var(--primary)") || "white"};
    border-radius: 8px;
    padding: 0 2px;
    margin: 1px 0 1px 5px;
    border: 1px solid
        ${({ chipBackgroundColor }) => (chipBackgroundColor !== ChipStyle.BLACK && "var(--primary)") || "white"};
`;

export interface StyleProps {
    chipBackgroundColor: ChipStyle;
}

export default function Chip({ children, style }: Props) {
    return <StyledChip chipBackgroundColor={style}>{children}</StyledChip>;
}

interface Props {
    children: ReactNode;
    style: ChipStyle;
}
