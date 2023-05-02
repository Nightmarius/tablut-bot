import styled from "styled-components";
import crown from "../../assets/crown.svg";

export const Crown = () => {
    return (
        <div>
            <img src={crown} alt="Crown SVG imgae" />
        </div>
    );
};
export const Figure = styled.div<{ color: string }>`
    border-radius: 50%;
    background-color: ${(props) => props.color};
    border: 1px solid black;
    width: 85%;
    height: 85%;
`;
