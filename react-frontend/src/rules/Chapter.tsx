interface Props {
    rules: string[];
    title: string;
}

function Chapter({rules, title}: Props) {

    return (
        <div>
            <h3>{title}</h3>
            <ul>
                {rules.map((rule) => (
                    <li key={rule}>{rule}</li>
                ))}
            </ul>
        </div>
    );
}

export default Chapter;