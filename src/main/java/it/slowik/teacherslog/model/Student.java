package it.slowik.teacherslog.model;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class Student implements Comparable<Student> {
    private final Integer id;
    private final String name;
    private final String surname;

    public Student(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public int compareTo(Student o) {
        return o.surname.compareTo(surname);
    }
}
