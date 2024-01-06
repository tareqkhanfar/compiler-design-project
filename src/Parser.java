import java.util.LinkedList;

public class Parser {

   private  LinkedList<Token> tokenLinkedList ;
   private Token token ;

   public Parser(LinkedList<Token> tokenLinkedList) {
       this.tokenLinkedList = tokenLinkedList ;
   }
   public void getToken() {
       token = tokenLinkedList.poll();
   }

   public void mainFunction() {
       getToken();
       module_heading() ;
       declerations() ;
       procedure_decl();
       block() ;
       name() ;

       if (token.getTokenName().equals(".")){
           getToken();
       }
       else {
           reportError(token);
       }
   }

    private void reportError(Token token) {
       System.err.println("Error with : "+token.getTokenName() + " at line : " + token.getLineNumber() );
    }

    private void name() {

    }

    private void block() {

       if (token.getTokenName().equals("begin")){
           getToken();
       }
       else {
           reportError(token);
       }
       stmtList();

       if (token.getTokenName().equals("end")){
           getToken();
       }
       else {
           reportError(token);
       }


    }

    private void stmtList() {
        statement() ;
        while (token.getTokenName().equals(";")){
            getToken();
            statement();
        }
    }

    private void statement() {
      if (token.getTokenName().equals("readint")){
          readStmt() ;
      }
      else if (token.getTokenName().equals("writeint")){
            writeStmt() ;
      }
      else if (token.getTokenName().equals("if")){
          ifStmt() ;
      }
      else if (token.getTokenName().equals("loop")) {
          repateStmt() ;
      }
      else if (token.getTokenName().equals("while")){
          whileStmt();
      }
      else if (token.getTokenName().equals("exit")){
          exitStmt();
      }
      else if (token.getTokenName().equals("call")){
          callStmt();
      }
      else if (Character.isLetter(token.getTokenName().charAt(0))){
          assStmt();
      }
      else if (token.getTokenName().equals(";")){
          getToken();
      }
      else if (!token.getTokenName().equals(";")) {
          reportError(token);
      }

    }

    private void assStmt() {
       name();
       if (token.getTokenName().equals(":=")){
           getToken();
       }
       else {
           reportError(token);
       }
       exp();
    }

    private void exp() {
       term();
       while (token.getTokenName().equals("+") || token.getTokenName().equals("-")){
           addOperation();
           term();
       }
    }
    private void addOperation() {
       if (token.getTokenName().equals("+") || token.getTokenName().equals("-")){
           getToken();
       }
       else{
           reportError(token);
       }
    }
    private void mullOperation() {
        if (token.getTokenName().equals("*") || token.getTokenName().equals("/") || token.getTokenName().equals("mod") || token.getTokenName().equals("div")){
            getToken();
        }
        else{
            reportError(token);
        }
    }

    private void term() {
       factor();
       while (token.getTokenName().equals("*")||token.getTokenName().equals("/") || token.getTokenName().equals("mod") || token.getTokenName().equals("div")){
           mullOperation();
           factor();
       }
    }

    private void factor() {
       if (token.getTokenName().equals("(")){
           getToken();
           exp();
           if (token.getTokenName().equals(")")){
               getToken();
           }
           else {
               reportError(token);
           }

       }
       else if (Character.isLetter(token.getTokenName().charAt(0))){
           name();
       }
       else if (isIntegerValue(token) || isRealValue(token)){
           value();
       }
       else {
           reportError(token);
       }



    }

    private void whileStmt() {
    }

    private void repateStmt() {
    }

    private void callStmt() {
        
    }

    private void exitStmt() {
        
    }

    private void ifStmt() {
        
    }

    private void writeStmt() {
        if (token.getTokenName().equals("writeint") || token.getTokenName().equals("writereal") || token.getTokenName().equals("writechar")){
                getToken();
                if (token.getTokenName().equals("(")){
                    getToken();
                    writeList() ;

                    if (token.getTokenName().equals(")")){
                        getToken();
                    }
                    else {
                        reportError(token);
                    }
                }
                else {
                    reportError(token);
                }
        }
        else if (token.getTokenName().equals("writeln")){
            getToken();
        }
        else {
            reportError(token);
        }
    }

    private void writeList() {
    }

    private void readStmt() {
       if (token.getTokenName().equals("readint") || token.getTokenName().equals("readreal")|| token.getTokenName().equals("readchar")){
           getToken();
           if (token.getTokenName().equals("(")){
               getToken();
               nameList();
               if (token.getTokenName().equals(")")){
                   getToken();
               }
               else {
                   reportError(token);
               }
           }
           else {
               reportError(token);
           }
       }
       else if (token.getTokenName().equals("readln")) {
                getToken();
       }
       else {
           reportError(token);
       }
        
    }

    private void procedure_decl() {
        procedureHeading() ;
        declerations();
        block();
        name();
    }

    private void procedureHeading() {
       if (token.getTokenName().equals("procedure")){
           getToken();
       }
       else {
           reportError(token);
       }

       name();

       if (token.getTokenName().equals(";")){
           getToken();
       }
       else {
           reportError(token);
       }
    }

    private void declerations() {
         constDecl();
         varDecl();
    }

    private void varDecl() {
       if (token.getTokenName().equals("var")){
           varList();
       }
    }

    private void varList() {
       while (Character.isLetter(token.getTokenName().charAt(0))){
           varItem();
           if (token.getTokenName().equals(";")){
               getToken();
           }
           else {
               reportError(token);
           }
       }
    }

    private void varItem() {
       nameList() ;
       if (token.getTokenName().equals(":")){
           getToken();
       }
       else{
           reportError(token);
       }
       dataType() ;
    }

    private void dataType() {
      if (token.getTokenName().equals("integer")){
          getToken();
      }
      else if (token.getTokenName().equals("real")){
          getToken();
      }
      else if (token.getTokenName().equals("char")){
          getToken();
      }
      else {
          reportError(token);
      }
    }

    private void nameList() {

       name();
       while (token.getTokenName().equals(",")){
           getToken();

           name();
       }

    }

    private void constDecl() {

        if (token.getTokenName().equals("const")){
            getToken();
            constList() ;
        }

    }

    private void constList() {
       while (Character.isLetter(token.getTokenName().charAt(0))){
           name();
           if (token.getTokenName().equals("=")){
               getToken();
           }
           else {
               reportError(token);
           }
           value() ;
           if (token.getTokenName().equals(";")){
               getToken();
           }
           else {
               reportError(token);
           }
        }
    }

    private void value() {
    }

    private void module_heading() {
       if (token.getTokenName().equals("module")){
           getToken();
       }
       else {
           reportError(token);
       }
       name();
       if (token.getTokenName().equals(";")){
           getToken();
       }
       else {
           reportError(token);
       }
    }
    private boolean isIntegerValue(Token token) {
        String tokenName = token.getTokenName();
        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }

        for (char c : tokenName.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    private boolean isRealValue(Token token) {
        String tokenName = token.getTokenName();
        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }

        boolean hasDecimalPoint = false;
        for (char c : tokenName.toCharArray()) {
            if (c == '.') {
                if (hasDecimalPoint) { // Check for multiple decimal points
                    return false;
                }
                hasDecimalPoint = true;
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return hasDecimalPoint; // A real number must contain a decimal point
    }
}
