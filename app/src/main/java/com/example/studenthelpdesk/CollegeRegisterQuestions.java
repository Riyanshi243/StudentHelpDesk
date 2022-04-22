package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;

public class CollegeRegisterQuestions implements Serializable {
    private String question,answer;
    private boolean changeable,compulsory;
    private int type,id,domain;

    @Override
    public boolean equals(@Nullable Object obj) {
        CollegeRegisterQuestions ob=(CollegeRegisterQuestions) obj;
        if(question.equalsIgnoreCase(ob.getQuestion()))
            return true;
        return false;

    }


    public void setDomain(int domain) {
        this.domain = domain;
    }

    public int getDomain() {
        return domain;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    @NonNull
    @Override
    public String toString() {
        String s=question+" "+changeable+" "+compulsory+" "+type;
        return s;
    }

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
