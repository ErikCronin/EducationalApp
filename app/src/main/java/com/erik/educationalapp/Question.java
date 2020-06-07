package com.erik.educationalapp;

import android.os.Parcel;
import android.os.Parcelable;

//implements Parcelable so that the List variable questionList can be transferred when onDestroy is called.
public class Question implements Parcelable {
    static final String DIFFICULTY_EASY = "Easy";
    static final String DIFFICULTY_MEDIUM = "Medium";
    static final String DIFFICULTY_HARD = "Hard";
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    private String question, option1, option2, option3;
    private int answerNr;
    private String difficulty;

    Question() {
    }

    Question(String question, String option1, String option2, String option3, int answerNr,
             String difficulty) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
        this.difficulty = difficulty;
    }

    private Question(Parcel in) {
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        answerNr = in.readInt();
        difficulty = in.readString();
    }

    static String[] getAllDifficultyLevels() {
        return new String[]{
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD,
        };
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeInt(answerNr);
        dest.writeString(difficulty);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    String getQuestion() {
        return question;
    }

    void setQuestion(String question) {
        this.question = question;
    }

    String getOption1() {
        return option1;
    }

    void setOption1(String option1) {
        this.option1 = option1;
    }

    String getOption2() {
        return option2;
    }

    void setOption2(String option2) {
        this.option2 = option2;
    }

    String getOption3() {
        return option3;
    }

    void setOption3(String option3) {
        this.option3 = option3;
    }

    int getAnswerNr() {
        return answerNr;
    }

    void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    String getDifficulty() {
        return difficulty;
    }

    void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
