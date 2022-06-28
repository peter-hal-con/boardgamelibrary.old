package com.halcon.gaming.boardgamelibrary.cucumber;

class GameRepository {
    private Map<String,String> gameIdMap = [:]

    public void registerGame(String id, String title) {
        gameIdMap[title] = id
    }

    public String gameId(title) {
        return gameIdMap[title]
    }
}
