import TitleSection from "./TitleSection";
import InfoBox, { Placement } from "./InfoBox";
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
            <InfoBox buttonText="Rules" title="How to play" linkTarget="rules"
                     imagePlacement={Placement.Left} imageName="code-background1.jpg"/>
            <InfoBox buttonText="Go to Tournament" title="Tablut tournament" linkTarget="tournament"
                     imagePlacement={Placement.Right} imageName="code-background2.jpg"/>
        </Section>

    )
}
