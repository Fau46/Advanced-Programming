@XMLable
public class Test {
    @XMLfield(type="String")
    public String firstName;
    @XMLfield(type = "String")
    public String lastName;
    @XMLfield(type = "int")
    private int age;

    public Test(){}

    public Test(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
