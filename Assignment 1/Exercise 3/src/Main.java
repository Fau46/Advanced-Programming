public class Main {
    public static void main(String[] args) {
        Object[] test = XMLDeserializer.deserialize("./src");
        for(Object test1 : test){
            Test test2 = (Test) test1;
            System.out.println(test2.getFirstName()+" "+test2.getLastName()+ " "+test2.getAge());
        }
    }
}
