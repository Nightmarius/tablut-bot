import styled, { keyframes } from "styled-components";
import crown from "../../assets/crown.svg";
import { PixelOffset } from "./StandardField";

export const Crown = () => {
    return <img src={crown} alt="Crown image for King" />;
};

const moveAnimation = (x: number, y: number) => keyframes`
  0%, 25% {
    transform: translate(0px, 0px);
  }

  75%, 100% {
    transform: translate(${x}px, ${y}px);
  }
`;

export const Circle = styled.div<{ color: string }>`
    border-radius: 50%;
    background-color: ${(props) => props.color};
    border: 1px solid black;
    width: 85%;
    height: 85%;
`;

export const Figure = styled.div<{ animateTo?: PixelOffset }>`
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    animation: ${(props) => (props.animateTo ? moveAnimation(props.animateTo.x, props.animateTo.y) : "")} 4s ease-in-out
        infinite;
`;
