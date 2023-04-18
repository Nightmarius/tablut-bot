import {useParams} from "react-router";
import TablutBoard from "./board/TablutBoard";
import styled from "styled-components";

export default function GamePage() {
    let {gameId} = useParams();
    const startingBoard = [
        [0, 0, 0, 1, 1, 1, 0, 0, 0],
        [0, 0, 0, 0, 1, 0, 0, 0, 0],
        [0, 0, 0, 0, 2, 0, 0, 0, 0],
        [1, 0, 0, 0, 2, 0, 0, 0, 1],
        [1, 1, 2, 2, 3, 2, 2, 1, 1],
        [1, 0, 0, 0, 2, 0, 0, 0, 1],
        [0, 0, 0, 0, 2, 0, 0, 0, 0],
        [0, 0, 0, 0, 1, 0, 0, 0, 0],
        [0, 0, 0, 1, 1, 1, 0, 0, 0],
    ];
    const GameContainer = styled.div`
    margin-left: 0.5em;
    `
    return (
        <>
            <h2>Game</h2>
            <GameContainer>
                <h3> Game ID: {gameId} </h3>
                <TablutBoard board={startingBoard}/>
            </GameContainer>
        </>
    )
}
