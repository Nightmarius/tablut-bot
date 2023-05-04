import styled from "styled-components";

const StyledInput = styled.input`
    background-color: white;
    font-weight: bold;
    padding: 1rem 2rem;
    margin-right: 1rem;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    border-radius: 4px;
    border: 1px solid var(--secondary);

    &:hover {
        border: 1px solid var(--tertiary);
    }
`;

export default function Button({ type, placeholder, value, onChange }: Props) {
    return <StyledInput type={type} placeholder={placeholder} value={value} onChange={onChange} />;
}

interface Props {
    type: string;
    placeholder: string;
    value: string;
    onChange: (e: any) => void;
}
