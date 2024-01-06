import java.util.LinkedList;

public class Parser {

   private  LinkedList<Token> tokenLinkedList ;
   private Token token ;

   public Parser(LinkedList<Token> tokenLinkedList) {
       this.tokenLinkedList = tokenLinkedList ;
   }
   private void getToken() {
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
       //System.err.println("Error with : "+token.getTokenName() + " at line : " + token.getLineNumber() );
       //System.exit(0);
       throw new IllegalArgumentException("Error with : "+token.getTokenName() + " at line : " + token.getLineNumber());
    }

    private void name() {
        if (isName(token)){
            getToken();

            System.out.println("Token from Name : " + token.getTokenName());
        }
        else {
            reportError(token);
        }
    }

    private boolean isName(Token token) {
        String tokenName = token.getTokenName();
        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }

        if (!Character.isLetter(tokenName.charAt(0))) {
            return false;
        }

        for (int i = 1; i < tokenName.length(); i++) {
            char c = tokenName.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c)) {
                return false;
            }
        }

        return true;
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
       if (token.getTokenName().equals("while")){
           getToken();
           condition();
           if (token.getTokenName().equals("do")){
               getToken();
               stmtList();
               if (token.getTokenName().equals("end")){
                   getToken();
               }else {
                   reportError(token);
               }
           }
           else {
               reportError(token);
           }

       }else {
           reportError(token);
       }
    }

    private void repateStmt() {
       if (token.getTokenName().equals("loop")){
           getToken();
           stmtList();
           if (token.getTokenName().equals("until")){
               getToken();
               condition();
           }
           else {
               reportError(token);
           }

       }
       else {
           reportError(token);
       }
    }

    private void callStmt() {
        if (token.getTokenName().equals("call")){
            getToken();
        }
        else {
            reportError(token);
        }
        name();
    }

    private void exitStmt() {
        if (token.getTokenName().equals("exit")){
            getToken();
        }else {
            reportError(token);
        }
    }

    private void ifStmt() {

       if (token.getTokenName().equals("if")){
           getToken();
           condition();
           if (token.getTokenName().equals("then")){
               getToken();
               stmtList();
               ElseIF() ;
               Else();
               
               if (token.getTokenName().equals("end")){
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
       else {
           reportError(token);
       }
        
    }

    private void Else() {
       if (token.getTokenName().equals("else")){
           getToken();
           stmtList();
       }

    }

    private void ElseIF() {
       while (token.getTokenName().equals("elseif")){
           getToken();
           condition();
           if (token.getTokenName().equals("then")){
               getToken();
           }
           else {
               reportError(token);
           }
           stmtList();

       }
    }

    private void condition() {

       nameValue() ;
       relationalOperation() ;
       nameValue ();
        
    }

    private void relationalOperation() {

       if (token.getTokenName().equals("=")){
           getToken();
       }
       else if (token.getTokenName().equals("|=")){
           getToken();
       }
       else if (token.getTokenName().equals("<")){
           getToken();
       }
       else if (token.getTokenName().equals("<=")){
           getToken();
       }
       else if (token.getTokenName().equals(">")){
           getToken();
       }
       else if (token.getTokenName().equals(">=")){
           getToken();
       }
       else {
           reportError(token);
       }
    }

    private void nameValue() {
      if (Character.isLetter(token.getTokenName().charAt(0))){
          name();
      }
      else if (isIntegerValue(token) || isRealValue(token)){
          value();
      }
      else {
          reportError(token);
      }
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
       writeItem() ;
       while (token.getTokenName().equals(",")){
           getToken();
           writeItem() ;
       }
    }

    private void writeItem() {
       if (Character.isLetter(token.getTokenName().charAt(0))){
           name();
       }
       else if (isRealValue(token) || isIntegerValue(token)) {
           value();
       }
       else {
           reportError(token);
       }
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
           getToken();
           varList();
       }
    }

    private void varList() {
       byte flag = 0 ;
        while (isStartOfVarItem(flag)) {
            flag =1 ;
            System.out.println("when first entered : " + token.getTokenName());
            varItem();
            System.out.println("Token before colon :" + token.getTokenName());
            if (token.getTokenName().equals(";")){
                getToken();
            }
            else{
                reportError(token);
            }
        }

        System.out.println("Exist Token from Var = " + token.getTokenName());
    }
    private boolean isStartOfVarItem(byte flag) {
        if (token == null || token.getTokenName() == null || token.getTokenName().isEmpty()) {
            return false;
        }
        if (Scanner.KEY_WORDS.contains(token.getTokenName()) && flag == 0) {
            return true ;
        }

        if (Scanner.KEY_WORDS.contains(token.getTokenName())) {
            return false;
        }
        return Character.isLetter(token.getTokenName().charAt(0));
    }





    private void varItem() {

       nameList() ;
       System.out.println("token from var item : " + token.getTokenName());
       if (token.getTokenName().equals(":")){
           getToken();
       }
       else{
           System.out.println("error 2 ");

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
          System.out.println("error 3 ");

          reportError(token);
      }
    }

    private void nameList() {

       name();
       System.out.println("After Execute NameList:" + token.getTokenName());
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

        while (!Scanner.KEY_WORDS.contains(token.getTokenName())) {
            name();
            if (token.getTokenName().equals("=")) {
                getToken();

                value();
                if (token.getTokenName().equals(";")) {
                    getToken();

                } else {
                    reportError(token);
                }
            } else {
                reportError(token);
            }
        }
    }

    private void value() {
       if (isIntegerValue(token)){
           IntegerValue_();
       }
       else if (isRealValue(token)){
           RealValue();
       }
       else {
           reportError(token);
       }
    }

    private void RealValue() {
       getToken();
    }

    private void IntegerValue_() {
       getToken();
        
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
        return tokenName.matches("\\d+");  // Regex to match one or more digits
    }
    private boolean isRealValue(Token token) {
        String tokenName = token.getTokenName();
        return tokenName.matches("\\d+\\.\\d+");  // Regex to match real numbers with a decimal point
    }
}
