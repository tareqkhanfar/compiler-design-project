import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Scanner scanner = new Scanner() ;
        scanner.readFile("G:\\D\\compiler\\compilerProject\\code.txt");
        LinkedList<Token> tokenLinkedList = scanner.startScan();
        for (Token token : tokenLinkedList) {
            System.out.println( " Type : ["+token.getType() + "] Token Name : [" +token.getTokenName()+"]" +"   Line ["+ token.getLineNumber()+"]");
            System.out.println("_________________________________________________________________________________");
        }
    }
}