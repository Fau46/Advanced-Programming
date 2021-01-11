@XMLable
public class Test {
    @XMLfield(type="String")
    public String firstName;
    @XMLfield(type = "String", name = "surname")
    public String lastName;
    @XMLfield(type = "int")
    private int age;
    @XMLfield(type = "char")
    private char gender;

    public Test(){}

    public Test(String firstName, String lastName, int age, char gender){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public char getGender() {
        return gender;
    }
}


