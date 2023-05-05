export interface GameDto {
    id: GameId;
    players: PlayerName[];
    status: GameStatus;
    state: GameState;
    winner?: PlayerName;
}

export interface PlayerName {
    value: string;
}

export interface GameId {
    value: number;
}

export interface PlayerName {
    value: string;
}

export interface GameState {
    currentRequests: PlayRequest[];
    moves: Move[];
    board: Board;
}

export interface PlayRequest {
    playerName: PlayerName;
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
    playerName: PlayerName;
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
    players: PlayerName[];
    status: TournamentStatus;
    state: TournamentState;
    winner?: PlayerName;
    scores: Score[];
}

export interface TournamentState {
    currentRequests: PlayRequest[];
    games: GameDto[];
}

export interface Score {
    playerName: PlayerName;
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
