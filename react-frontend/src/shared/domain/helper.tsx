import { GameAction, GameDto, GameStatus, Player } from "./model";

export const getPlayerForNextTurn = (game: GameDto | undefined): Player | undefined => {
    if (game?.status === GameStatus.IN_PROGRESS && game.state.currentRequests.length > 0) {
        return game.players.find((player) => player.id?.value === game.state.currentRequests[0].playerId?.value);
    }
    return undefined;
}

export const getWinner = (game: GameDto | undefined): Player | undefined => {
    if (game?.status === GameStatus.FINISHED) {
        return game.players.find((player) => player.id.value === game.winner?.value && game.winner !== undefined);
    }
    return undefined;
}

const getLetter = (index: number): string => {
    return String.fromCharCode(65 + index);
}

export const getMoveNotation = (action: GameAction): string => {
    if (action.from.x === action.to.x) {
        return getLetter(action.from.x) + (action.from.y + 1) + (action.to.y + 1);
    } else {
        return (
            getLetter(action.from.x) + (action.from.y + 1) + getLetter(action.to.x)
        );
    }
}
