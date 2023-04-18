import styled from "styled-components";

const Box = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 4vw 4vw 4vw 4vw;
  width: 100%;
  max-width: 1200px;
  box-shadow: 4px 4px 20px var(--quinary);
`;

export default function InfoBox({ title, subtitle, paragraph, linkTarget }: Props) {

    return (
        <Box>
            <h3>{title}</h3>
            <h4>{subtitle}</h4>
            <p>{paragraph}</p>
            <a href={linkTarget}>Link</a>
        </Box>
    )
}

interface Props {
    title: string;
    subtitle: string;
    paragraph: string;
    linkTarget: string;
}
