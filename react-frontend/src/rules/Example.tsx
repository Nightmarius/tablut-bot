import TablutBoard, { BoardAnimation } from "../game/board/TablutBoard";
import { Board } from "../shared/domain/model";

export default function Example({ board, animation }: Props) {
    return <TablutBoard board={board} animation={animation}></TablutBoard>;
}

interface Props {
    board: Board;
    animation?: BoardAnimation;
}
