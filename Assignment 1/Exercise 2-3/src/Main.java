public class Main {
    public static void main(String[] args) {
        Object[] arr = new Object[2];
        String file = "serialize";
        arr[0] = new Test("Margaret", "Hamilton", 28, 'F');
        arr[1] = new Test("Alan", "Turing", 80, 'M');
        XMLSerializer.serialize(arr,"serialize");

        System.out.println("Start deserialization of file "+file+".xml");
        Object[] test = XMLDeserializer.deserialize("./"+file+".xml");
        System.out.println("\nOutput: ");
        for(Object object : test){
            Test test2 = (Test) object;
            System.out.println(test2.getFirstName()+" "+test2.getLastName()+ " "+test2.getAge()+" "+test2.getGender());
        }
    }
}
