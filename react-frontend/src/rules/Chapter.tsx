import Example from "./Example";

function Chapter({rules, title, exampleText}: Props) {

    const examples = () => {
        if (exampleText !== undefined) {
            return <Example exampleText={exampleText}/>
        }
    }

    return (
        <div>
            <h3>{title}</h3>
            <ul>
                {rules.map((rule) => (
                    <li key={rule}>{rule}</li>
                ))}
            </ul>
            {examples()}
        </div>
    );
}

interface Props {
    rules: string[];
    title: string;
    exampleText?: string;
}

export default Chapter;