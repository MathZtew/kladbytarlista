package se.liu.lintek.amo;

import java.util.HashMap;
import java.util.Map;

public class StudentsList {
    Map<String, Student> students;

    public StudentsList() {
        students = new HashMap<String, Student>();
        students.put("matsa159", new Student("matsa159"));
        System.out.println(students);
        System.out.println(students.containsKey("matsa159"));
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
}
