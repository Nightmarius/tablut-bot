function Example({exampleText}: Props) {
    return (
        <div>
            <h4>Examples</h4>
            <p>{exampleText}</p>
            <h5>Before</h5>
            <p>INSERT BOARD HERE</p>
            <h5>After</h5>
            <p>INSERT BOARD HERE</p>
        </div>
    );

}

interface Props {
    exampleText: string;
}

export default Example;