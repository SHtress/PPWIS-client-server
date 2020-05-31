package controller;

import kek.Exam;
import kek.Model;
import kek.SNP;
import kek.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Controller implements Serializable {
    private Model model;
    SEARCH_TYPE search;

    public Controller(Model model) {
        this.model = model;
    }

    public ArrayList<Student> getStudentList() {
        return model.getStudentList();
    }

    public int getExamNumber() {
        return model.getExamNumber();
    }

    public void setModel(ArrayList<Student> studentList, int examNumber) {
        this.model=new Model(examNumber);
        this.model.setStudentList(studentList);
    }

    public enum SEARCH_TYPE {
        CRITERIA_1("ПО ФАМИЛИИ И НОМЕРУ ГРУППЫ"),
        CRITERIA_2("ПО ФАМИЛИИ И КОЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ"),
        CRITERIA_3("ПО НОМЕРУ ГРУППЫ И КОЛЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ");

        private final String label_text;

        SEARCH_TYPE(String label_text) {
            this.label_text = label_text;
        }

        public final String label_text() {
            return label_text;
        }
    }

    public List search(String selectedItem, List<String> criteriaList) {
        final String SURNAME = criteriaList.get(0);
        List<Student> studentList = getStudentList();
        List resultList;
        System.out.println(criteriaList.get(3));
        resultList = new ArrayList<Student>();

        if (selectedItem.equals(SEARCH_TYPE.CRITERIA_1.label_text))
            search = SEARCH_TYPE.CRITERIA_1;
        if (selectedItem.equals(SEARCH_TYPE.CRITERIA_2.label_text))
            search = SEARCH_TYPE.CRITERIA_2;
        if (selectedItem.equals(SEARCH_TYPE.CRITERIA_3.label_text))
            search = SEARCH_TYPE.CRITERIA_3;

        switch (search) {
            case CRITERIA_2:
                final String MIN_SCORE = criteriaList.get(1);
                final String MAX_SCORE = criteriaList.get(2);
                Integer studentScore_2 = 0;


                for (Student student : studentList) {
                    studentScore_2 = 0;
                    for (Exam exam : student.getExamList()) {
                        studentScore_2 += exam.getScore();
                    }
                    if (student.getSurname().equals(SURNAME) && studentScore_2 <= Integer.valueOf(MAX_SCORE) && studentScore_2 >= Integer.valueOf(MIN_SCORE)) {
                        resultList.add(student);
                    }
                }
                break;
            case CRITERIA_1:
                final String GROUP = criteriaList.get(3);
                System.out.println(GROUP);
                for (Student student : studentList) {
                    if (student.getSurname().equals(SURNAME) & student.getGroup().equals(GROUP)) {
                        resultList.add(student);
                    }
                }
                break;
            case CRITERIA_3:
                final String MIN_SCORE_3 = criteriaList.get(4);
                final String MAX_SCORE_3 = criteriaList.get(5);
                final String GROUP_3 = criteriaList.get(3);
                Integer studentScore_3 = 0;


                for (Student student : studentList) {
                    studentScore_3 = 0;
                    for (Exam exam : student.getExamList()) {
                        studentScore_3 += exam.getScore();
                    }

                    if (student.getGroup().equals(GROUP_3) && studentScore_3 <= Integer.valueOf(MAX_SCORE_3) && studentScore_3 >= Integer.valueOf(MIN_SCORE_3)) {
                        resultList.add(student);
                    }

                }
                break;
        }

        return resultList;
    }

    public void delete(List<Student> indexList) {
        for (Student student : indexList) {
            getStudentList().remove(student);
        }
    }
}
