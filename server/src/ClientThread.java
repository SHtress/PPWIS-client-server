import controller.Controller;
import controller.DocOpener;
import javafx.scene.control.skin.SliderSkin;
import kek.SNP;
import kek.Student;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import kek.Model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientThread implements Runnable {
    private Socket sock;
    private ObjectInputStream serverInputStream = null;
    private ObjectOutputStream serverOutputStream = null;
    private Controller controller = new Controller(new Model(10, 0));

    public ClientThread(Socket sock) {
        this.sock = sock;
    }


    public void run() {
        String msg;
        try {
            serverInputStream = new ObjectInputStream(sock.getInputStream());
            serverOutputStream = new ObjectOutputStream(sock.getOutputStream());
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": client connected\n");

            msg = (String) serverInputStream.readObject();
            while (!msg.equals("exit") && Main.isRunning) {
                switch (msg) {
                    case "addStud": {
                        addStud();
                        break;
                    }
                    case "openDoc": {
                        openDoc();
                        break;
                    }
                    case "saveDoc": {
                        saveDoc();
                        break;
                    }
                    case "newDoc": {
                        newDoc();
                        break;
                    }

                    case "search": {
                        search();
                        break;
                    }

                    case "delete": {
                        search();
                        search();
                        delete();
                    }
                }
                msg = (String) serverInputStream.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                serverOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sock.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void addStud() {
        try {
            Student studentToAdd = (Student) serverInputStream.readObject();
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": got student " + studentToAdd + "\n");
            controller.addStudent(studentToAdd);
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": added student " + studentToAdd + "\n");
            serverOutputStream.writeObject(controller.getStudentList().size());
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": sent response " + controller.getStudentList() + "\n");
            for (int i = 0; i < controller.getStudentList().size(); i++) {
                serverOutputStream.writeObject(controller.getStudentList().get(i));
                Main.date = new Date();
                Main.logText.setText(Main.logText.getText() + Main.date + ": sent student " + controller.getStudentList().get(i) + "\n");
         
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    void openDoc() throws Exception {
        ArrayList<String> listFiles = new ArrayList<>();
        File myFolder = new File("D:/examples/");
        File[] files = myFolder.listFiles();
        for (int i = 0; i < files.length; i++) listFiles.add(files[i].toString());
        serverOutputStream.writeObject(listFiles);
        Main.date = new Date();
        Main.logText.setText(Main.logText.getText() + Main.date + ": sent fileList " + listFiles + "\n");

        String path = (String) serverInputStream.readObject();
        if (listFiles.contains(path)) {
            ArrayList<Student> list = DocOpener.openDoc(new File(path));
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": opened file " + path + "\n");
            this.controller.setModel(list, controller.getExamNumber());
            serverOutputStream.writeObject(list);
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": sent students " + list + "\n");
        } else System.out.println("Файл не выбран");
    }

    public void saveDoc() throws Exception {
        String fileName = (String) serverInputStream.readObject();
        List<Student> studentList = controller.getStudentList();
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
        int ExamNumbers = controller.getExamNumber();

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
                for (int j = 0; j < ExamNumbers; j++) {
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
            streamResult = new StreamResult("D:/examples/" + fileName);
            transformer.transform(source, streamResult);
            Main.date = new Date();
            Main.logText.setText(Main.logText.getText() + Main.date + ": saved file " + fileName + "\n");
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    void newDoc() throws Exception {
        int examNumber = (int) serverInputStream.readObject();
        Main.date = new Date();
        Main.logText.setText(Main.logText.getText() + Main.date + ": get exam number\n");
        int entitiesNumber = (int) serverInputStream.readObject();
        Main.date = new Date();
        Main.logText.setText(Main.logText.getText() + Main.date + ": get entities number\n");
        controller.newDoc(examNumber, entitiesNumber);
        Main.date = new Date();
        Main.logText.setText(Main.logText.getText() + Main.date + ": create new documen\n");
        serverOutputStream.writeObject(controller.getStudentList());
        Main.date = new Date();
        Main.logText.setText(Main.logText.getText() + Main.date + ": sent students " + controller.getStudentList() + "\n");
    }

    void search() {
        try {
            String selectedItem = (String) serverInputStream.readObject();
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": get selectedItem\n");
            ArrayList<String> criteriaList = (ArrayList<String>) serverInputStream.readObject();
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": get studentList\n");
            ArrayList<Student> resultList = controller.search(selectedItem, criteriaList);
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": find students "+resultList+"\n");
            serverOutputStream.writeObject(resultList.size());
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": sent size of list\n");
            for (int i = 0; i < resultList.size(); i++) {
                serverOutputStream.writeObject(resultList.get(i));
                Main.date= new Date();
                Main.logText.setText(Main.logText.getText()+Main.date+": sent student "+resultList.get(i)+"\n");
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void delete() {
        try {
            serverOutputStream.writeObject(controller.getStudentList());
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": sent studentList\n");
            int size = (Integer) serverInputStream.readObject();
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": get size\n");
            ArrayList<Integer> posToDelete = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                posToDelete.add((Integer) serverInputStream.readObject());
                Main.date= new Date();
                Main.logText.setText(Main.logText.getText()+Main.date+": get student\n");
            }
            controller.delete(posToDelete);
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": delete students\n");
            serverOutputStream.writeObject(controller.getStudentList().size());
            Main.date= new Date();
            Main.logText.setText(Main.logText.getText()+Main.date+": sent size\n");
            for (int i = 0; i < controller.getStudentList().size(); i++) {
                serverOutputStream.writeObject(controller.getStudentList().get(i));
                Main.date= new Date();
                Main.logText.setText(Main.logText.getText()+Main.date+": sent student\n");
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
