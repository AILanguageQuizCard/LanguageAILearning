package com.chunxia.chatgpt.adapter.training;

public class TrainingInfo {
    private String title;
    private String description;
    private boolean favorite;

    private int bgDrawableId;

    private TrainingType type;

    public int getBgDrawableId() {
        return bgDrawableId;
    }

    public void setBgDrawableId(int bgDrawableId) {
        this.bgDrawableId = bgDrawableId;
    }

    public TrainingInfo(String title, String description, boolean favorite,
                        int bgDrawableId, TrainingType type) {
        this.title = title;
        this.description = description;
        this.favorite = favorite;
        this.bgDrawableId = bgDrawableId;
        this.type = type;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
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