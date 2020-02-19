package se.liu.lintek.amo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StudentList {
    Map<String, Student> students;

    public StudentList() {
        students = new HashMap<>();
    }

    public void importStudents(String path) throws IOException {
        try {
            InputStream is = new FileInputStream(path);

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String fileAsString = sb.toString();

            Student[] test = new Gson().fromJson(fileAsString, Student[].class);
            if (test != null) {
                for (Student student : test) {
                    addStudent(student);
                }
            }
            System.out.println(Arrays.toString(test));
        } catch (FileNotFoundException e) {
            System.out.println("Creating file");
            try {
                String temp = "";
                Files.write(Paths.get(path), temp.getBytes());
            } catch (IOException ioe) {
                e.printStackTrace();
            }
        }
    }

    public boolean contains(String liuId) {
        return students.containsKey(liuId);
    }

    public Student get(String liuId) {
        return students.get(liuId);
    }

    public String getStatus(String liuId) {
        if (isLiuId(liuId)) {
            if (contains(liuId)) {
                Student student = get(liuId);
                return "Valid LiUID\nIn database\n" +
                        "liUID: " + student.getLiuId() + "\n" +
                        "Clothes: " + student.getClothes() + "\n" +
                        "Other: " + student.getOther();
            }
            return "Valid LiUID\nNot in database";
        }
        else {
            return "Not a valid LiUID";
        }
    }

    public boolean addValues(String liuId, int clothes, int other) {
        if (contains(liuId)) {
            Student student = students.get(liuId);
            student.addClothes(clothes);
            student.addOther(other);
            return true;
        }
        return false;
    }


    public boolean removeValues(String liuId, int clothes, int other) {
        if (contains(liuId)) {
            Student student = students.get(liuId);
            student.removeClothes(clothes);
            student.removeOther(other);
            return true;
        }
        return false;
    }

    public void addStudent(String id, int clothes, int other) {
        if (isLiuId(id)) {
            students.put(id, new Student(id, clothes, other));
        }
    }

    public void addStudent(Student student) {
        students.put(student.getLiuId(), student);
    }

    static boolean isLiuId(String liuId) {
        if (liuId.length() == 8) {
            try {
                Integer.parseInt(liuId.substring(5));
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        if (liuId.length() == 7) {
            try {
                Integer.parseInt(liuId.substring(4));
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public Student[] getStudents() {
        return students.values().toArray(new Student[0]);
    }

    public String getJson() {
        Gson obj = new GsonBuilder().setPrettyPrinting().create();
        String json = obj.toJson(getStudents());
        return json;
    }

    @Override
    public String toString() {
        return "StudentsList{" +
                "students=" + students +
                '}';
    }
}
