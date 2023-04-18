import styled from "styled-components";

const Section = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Title = styled.h1`
  font-size: 3rem;
  text-align: center;
  margin-bottom: 0;
`;

const Subtitle = styled.h2`
  font-size: 2rem;
  text-align: center;
  margin-top: 0;
  margin-bottom: 0;
`;

const imageUrl = "src/assets/title-image.jpg";

const Image = styled.div`
  background-image: url(${imageUrl});
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  height: 80vw;
  max-height: 20rem;
  width: 160vw;
  max-width: 36rem;
  box-shadow: 0 0 2rem 2rem white inset;
`;

export default function TitleSection() {

    return (
        <Section>
            <Title>Zühlke Coding Challenge</Title>
            <Subtitle className="text_bit_animation">Date TBD</Subtitle>
            <Image></Image>
        </Section>
    )
}
