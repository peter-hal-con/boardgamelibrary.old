package com.halcon.gaming.boardgamelibrary.cucumber;

class TitleRepository {
    private Map<String,String> titleIdMap = [:]

    public void registerTitle(String id, String name) {
        titleIdMap[name] = id
    }

    public String titleId(name) {
        return titleIdMap[name]
    }
}
