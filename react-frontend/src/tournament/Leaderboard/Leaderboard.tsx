import React from "react";
import styled from "styled-components";
import { PlayerName } from "../../shared/domain/model";

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
    opacity: 0;
    animation: slideInAnimationY 1s ease forwards;

    @keyframes slideInAnimationY {
        from {
            transform: translateY(100%);
        }
        to {
            opacity: 1;
        }
    }
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
    position: relative;
    display: block;
    height: 0.6rem;
    margin-right: 0.5rem;
    border-radius: 4px;
    width: ${(props: ProgressBarProps) => props.width};
    background-color: var(--background);
    overflow: hidden;

    &:before {
        position: absolute;
        content: "";
        height: 100%;
        background-color: ${(props) => props.color};
        animation: progress 1s ease forwards;
    }
    @keyframes progress {
        from {
            width: 0;
        }
        to {
            width: 100%;
        }
    }
`;

const Points = styled.span`
    width: 10rem;
    text-align: left;
    opacity: 0;
    animation: slideInAnimation 1s ease forwards 0.8s;

    @keyframes slideInAnimation {
        from {
            transform: translateX(-50%);
        }
        to {
            opacity: 1;
        }
    }
`;

interface Props {
    scores: { name: PlayerName; points: number }[];
}

export default function Leaderboard({ scores }: Props) {
    const sortedScores = [...scores].sort((a, b) => b.points - a.points);

    let currentPosition = 1;

    return (
        <Container>
            <Title>Leaderboard</Title>
            <ScoreList>
                {sortedScores.map((score, index) => {
                    if (index > 0 && score.points < sortedScores[index - 1].points) {
                        currentPosition = index + 1;
                    }
                    const progressWidth = (score.points / sortedScores[0].points) * 100 + "%";

                    let Position = BlackPosition;
                    let color = "var(--primary)";
                    if (currentPosition === 1) {
                        Position = FirstPosition;
                        color = "var(--gold)";
                    } else if (currentPosition === 2) {
                        Position = SecondPosition;
                        color = "var(--silver)";
                    } else if (currentPosition === 3) {
                        Position = ThirdPosition;
                        color = "var(--bronze)";
                    }

                    return (
                        <ScoreListItem key={currentPosition}>
                            <NamePositionContainer>
                                <Position>{currentPosition}</Position>
                                <Name>{score.name.value}</Name>
                            </NamePositionContainer>
                            <ProgressBarContainer>
                                <ProgressBar color={color} width={progressWidth} />
                                <Points>{score.points}</Points>
                            </ProgressBarContainer>
                        </ScoreListItem>
                    );
                })}
            </ScoreList>
        </Container>
    );
}
