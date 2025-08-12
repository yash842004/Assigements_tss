package com.tss.database;

public class Student {
    private int student_id;
    private String name;
    private int age;
    private double percentage;
    private int roll_no;

    public Student(int student_id, String name, int age, double percentage, int roll_no) {
        this.student_id = student_id;
        this.name = name;
        this.age = age;
        this.percentage = percentage;
        this.roll_no = roll_no;
    }

    public int getStudent_id() {
        return student_id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getRoll_no() {
        return roll_no;
    }
}
