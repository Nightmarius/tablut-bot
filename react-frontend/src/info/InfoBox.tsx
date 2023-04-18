import styled from "styled-components";
import Button from "../shared/button/Button";

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

export interface ImageProps {
    imageUrl: string;
    placement: Placement;
}

export interface DescriptionProps {
    placement: Placement;
}

const Title = styled.h3`
  font-size: 4rem;
  margin-bottom: 0;
  color: white;
  padding-bottom: 4%;
`;

export default function InfoBox({ title, buttonText, linkTarget, imagePlacement, imageName }: Props) {

    return (
        <Box imageUrl={"src/assets/" + imageName} placement={imagePlacement}>
            <Description placement={imagePlacement}>
                <Title>{title}</Title>
                <Button text={buttonText} linkTarget={linkTarget}/>
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
    buttonText: string;
    linkTarget: string;
    imagePlacement: Placement;
    imageName: string;
}
