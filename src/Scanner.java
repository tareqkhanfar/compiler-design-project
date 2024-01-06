import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {

    private BufferedReader bufferedReader ;
    private  Set<String> KEY_WORDS ;
    private Set<String> ArithmaticOperator ;
    private Set<String> relationalOperator ;
    private Set<String> specialChar ;



    private  char currectChar;

    private long lineNumber  =1 ;
   private static Pattern pattern ;


    private LinkedList<Token> tokenLinkedList = new LinkedList<>();
    public Scanner(){
        this.KEY_WORDS = Set.of("module", "begin", "end", "const", "var", "integer", "real", "char", "procedure", "mod", "div" , "readint", "readreal", "readchar", "readln", "writeint", "writereal", "writechar"
                , "writeln", "if", "then", "elseif", "else", "while", "do", "loop", "until", "exit", "call");
        this.ArithmaticOperator = Set.of("+" , "-" , "*" , "/" );
        this.specialChar = Set.of(";"  , "=" , "(" , ")");

    }

    public void readFile(String filePath) {
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void testReadFile () {
        getChar();
while (currectChar != (char) -1){
    System.out.println(currectChar);
getChar();
        }
System.out.println("Stop ");
    }

    public void getChar() {
        try {
            int readValue = bufferedReader.read();
            if (readValue != -1) {
                currectChar = (char) readValue;
                if (currectChar == '\n') {
                    lineNumber++;
                }
            } else {
                currectChar = (char) -1; // EOF indicator
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public LinkedList<Token> startScan() {
        getChar();
       while (currectChar != (char) -1){
            if (Character.isWhitespace(currectChar)){
                    getChar();
            }
            else if ((currectChar >= 'A' && currectChar <= 'Z') || (currectChar >= 'a' && currectChar <= 'z')) {
                tokenLinkedList.add(checkIfKeyWordOrIdentifier());
            }
            else if (Character.isDigit(currectChar)) {
                tokenLinkedList.add(CheckIfIntegerOrReal());
            }
            else {
                checkTheOperator();
            }


        }

        return tokenLinkedList ;
    }

    private void checkTheOperator() {

        if (currectChar == ':') {
            getChar();
            if (currectChar == '='){
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ":=" , lineNumber));
                getChar();
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ":" , lineNumber));
            }
        }
        else if (currectChar =='<'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "<=" , lineNumber));
                getChar();
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "<" , lineNumber));
            }
        }
        else if (currectChar =='>'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ">=" , lineNumber));
                getChar();
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ">" , lineNumber));
            }
        }

        else if (currectChar =='|'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "|=" , lineNumber));
                getChar();
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "|" , lineNumber));
            }
        }
        else if (currectChar =='=' || currectChar == ';' || currectChar == ')' || currectChar == '(' || currectChar == '+' || currectChar == '-' || currectChar == '*' || currectChar =='/' || currectChar=='.'){
            tokenLinkedList.add(new Token(TokenType.OPERATOR , currectChar+"" , lineNumber));
            getChar();
        }

    }

    private Token CheckIfIntegerOrReal() {

        StringBuilder tempToken = new StringBuilder() ;
        boolean flag =false  ; // false == integer and true = real
        while (true) {

            if (currectChar=='.'){
                tempToken.append('.');
                flag = true;
                getChar();

            }
            else if (Character.isDigit(currectChar)){
                tempToken.append(currectChar);
                getChar();

            }
            else {
                break;
            }
        }

        if (flag){
            pattern = Pattern.compile(Regex.REGEX_REAL_NUMBERS);
            Matcher matcher = pattern.matcher(tempToken.toString());
            while (matcher.find()){
                Token token = new Token() ;
                token.setLineNumber(lineNumber);
                token.setType(TokenType.IDENTIFIES);
                token.setTokenName(tempToken.toString());
                return token;
            }
        }
        else {
            pattern = Pattern.compile(Regex.REGEX_INTEGER_NUMBERS);
            Matcher matcher = pattern.matcher(tempToken.toString());
            while (matcher.find()){
                Token token = new Token() ;
                token.setLineNumber(lineNumber);
                token.setType(TokenType.IDENTIFIES);
                token.setTokenName(tempToken.toString());
                return token;
            }
        }
        return null;
    }

    private Token checkIfKeyWordOrIdentifier(){
        StringBuilder tempToken = new StringBuilder() ;
        while (true) {
            if (Character.isLetterOrDigit(currectChar)){
                tempToken.append(currectChar);
                getChar();

            }
            else {
                break;
            }
        }

        if (KEY_WORDS.contains(tempToken.toString())){
            Token token = new Token();
            token.setTokenName(tempToken.toString());
            token.setType(TokenType.KEYWORD);
            token.setLineNumber(lineNumber);
            return token ;
        }
        else {
            pattern = Pattern.compile(Regex.REGEX_IDENTIFIERS);
           Matcher matcher = pattern.matcher(tempToken.toString());
            while (matcher.find()){
                Token token = new Token();
                token.setTokenName(tempToken.toString());
                token.setType(TokenType.IDENTIFIES);
                token.setLineNumber(lineNumber);
                return token;
            }
        }
        return null ;
    }


}
