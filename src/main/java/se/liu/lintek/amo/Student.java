package se.liu.lintek.amo;

import java.util.Objects;

public class Student {
    private String liuId;
    private int clothes;
    private int other;

    public Student(String liuId) {
        this.liuId = liuId;
        this.clothes = 0;
        this.other = 0;
    }

    public Student(String liuId, int clothes, int other) {
        this.liuId = liuId;
        this.clothes = clothes;
        this.other = other;
    }

    public int getClothes() {
        return clothes;
    }

    public int getOther() {
        return other;
    }

    public String getLiuId() {
        return liuId;
    }

    public void addClothes(int clothes) {
        this.clothes += clothes;
    }

    public void removeClothes(int clothes) {
        this.clothes -= clothes;
    }

    public void addOther(int other) {
        this.other += other;
    }

    public void removeOther(int other) {
        this.other -= other;
    }

    @Override
    public String toString() {
        return "Student{" +
                "liuId='" + liuId + '\'' +
                ", clothes=" + clothes +
                ", other=" + other +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return liuId.equals(student.liuId) && clothes == student.getClothes() && other == student.getOther();
    }

    @Override
    public int hashCode() {
        return Objects.hash(liuId);
    }
}
