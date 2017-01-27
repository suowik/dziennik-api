package it.slowik;

/**
 * Created by
 * pawelslowik
 * on 23/01/17.
 */
public class MessagePojo {
    private String name;
    private String surname;

    public MessagePojo(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "MessagePojo{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
