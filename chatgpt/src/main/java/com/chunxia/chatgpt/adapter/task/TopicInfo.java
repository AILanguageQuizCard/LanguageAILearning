package com.chunxia.chatgpt.adapter.task;

public class TopicInfo{
    private String title;
    private String description;
    private boolean favorite;
    private String startWords;
    private String systemCommand;
    private int startMode;
    private String beforeUserCommand="";
    private int bgDrawableId;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBgDrawableId() {
        return bgDrawableId;
    }

    public void setBgDrawableId(int bgDrawableId) {
        this.bgDrawableId = bgDrawableId;
    }

    public String getBeforeUserCommand() {
        return beforeUserCommand;
    }

    public void setBeforeUserCommand(String beforeUserCommand) {
        this.beforeUserCommand = beforeUserCommand;
    }

    public String getStartWords() {
        return startWords;
    }

    public void setStartWords(String startWords) {
        this.startWords = startWords;
    }

    public String getSystemCommand() {
        return systemCommand;
    }

    public void setSystemCommand(String systemCommand) {
        this.systemCommand = systemCommand;
    }

    public int getStartMode() {
        return startMode;
    }

    public void setStartMode(int startMode) {
        this.startMode = startMode;
    }


    public TopicInfo(String title, String description, boolean favorite, int startMode,
                     String startWords, String systemCommand,  String beforeUserCommand,
                     int bgDrawableId, int count) {
        this.title = title;
        this.description = description;
        this.favorite = favorite;
        this.startWords = startWords;
        this.systemCommand = systemCommand;
        this.startMode = startMode;
        this.beforeUserCommand = beforeUserCommand;
        this.bgDrawableId = bgDrawableId;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}