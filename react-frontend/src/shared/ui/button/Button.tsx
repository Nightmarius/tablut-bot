import styled from "styled-components";
import { ReactNode } from "react";

const StyledButton = styled.button<StyleProps>`
  background-color: ${({ buttonBackgroundColor }) => buttonBackgroundColor === ButtonStyle.PURPLE && "var(--secondary)" || "white"};
  font-weight: bold;
  color: ${({ buttonBackgroundColor }) => buttonBackgroundColor !== ButtonStyle.PURPLE && "var(--secondary)" || "white"};
  padding: 1rem 2rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid ${({ buttonBackgroundColor }) => buttonBackgroundColor === ButtonStyle.PURPLE && "var(--secondary)" || "white"};

  &:hover {
    color: ${({ buttonBackgroundColor }) => buttonBackgroundColor === ButtonStyle.PURPLE && "var(--secondary)" || "white"};
    background-color: ${({ buttonBackgroundColor }) => buttonBackgroundColor !== ButtonStyle.PURPLE && "var(--secondary)" || "white"};
    border: 1px solid ${({ buttonBackgroundColor }) => buttonBackgroundColor === ButtonStyle.PURPLE && "var(--secondary)" || "white"};
  }
`;

export interface StyleProps {
    buttonBackgroundColor: ButtonStyle
}


export default function Button({ children, onClick, style = ButtonStyle.PURPLE }: Props) {

    return (
        <StyledButton buttonBackgroundColor={style} onClick={onClick}>{children}</StyledButton>
    )
}

export enum ButtonStyle {
    WHITE, PURPLE
}

interface Props {
    children: ReactNode;
    onClick: () => void;
    style?: ButtonStyle
}
