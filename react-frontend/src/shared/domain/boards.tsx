import { Board, Field, FieldState, Position } from "./model";

const INITIAL_BOARD_STR: string[] = [
    "   AAA   ",
    "    A    ",
    "    D    ",
    "A   D   A",
    "AADDKDDAA",
    "A   D   A",
    "    D    ",
    "    A    ",
    "   AAA   ",
];

const MID_GAME_STR: string[] = [
    "   A     ",
    "     A   ",
    "  D      ",
    "A   A   A",
    "AADD KDA ",
    "        A",
    "A   DA   ",
    "         ",
    "   AAA   ",
];

const END_GAME_STR: string[] = [
    "   A     ",
    "     A   ",
    "  D  K   ",
    "A   A   A",
    "AADD  DA ",
    "        A",
    "   A A   ",
    "         ",
    "   AAA   ",
];

export const INITIAL_BOARD: Board = createBoardFromStringArray(INITIAL_BOARD_STR);
export const MID_GAME: Board = createBoardFromStringArray(MID_GAME_STR);

export const END_GAME: Board = createBoardFromStringArray(END_GAME_STR);

function createBoardFromStringArray(strings: string[]): Board {
    const boardFields: Field[][] = [];

    strings.forEach((str) => {
        const row: Field[] = [];
        for (let i = 0; i < str.length; i++) {
            row.push({ state: fromChar(str[i]) } as Field);
        }
        boardFields.push(row);
    });

    return {
        fields: boardFields,
    } as Board;
}

function fromChar(char: string): FieldState {
    switch (char) {
        case "A":
            return FieldState.ATTACKER;
        case "D":
            return FieldState.DEFENDER;
        case "K":
            return FieldState.KING;
        default:
            return FieldState.EMPTY;
    }
}

export function getAllPositions(board: Board, state: FieldState): Position[] {
    const positions: Position[] = [];
    board.fields.forEach((row, y) => {
        row.forEach((field, x) => {
            if (field.state === state) {
                positions.push({ x, y });
            }
        });
    });
    return positions;
}
