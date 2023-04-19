import styled from "styled-components";
import Button from "../shared/button/Button";
import { useRef } from "react";
import { nanoid } from "nanoid";
import slide, { Direction } from "../shared/effects/AnimationEffect";

const Box = styled.div<ImageProps>`
  background-image: url(${props => props.imageUrl});
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
  flex-direction: ${({ placement }) =>
          Placement.Left === placement && "row" ||
          "row-reverse"};
  align-items: flex-start;
  margin-left: auto;
  width: 100%;
  margin-top: 1rem;
`;

const Description = styled.div<DescriptionProps>`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 10%;
  min-width: 50%;

  background: linear-gradient(125deg, ${({ placement }) =>
          Placement.Left === placement && "rgba(131, 79, 135, 0.8)" ||
          "rgba(88, 160, 190, 0.8)"}, ${({ placement }) =>
          Placement.Left !== placement && "rgba(131, 79, 135, 0.8)" ||
          "rgba(88, 160, 190, 0.8)"});
  padding-bottom: 4%;
  padding-right: 4%;
`;

const Title = styled.h3`
  font-size: 4rem;
  margin-bottom: 0;
  color: white;
  padding-bottom: 4%;
`;

const Paragraph = styled.p`
  font-size: 3rem;
  margin-bottom: 0;
  color: white;
  padding-bottom: 4%;
  margin-top: 0;
`;

export interface ImageProps {
    imageUrl: string;
    placement: Placement;
}

export interface DescriptionProps {
    placement: Placement;
}


export default function InfoBox({ title, text, buttonText, linkTarget, imagePlacement, imageName }: Props) {

    const elements = useRef<Array<HTMLElement>>([]);

    const id = nanoid();

    slide(elements, `.box-${id} h3, .box-${id} p`, imagePlacement == Placement.Left ? Direction.Left : Direction.Right);

    return (
        <Box className={`box-${id}`} imageUrl={"src/assets/" + imageName} placement={imagePlacement}>
            <Description placement={imagePlacement}>
                <Title className="typing">{title}</Title>
                {text && <Paragraph>{text}</Paragraph>}
                {buttonText && linkTarget && <Button text={buttonText} linkTarget={linkTarget}/>}
            </Description>
        </Box>
    )
}

export enum Placement {
    Left,
    Right,
}

interface Props {
    title: string;
    text?: string;
    buttonText?: string;
    linkTarget?: string;
    imagePlacement: Placement;
    imageName: string;
}
