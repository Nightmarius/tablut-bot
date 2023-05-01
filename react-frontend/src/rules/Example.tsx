import TablutBoard from "../game/board/TablutBoard";
import { GameAction, INITIAL_BOARD } from "../shared/domain/model";

export default function Example({ exampleText }: Props) {
    const gameAction = {
        from: {
            x: 3,
            y: 0,
        },
        to: {
            x: 3,
            y: 3,
        },
    } as GameAction;
    return (
        <div>
            <p>{exampleText}</p>
            <TablutBoard board={INITIAL_BOARD} action={gameAction}></TablutBoard>
            <p>INSERT BOARD HERE WITH AMAZING ANIMATION</p>
        </div>
    );
}

interface Props {
    exampleText: string;
}
