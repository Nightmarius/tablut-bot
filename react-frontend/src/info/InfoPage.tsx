import InfoBox, { Placement } from "./InfoBox";
import styled from "styled-components";
import TitleImage from "/src/assets/title-image.jpg";
import RulesImage from "/src/assets/code-background7.jpg";
import TournamentImage from "/src/assets/code-background6.jpg";
import ZuehlkeCareersImage from "/src/assets/code-background8.jpg";

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
                     imagePlacement={Placement.Left} image={TitleImage}/>
            <InfoBox title="How to play" linkTarget="rules" buttonText="Learn the rules of Tablut"
                     imagePlacement={Placement.Right} image={RulesImage}/>
            <InfoBox title="Tablut tournament" linkTarget="tournament" buttonText="Watch the tournament live"
                     imagePlacement={Placement.Left} image={TournamentImage}/>
            <InfoBox title="Zühlke Careers" text="Your career with Zühlke" buttonText="Explore jobs"
                     linkTarget="https://www.zuehlke.com/en/careers"
                     imagePlacement={Placement.Right} image={ZuehlkeCareersImage}/>
        </Section>

    )
}
