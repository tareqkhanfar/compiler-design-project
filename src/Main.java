import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("PLease Enter Your Path for Your Code File : ");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine().trim() ;

        LexecalAnlyzer scanner = new LexecalAnlyzer();
        scanner.readFile(fileName);
        LinkedList<Token> tokenLinkedList = scanner.startScan();

       /* for (Token token : tokenLinkedList) {
            System.out.println( " Type : ["+token.getType() + "] Token Name : [" +token.getTokenName()+"]" +"   Line ["+ token.getLineNumber()+"]");
            System.out.println("_________________________________________________________________________________");
        }


        */
         

        Parser parser = new Parser(tokenLinkedList);
        parser.mainFunction();

        System.out.println("###################################################################");
        System.out.println("Finished Successfully , there is no any syntax Error in your code ");
        System.out.println("###################################################################");

    }
}