package kek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model implements Serializable {
    private ArrayList<Student> studentList;
    private int examNumber;

    public Model(int examNumber) {
        this.examNumber = examNumber;

        studentList = new ArrayList<>();
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public int getExamNumber() {
        return examNumber;
    }
}
