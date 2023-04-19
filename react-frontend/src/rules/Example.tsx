export default function Example({exampleText}: Props) {
    return (
        <div>
            <p>{exampleText}</p>
            <p>INSERT BOARD HERE WITH AMAZING ANIMATION</p>
        </div>
    );

}

interface Props {
    exampleText: string;
}
