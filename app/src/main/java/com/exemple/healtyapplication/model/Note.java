package com.exemple.healtyapplication.model;


public class Note {
    private String title;
    private String description;
    private String documentId;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note(){ }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentId() {
        return documentId;
    }

}
