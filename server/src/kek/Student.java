package kek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private SNP snp;
    private String group;
    private ArrayList<Exam> examList;

    public Student(SNP snp, String group, ArrayList<Exam> examList) {
        this.snp = snp;
        this.group = group;
        this.examList = examList;
    }

    public SNP getSnp() {
        return snp;
    }



    public String getSurname() {
        return snp.getSurname();
    }

    public void setSnp(SNP snp) {
        this.snp = snp;
    }

    public String getAlignSnp() {
        return snp.getSurname() + " " + snp.getName() + " " + snp.getPatronym();
    }

    public void setAlignSnp(String alignSnp) {
        this.snp = new SNP(alignSnp);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<Exam> getExamList() {
        return examList;
    }

    public void setExamList(ArrayList<Exam> examList) {
        this.examList = examList;
    }

    public int getExamScore(int i) {
        return examList.get(i).getScore();
    }
}
