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
        this.KEY_WORDS = Set.of("MODULE", "CONST", "VAR", "PROCEDURE", "BEGIN", "END", "IF", "THEN", "ELSE", "CALL", "EXIT");
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

    public void getChar() {
        try {
            currectChar = (char) bufferedReader.read();
            if (currectChar == '\n') {
                lineNumber++;
                currectChar = (char) bufferedReader.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public LinkedList<String> startScan() {

        while (currectChar != -1){
            if (Character.isWhitespace(currectChar)){
                    getChar();
            }
            else if ((currectChar>='A' && currectChar<='Z') || (currectChar >= 'a' && currectChar <='z')){
               tokenLinkedList.add(checkIfKeyWordOrIdentifier());
            }
            else if (Character.isDigit(currectChar)) {
                tokenLinkedList.add(CheckIfIntegerOrReal());
            }
            else {
                checkTheOperator();
            }

        }
        return null ;
    }

    private void checkTheOperator() {
        if (currectChar == ':') {
            getChar();
            if (currectChar == '='){
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ":=" , lineNumber));
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ":" , lineNumber));
            }
        }
        else if (currectChar =='<'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "<=" , lineNumber));
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "<" , lineNumber));
            }
        }
        else if (currectChar =='>'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ">=" , lineNumber));
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , ">" , lineNumber));
            }
        }

        else if (currectChar =='|'){
            getChar();
            if (currectChar == '=') {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "|=" , lineNumber));
            }
            else {
                tokenLinkedList.add(new Token(TokenType.OPERATOR , "|" , lineNumber));
            }
        }
        else if (currectChar =='=' || currectChar == ';' || currectChar == ')' || currectChar == '(' || currectChar == '+' || currectChar == '-' || currectChar == '*' || currectChar =='/'){
            tokenLinkedList.add(new Token(TokenType.OPERATOR , currectChar+"" , lineNumber));
        }

    }

    private Token CheckIfIntegerOrReal() {

        StringBuilder tempToken = new StringBuilder() ;
        tempToken.append(currectChar);
        boolean flag =false  ; // false == integer and true = real
        while (true) {
            if (currectChar=='.'){
                tempToken.append('.');
                flag = true;
            }
            else if (Character.isDigit(currectChar)){
                tempToken.append(currectChar);
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
        tempToken.append(currectChar);
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
