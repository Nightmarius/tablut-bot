import styled from "styled-components";

const StyledButton = styled.button`
  background-color: white;
  font-weight: bold;
  border: none;
  color: var(--secondary);
  padding: 1rem 2rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  cursor: pointer;
  border-radius: 4px;

  &:hover {
    color: white;
    background-color: var(--secondary);
  }
`;


export default function Button({ text, onClick }: Props) {

    return (
        <StyledButton onClick={onClick}>{text}</StyledButton>
    )
}

interface Props {
    text: string;
    onClick: () => void;
}
