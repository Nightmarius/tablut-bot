import React from 'react';
import styled from 'styled-components';

const Container = styled.div`
  padding: 2rem 0;
`;

const Title = styled.h2`
  margin-bottom: 1rem;
  text-align: center;
`;

const ScoreList = styled.div`
  align-items: center;
  margin: 0 10%;
  min-width: 90%;
`;

const ScoreListItem = styled.div`
  align-items: flex-start;
  display: flex;
  flex-direction: column;
  padding: 20px;
`;

const NamePositionContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Position = styled.span`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  font-weight: bold;
`;

const FirstPosition = styled(Position)`
  background-color: var(--gold);
`;

const SecondPosition = styled(Position)`
  background-color: var(--silver);
`;

const ThirdPosition = styled(Position)`
  background-color: var(--bronze);
`;

const BlackPosition = styled(Position)`
  background-color: var(--primary);
  color: #fff;
`;

const Name = styled.span`
  font-weight: bold;
  margin-right: 0.5rem;
  width: 10rem;
  text-align: left;
`;

const ProgressBarContainer = styled.div`
  display: flex;
  align-items: center;
  margin-top: 0.5rem;
  width: 100%;
`;

interface ProgressBarProps {
    color: string;
    width: string;
}

const ProgressBar = styled.span<ProgressBarProps>`
  background-color: ${(props) => props.color};
  height: 0.6rem;
  margin-right: 0.5rem;
  border-radius: 4px;
  width: ${(props) => props.width};
`;

const Points = styled.span`
  width: 10rem;
  text-align: left;
`;

interface Props {
    scores: { name: string, points: number }[];
}

export default function Leaderboard({scores}: Props) {
    const sortedScores = scores.sort((a, b) => b.points - a.points);

    let currentPosition = 1;

    return (
        <Container>
            <Title>Leaderboard</Title>
            <ScoreList>
                {sortedScores.map((score, index) => {
                    if (index > 0 && score.points < sortedScores[index - 1].points) {
                        currentPosition = index + 1;
                    }
                    const progressWidth = score.points / sortedScores[0].points * 100 + '%';

                    let Position = BlackPosition;
                    let color = 'var(--primary)';
                    if (currentPosition === 1) {
                        Position = FirstPosition;
                        color = 'var(--gold)';
                    } else if (currentPosition === 2) {
                        Position = SecondPosition;
                        color = 'var(--silver)';
                    } else if (currentPosition === 3) {
                        Position = ThirdPosition;
                        color = 'var(--bronze)';
                    }

                    return (
                        <ScoreListItem key={index}>
                            <NamePositionContainer>
                                <Position>{currentPosition}</Position>
                                <Name>{score.name}</Name>
                            </NamePositionContainer>
                            <ProgressBarContainer>
                                <ProgressBar
                                    color={color}
                                    width={progressWidth}
                                />
                                <Points>{score.points}</Points>
                            </ProgressBarContainer>
                        </ScoreListItem>
                    );
                })}
            </ScoreList>
        </Container>
    );
}
