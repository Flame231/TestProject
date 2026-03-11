package dto;

import myAnnotations.Column;
import myAnnotations.PrimaryKey;
import myAnnotations.Table;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Table("person")
public class Person {
    @PrimaryKey("id")
    long id;
    @Column("age")
    int age;
    @Column("salary")
    int salary;
    @Column("pasport")
    String pasport;
    @Column("adress")
    String adress;
    @Column("dateofBirthday")
    java.sql.Date dateofBirthday;
    @Column("dateTimeCreate")
    Timestamp dateTimeCreate;
    @Column("timetoLunch")
    java.sql.Time timetoLunch;
    @Column("letter")
    String letter;

    public Person() {
    }

    public Person(long id, int age, int salary, String pasport, String adress, Date dateofBirthday, Timestamp dateTimeCreate, Time timetoLunch, String letter) {
        this.id = id;
        this.age = age;
        this.salary = salary;
        this.pasport = pasport;
        this.adress = adress;
        this.dateofBirthday = dateofBirthday;
        this.dateTimeCreate = dateTimeCreate;
        this.timetoLunch = timetoLunch;
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", age=" + age +
                ", salary=" + salary +
                ", pasport='" + pasport + '\'' +
                ", address='" + adress + '\'' +
                ", dateofBirthday=" + dateofBirthday +
                ", dateTimeCreate=" + dateTimeCreate +
                ", timetoLunch=" + timetoLunch +
                ", letter='" + letter + '\'' +
                '}';
    }
}
