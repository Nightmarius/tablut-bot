import TitleSection from "./TitleSection";
import InfoBox from "./InfoBox";
import styled from "styled-components";

const Section = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export default function InfoPage() {

    return (
        <Section>
            <TitleSection/>
            <InfoBox title="How to play" subtitle="Sub" paragraph="asdf" linkTarget="rules"/>
        </Section>

    )
}
