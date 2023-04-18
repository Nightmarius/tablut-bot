import axios from "axios";

export function AdminPage() {
    const handleCreateGame = () => {
        axios
            .post(`http://localhost:8080/api/lobby/game`)
            .then(response => response.data)
            .then(data => console.log(data))
            .catch(error => console.error(error));
    };
    return (<button onClick={handleCreateGame}>New Game</button>);
}