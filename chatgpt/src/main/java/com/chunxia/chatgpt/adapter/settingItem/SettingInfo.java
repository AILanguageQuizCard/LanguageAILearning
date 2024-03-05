package com.chunxia.chatgpt.adapter.settingItem;

public class SettingInfo {
    private String title;
    private boolean choosed;


    public SettingInfo(String title, boolean choosed) {
        this.title = title;
        this.choosed = choosed;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}