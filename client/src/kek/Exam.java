package kek;

import java.io.Serializable;

public class Exam implements Serializable {
    private int score;

    public Exam(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
