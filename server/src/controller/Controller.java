package controller;

import kek.Exam;
import kek.Model;
import kek.SNP;
import kek.Student;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
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

    public void setModel(ArrayList<Student> studentList, int examNumber) {
        this.model=new Model(examNumber,0);
        this.model.setStudentList(studentList);
    }

    public int getExamNumber() {
        return model.getExamNumber();
    }

    public void newDoc(int examNumber, int entitiesNumber) {
        this.model = new Model(examNumber, entitiesNumber);
    }

    public void addStudent(String surname, String name, String patronym, String group, ArrayList<Exam> examList) {
        model.addStudent(
                new Student(new SNP(surname, name, patronym), group, examList)
        );
    }

    public void addStudent(Student studentToAdd) {
        model.addStudent(studentToAdd);
    }

    public void openDoc(File file) {
        try {
            model.setStudentList(DocOpener.openDoc(file));
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
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

    public void saveDoc(File file) {
        ArrayList<Student> studentList = model.getStudentList();
        Element students;
        Element student;
        Element snp;
        Element group;
        Element exams;
        Element exam;
        Attr surname;
        Attr name;
        Attr patronym;
        Attr groupName;
        Attr examScore;
        Document doc;
        DocumentBuilderFactory docBuilderFactory;
        DocumentBuilder docBuilder;
        TransformerFactory transformerFactory;
        Transformer transformer;
        DOMSource source;
        StreamResult streamResult;

        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            students = doc.createElement("students");
            doc.appendChild(students);

            for (Student studenti : studentList) {
                surname = doc.createAttribute("surname");
                surname.setValue(studenti.getSnp().getSurname());
                name = doc.createAttribute("name");
                name.setValue(studenti.getSnp().getName());
                patronym = doc.createAttribute("patronym");
                patronym.setValue(studenti.getSnp().getPatronym());
                snp = doc.createElement("snp");
                snp.setAttributeNode(surname);
                snp.setAttributeNode(name);
                snp.setAttributeNode(patronym);

                group = doc.createElement("group");
                groupName = doc.createAttribute("name");
                groupName.setValue(studenti.getGroup());
                group.setAttributeNode(groupName);

                exams = doc.createElement("exams");
                for (int j = 0; j < model.getExamNumber(); j++) {
                    examScore = doc.createAttribute("score");
                    examScore.setValue(((Integer) studenti.getExamScore(j)).toString());

                    exam = doc.createElement("exam");
                    exam.setAttributeNode(examScore);
                    exams.appendChild(exam);
                }

                student = doc.createElement("student");
                student.appendChild(snp);
                student.appendChild(group);
                student.appendChild(exams);
                students.appendChild(student);
            }

            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            source = new DOMSource(doc);
            streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public ArrayList<Student> search(String selectedItem, List<String> criteriaList) {
        final String SURNAME = criteriaList.get(0);
        ArrayList<Student> studentList = getStudentList();
        ArrayList<Student> resultList;
        resultList = new ArrayList<>();

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

    public void delete(ArrayList<Integer> indexList) {
        for (int student : indexList) {
            getStudentList().remove(student);
        }
    }
}
