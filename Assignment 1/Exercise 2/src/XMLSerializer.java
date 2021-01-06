public class XMLSerializer {
    public static void main(String[] args) {
        Test t = new Test();
        Object[] arr = new Object[2];
        arr[0] = t;
        arr[1] = new String("prova");
        serialize(arr,"prova");
    }

    public static void serialize(Object [ ] arr, String fileName){
        for(int i=0; i< arr.length; i++){
            Class c = arr[i].getClass();
            System.out.println(c.isAnnotationPresent(XMLable.class)+" ");
        }
    }
}
