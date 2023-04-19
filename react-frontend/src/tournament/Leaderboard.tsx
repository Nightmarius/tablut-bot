
interface Props {
    scores: [{name: string, points: number}];
}

export default function ({ scores }: Props) {
    const sortedScores = scores.sort((a, b) => b.points - a.points);

    let currentPosition = 1;
    return (
        <div>
            <h2>Leaderboard</h2>
            <table>
                <thead>
                <tr>
                    <th>Position</th>
                    <th>Name</th>
                    <th>Points</th>
                </tr>
                </thead>
                <tbody>
                {sortedScores.map((score, index) => {
                    if (index > 0 && score.points < sortedScores[index - 1].points) {
                        currentPosition = index + 1;
                    }
                    return (
                        <tr key={index}>
                            <td>{currentPosition}</td>
                            <td>{score.name}</td>
                            <td>{score.points}</td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </div>
    );
};