package com.example.studenthelpdesk;

public class CollegeRegisterQuestions {
    private String question;
    private boolean changeable,compulsory;
    private int type;

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public void setCompulsory(boolean compulsory) {
        this.compulsory = compulsory;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public boolean isChangeable() {
        return changeable;
    }
    public boolean isCumplolsory() {
        return compulsory;
    }

    public String getQuestion() {
        return question;
    }
}
