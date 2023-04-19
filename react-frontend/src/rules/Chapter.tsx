import Example from "./Example";
import styled from "styled-components";

const Section = styled.div`
  padding-left: 4vw;
  padding-right: 4vw;
`;

const Title = styled.h2`
  font-size: 2rem;
  background: linear-gradient(90deg,#985b9c,#a6e0fe);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`;

const Rules = styled.div`
  align-items: flex-start;
  display: inline-block;
  min-width: 60%;
  vertical-align: top;

`;

const Examples = styled.div`
  display: inline-block;
  width: auto;
  vertical-align: top;
`;

export default function Chapter({rules, title, exampleText}: Props) {

    const mapRules = () => {
        return rules.map((rule) => (
            <li key={rule}>{rule}</li>
        ));
    };

    return (
        <Section>
            <Title>{title}</Title>
            <Rules>
                <ul>{mapRules()}</ul>
            </Rules>
            <Examples>
                {exampleText && <Example exampleText={exampleText}/>}
            </Examples>
        </Section>
    );
}

interface Props {
    rules: string[];
    title: string;
    exampleText?: string;
}
