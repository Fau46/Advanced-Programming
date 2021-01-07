public class Main {
    public static void main(String[] args) {
        Object[] arr = new Object[3];
        arr[0] = new Test("Girolamo", "Gonzales", 28);
        arr[1] = new Test("Fausto", "Frasca", 80);
        arr[2] = new Test("Mario", "Rossi", 46);
        XMLSerializer.serialize(arr,"prova");
    }
}
