package com.example.myapplication3;

public class PostFragment
{
    private Boolean comment;
    private String textBody;
    private String author;
    private String post_id;

    public PostFragment(String textBody, String author, String post_id, Boolean comment) {
        this.comment = comment;
        this.post_id = post_id;
        this.author = author;
        this.textBody = textBody;
    }

    public void setTextBody(String body) {
        this.textBody = body;
    }

    public String getTextBody() {
        return this.textBody;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_id() {
        return this.post_id;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public Boolean getComment() {
        return this.comment;
    }


}