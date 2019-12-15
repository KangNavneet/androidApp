package com.example.whatsapp;

public class storyClass {

    Long story_id;
    String post_by;
    Long date_of_post;
    Long date_of_expiry;
    String attach_path;
    String caption;
    String typeOfFile;

    public storyClass() {
    }

    public storyClass(Long story_id, String post_by, Long date_of_post, Long date_of_expiry, String attach_path, String caption, String typeOfFile) {
        this.story_id = story_id;
        this.post_by = post_by;
        this.date_of_post = date_of_post;
        this.date_of_expiry = date_of_expiry;
        this.attach_path = attach_path;
        this.caption = caption;
        this.typeOfFile = typeOfFile;
    }

    public Long getStory_id() {
        return story_id;
    }

    public void setStory_id(Long story_id) {
        this.story_id = story_id;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public Long getDate_of_post() {
        return date_of_post;
    }

    public void setDate_of_post(Long date_of_post) {
        this.date_of_post = date_of_post;
    }

    public Long getDate_of_expiry() {
        return date_of_expiry;
    }

    public void setDate_of_expiry(Long date_of_expiry) {
        this.date_of_expiry = date_of_expiry;
    }

    public String getAttach_path() {
        return attach_path;
    }

    public void setAttach_path(String attach_path) {
        this.attach_path = attach_path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTypeOfFile() {
        return typeOfFile;
    }

    public void setTypeOfFile(String typeOfFile) {
        this.typeOfFile = typeOfFile;
    }
}
