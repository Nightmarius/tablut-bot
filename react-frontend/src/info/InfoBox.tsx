import styled from "styled-components";
import { nanoid } from "nanoid";
import useSlideAnimationOnIntersection, { Direction } from "../shared/hooks/SlideAnimationHook";
import LinkButton from "../shared/ui/button/LinkButton";
import { ButtonStyle } from "../shared/ui/button/Button";

export enum Placement {
    Left,
    Right,
}

const Box = styled.div<ImageProps>`
    background-image: url(${(props) => props.imageUrl});
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    display: flex;
    flex-direction: ${({ placement }) => (Placement.Left === placement && "row") || "row-reverse"};
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

    background: linear-gradient(
        125deg,
        ${({ placement }) => (Placement.Left === placement && "rgba(131, 79, 135, 0.8)") || "rgba(88, 160, 190, 0.8)"},
        ${({ placement }) => (Placement.Left !== placement && "rgba(131, 79, 135, 0.8)") || "rgba(88, 160, 190, 0.8)"}
    );
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

export default function InfoBox({ title, text, buttonText, linkTarget, imagePlacement, image }: Props) {
    const id = nanoid();

    const slideDirection = imagePlacement === Placement.Left ? Direction.Left : Direction.Right;
    useSlideAnimationOnIntersection(`.box-${id} h3, .box-${id} p`, slideDirection);

    return (
        <Box className={`box-${id}`} imageUrl={image} placement={imagePlacement}>
            <Description placement={imagePlacement}>
                <Title className="typing">{title}</Title>
                {text && <Paragraph>{text}</Paragraph>}
                {buttonText && linkTarget && (
                    <LinkButton style={ButtonStyle.WHITE} linkTarget={linkTarget}>
                        {buttonText}
                    </LinkButton>
                )}
            </Description>
        </Box>
    );
}

interface Props {
    title: string;
    text?: string;
    buttonText?: string;
    linkTarget?: string;
    imagePlacement: Placement;
    image: string;
}
