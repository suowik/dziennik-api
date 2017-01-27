package it.slowik.teacherslog.model;

import java.util.List;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class Group {
    private String name;
    private String dateOfActivities;
    private String description;
    private List<Student> students;
    private Tests tests;
    private StudentsPresence studentsPresence;

    public String getName() {
        return name;
    }

    public String getDateOfActivities() {
        return dateOfActivities;
    }

    public String getDescription() {
        return description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Tests getTests() {
        return tests;
    }

    public StudentsPresence getStudentsPresence() {
        return studentsPresence;
    }
}
