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
            <InfoBox title="Zühlke Coding Challenge 2023" text="Are you ready to compete with your bot?"
                     buttonText="Sign up"
                     linkTarget="https://zühlke.com/coding-challenge-2023/signup/tbd"
                     imagePlacement={Placement.Left} imageName="title-image.jpg"/>
            <InfoBox title="How to play" linkTarget="rules" buttonText="Learn the rules of Tablut"
                     imagePlacement={Placement.Right} imageName="code-background7.jpg"/>
            <InfoBox title="Tablut tournament" linkTarget="tournament" buttonText="Watch the tournament live"
                     imagePlacement={Placement.Left} imageName="code-background6.jpg"/>
            <InfoBox title="Zühlke Careers" text="Your career with Zühlke" buttonText="Explore jobs"
                     linkTarget="https://www.zuehlke.com/en/careers"
                     imagePlacement={Placement.Right} imageName="code-background8.jpg"/>
        </Section>

    )
}
