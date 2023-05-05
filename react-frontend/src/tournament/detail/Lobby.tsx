import styled from "styled-components";
import { PlayerName } from "../../shared/domain/model";
import { useEffect, useState } from "react";
import remoteService from "../../services/RemoteService";
import { useParams } from "react-router";

const Title = styled.h1`
    font-size: 3rem;
    text-align: center;
    margin: 2rem 0 2rem 0;
`;

const PlayerContainer = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;
    justify-content: space-evenly;
    margin: 3rem 1rem 1rem;
`;

const PlayerChip = styled.div`
    font-weight: bold;
    font-size: 1.5rem;
    background-color: var(--tertiary);
    color: white;
    align-items: center;
    display: inline-flex;
    justify-content: center;
    margin: 1rem 1rem 1rem;
    border-radius: 9999px;
    padding: 0.5rem 1rem;
    animation: pulsate 3s ease-in-out infinite;
`;

const StyledButton = styled.button`
    margin-left: 10px;
`;

export default function Lobby({ players }: Props) {
    const { tournamentId } = useParams();
    const [lobby, setLobby] = useState<PlayerName[]>([]);

    useEffect(() => {
        const fetchLobby = () => {
            remoteService.get<PlayerName[]>("/api/lobby").then((response: PlayerName[]) => {
                setLobby(response.filter((p1) => !players.find((p2) => p1.value === p2.value)));
            });
        };

        // Initial fetch
        fetchLobby();

        // Polling
        const intervalId = setInterval(() => {
            fetchLobby();
        }, 1000);

        // Cleanup
        return () => {
            clearInterval(intervalId);
        };
    }, [players]);

    const editPlayers = (players: PlayerName[]) => {
        remoteService.post<PlayerName[]>("/admin/tournament/" + tournamentId + "/edit", players);
    };

    const addPlayer = (player: PlayerName) => {
        players.push(player);
        editPlayers(players);
    };

    const removePlayer = (player: PlayerName) => {
        players.splice(players.indexOf(player), 1);
        editPlayers(players);
    };

    return (
        <>
            <Title>Players in Tournament</Title>
            <PlayerContainer>
                {players.map((player) => (
                    <PlayerChip key={player.value}>
                        {player.value}
                        <StyledButton onClick={() => removePlayer(player)}>❌</StyledButton>
                    </PlayerChip>
                ))}
            </PlayerContainer>

            <Title>Players in Lobby</Title>
            <PlayerContainer>
                {lobby.map((player) => (
                    <PlayerChip key={player.value}>
                        {player.value}
                        <StyledButton onClick={() => addPlayer(player)}>➕</StyledButton>
                    </PlayerChip>
                ))}
            </PlayerContainer>
        </>
    );
}

interface Props {
    players: PlayerName[];
}
