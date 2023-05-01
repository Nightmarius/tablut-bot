export interface GameDto {
    id: GameId;
    players: Player[];
    status: GameStatus;
    state: GameState;
    winner?: PlayerId;
}

export interface Player {
    id: PlayerId;
    name: PlayerName;
}

export interface PlayerName {
    value: string;
}

export interface GameId {
    value: number;
}

export interface PlayerId {
    value: string;
}

export interface GameState {
    currentRequests: PlayRequest[];
    moves: Move[];
    board: Board;
}

export interface PlayRequest {
    playerId: PlayerId;
    gameId: GameId;
    attacker: boolean;
    board: Board;
    requestId: RequestId;
    possibleActions: GameAction[];
}

export interface RequestId {
    value: string;
}

export interface Move {
    playerId: PlayerId;
    requestId: RequestId;
    action: GameAction;
}

export interface Board {
    fields: Field[][];
}

export interface Field {
    state: FieldState;
}

export enum FieldState {
    EMPTY = "EMPTY",
    ATTACKER = "ATTACKER",
    DEFENDER = "DEFENDER",
    KING = "KING",
}

export enum GameStatus {
    NOT_STARTED = "NOT_STARTED",
    IN_PROGRESS = "IN_PROGRESS",
    FINISHED = "FINISHED",
    DELETED = "DELETED",
}

export interface GameAction {
    from: Position;
    to: Position;
}

export interface Position {
    x: number;
    y: number;
}

export interface TournamentDto {
    id: TournamentId;
    players: Player[];
    status: TournamentStatus;
    state: TournamentState;
    winner?: PlayerId;
    scores: Score[];
}

export interface TournamentState {
    currentRequests: PlayRequest[];
    games: GameDto[];
}

export interface Score {
    playerId: PlayerId;
    score: number;
}

export interface TournamentId {
    value: number;
}

export enum TournamentStatus {
    NOT_STARTED = "NOT_STARTED",
    IN_PROGRESS = "IN_PROGRESS",
    FINISHED = "FINISHED",
    DELETED = "DELETED",
}

export interface BotDto {
    name: PlayerName;
    token: Token;
}

export interface Token {
    value: string;
}

export const INITIAL_BOARD: Board = {
    fields: [
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.DEFENDER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
        [
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.DEFENDER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
        ],
        [
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
            { state: FieldState.DEFENDER },
            { state: FieldState.DEFENDER },
            { state: FieldState.KING },
            { state: FieldState.DEFENDER },
            { state: FieldState.DEFENDER },
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
        ],
        [
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.DEFENDER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
        ],
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.DEFENDER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
        [
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
            { state: FieldState.ATTACKER },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
            { state: FieldState.EMPTY },
        ],
    ],
};
