import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Scanner scanner = new Scanner() ;
        scanner.readFile("G:\\D\\compiler\\compilerProject\\code.txt");
        LinkedList<Token> tokenLinkedList = scanner.startScan();
       System.out.println(tokenLinkedList);
    }
}