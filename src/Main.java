import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner() ;
        scanner.readFile("G:\\D\\compiler\\compilerProject\\code.txt");
        LinkedList<Token> tokenLinkedList = scanner.startScan();

        for (Token token : tokenLinkedList) {
            System.out.println( " Type : ["+token.getType() + "] Token Name : [" +token.getTokenName()+"]" +"   Line ["+ token.getLineNumber()+"]");
            System.out.println("_________________________________________________________________________________");
        }

         

        Parser parser = new Parser(tokenLinkedList);
        parser.mainFunction();

        System.out.println("###################################################################");
        System.out.println("Finished Successfully , there is no any syntax Error in your code ");
        System.out.println("###################################################################");

    }
}