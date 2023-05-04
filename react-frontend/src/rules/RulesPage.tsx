import Chapter from "./Chapter";
import { GameAction } from "../shared/domain/model";
import { INITIAL_BOARD } from "../shared/domain/boards";

export default function RulesPage() {
    const setupRules = [
        "The game is played on a 9×9 board",
        "The 16 attackers play against the 8 defenders and the king",
        "The king starts on the central square known as the castle",
    ];
    const movingRules = [
        "Any piece can move any number of squares in a straight line. Like a rook in chess",
        "No piece may ever pass over another piece in its path",
        "Only the king may enter or move through the castle",
        "A piece may be moved in between two enemy pieces without being captured",
    ];
    const capturingRules = [
        "To capture an enemy piece, it needs to be surrounded on opposing sides, but not diagonally",
        "The empty castle counts as a hostile piece for both colors",
        "Captured pieces are removed from the board",
    ];
    const winningRules = [
        "The white defenders win if the king moves to the edge of the board",
        "The black attackers win if they capture the king",
        "Capturing the king inside the castle requires it to be surrounded on all four sides",
        "Capturing the king next to the castle requires it to be surrounded on all three remaining sides",
        "Capturing the king outside of the castle requires it to be surrounded on two opposite sides like any other piece",
    ];

    const simpleMoveAnimation = {
        action: {
            from: {
                x: 3,
                y: 0,
            },
            to: {
                x: 3,
                y: 3,
            },
        } as GameAction,
    };

    return (
        <div>
            <Chapter rules={setupRules} title={"Setup"} board={INITIAL_BOARD} />
            <Chapter rules={movingRules} title={"Moving"} board={INITIAL_BOARD} animation={simpleMoveAnimation} />
            <Chapter rules={capturingRules} title={"Capturing"} />
            <Chapter rules={winningRules} title={"Winning"} />
        </div>
    );
}
