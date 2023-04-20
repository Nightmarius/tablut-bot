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
`;

export default function TitleSection() {

    return (
        <Section>
            <Title>Zühlke Coding Challenge</Title>
            <Subtitle className="text_bit_animation">Date TBD</Subtitle>
        </Section>
    )
}
