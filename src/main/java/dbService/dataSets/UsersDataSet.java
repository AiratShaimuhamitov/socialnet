package dbService.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
@SuppressWarnings("UnusedDeclaration")
public class UsersDataSet {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email",unique = true,updatable = false)
    private String email;

    @Column(name = "password")
    private String pass;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    public UsersDataSet(){

    }

    public UsersDataSet(long id, String name, String lastName, int age, String email, String pass) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getAge(){
        return age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
