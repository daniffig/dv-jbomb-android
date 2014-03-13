package reference;

public enum JBombRequestResponse {
	ERROR_FLASH,
	NOTICE_FLASH,
	
	NEW_GAME_REQUEST,
	NEW_GAME_RESPONSE,
	CREATE_GAME_REQUEST,
	CREATE_GAME_RESPONSE,
	
	GAME_LIST_REQUEST,
	GAME_LIST_RESPONSE,
	JOIN_GAME_REQUEST,
	GAMEPLAY_INFORMATION_RESPONSE,
	PLAYER_ADDED,
	ADJACENT_PLAYERS,
	MAX_PLAYERS_REACHED,
	START_GAME_REQUEST,
	BOMB_OWNER_RESPONSE,
	SEND_BOMB_REQUEST,
	QUIZ_QUESTION_RESPONSE,
	QUIZ_ANSWER_REQUEST,
	QUIZ_ANSWER_RESPONSE,
	GAMEPLAY_STATE_RESPONSE,
	BOMB_DETONATED_RESPONSE,

	FINISH_CONNECTION_REQUEST,
	
	GAME_SETTINGS_INFORMATION_REQUEST,
	GAME_SETTINGS_INFORMATION_RESPONSE
}