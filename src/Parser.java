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

       if (token != null && token.getTokenName().equals(".")){
           getToken();
           if (token != null){
               reportError(token , "There is a Some Codes after \" . \" that located in end of your program.\n so remove its to compile your code correctly.");
           }
       }
       else {
           if (token == null){
               Token token1 = new Token() ;
               token1.setTokenName("end for your program will be \".\" only");
               token1.setLineNumber(LexecalAnlyzer.lineNumber);
               reportError(token1, "Your Token not equal the '.' ");

           }
           else {
               reportError(token, "Your Token not equal the '.' ");
           }


       }
   }

    private void reportError(Token token , String correctError) {
        System.out.println("###################################################################");
        System.out.println("Error with token : "+token.getTokenName() + "\n at line : " + token.getLineNumber() +"\n Details Error : " + correctError);

       // throw  new IllegalArgumentException("error") ;
       System.out.println("###################################################################");
        System.exit(1);
    }

    private void name() {
        if (isName(token)){
            getToken();
        }
        else {
            reportError(token , "Your Token is Not a Name. The Name must be contain a letter or digits only ");
        }
    }

    private boolean isName(Token token) {
        String tokenName = token.getTokenName();


        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }
        if (LexecalAnlyzer.KEY_WORDS.contains(tokenName)){
            return false ;
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
           reportError(token , "Your Token is Not equal begin . The block must be started with \"begin keyword\"");
       }
       stmtList();

       if (token.getTokenName().equals("end")){
           getToken();
       }
       else {
           reportError(token,"Your Token is Not equal end . The block must be ended with \"end keyword\"");
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

       if (token.getType().equals(TokenType.KEYWORD)){
           if (token.getTokenName().startsWith("read")){
               readStmt() ;
               return;
           }
           else if (token.getTokenName().startsWith("write")){
               writeStmt() ;
               return;
           }
           else if (token.getTokenName().equals("if")){
               ifStmt() ;
               return;
           }
           else if (token.getTokenName().equals("loop")) {
               repateStmt() ;
               return;
           }
           else if (token.getTokenName().equals("while")){
               whileStmt();
               return;
           }
           else if (token.getTokenName().equals("exit")){
               exitStmt();
               return;
           }
           else if (token.getTokenName().equals("call")){
               callStmt();
               return;
           }

       }
       else if (token.getType().equals(TokenType.IDENTIFIES)){
           if (Character.isLetter(token.getTokenName().charAt(0))){
               assStmt();
               return;
           }
       }
       else if (!token.getTokenName().equals(";")) {
           reportError(token , "Your Token is Not equal \";\" ");
       }



    }

    private void assStmt() {
       name();
       if (token.getTokenName().equals(":=")){
           getToken();
       }
       else {
           reportError(token , "Your Token is Not equal \":=\" . The Expected Token must equal \":=\"");
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
           reportError(token, "your token is not add Operation(+ , -) .");
       }
    }
    private void mullOperation() {
        if (token.getTokenName().equals("*") || token.getTokenName().equals("/") || token.getTokenName().equals("mod") || token.getTokenName().equals("div")){
            getToken();
        }
        else{
            reportError(token , "your token is not Mul Operation(* , / , mod , div).");
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
               reportError(token , "Missing Closing Parenthesis ");
           }

       }
       else if (isName(token)){
           name();
       }
       else if (isIntegerValue(token) || isRealValue(token)){
           value();
       }
       else {
           reportError(token , "Unexpected Token .  ");
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
                   reportError(token , "Missing \"end\" Keyword:");
               }
           }
           else {
               reportError(token , "Missing \"do\" Keyword");
           }

       }else {
           reportError(token , "Unexpected Token in Place of \"while\"");
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
               reportError(token , "Missing \"until\" Keyword:");
           }

       }
       else {
           reportError(token , "Unexpected Token in Place of \"loop\"");
       }
    }

    private void callStmt() {
        if (token.getTokenName().equals("call")){
            getToken();
        }
        else {
            reportError(token , "Unexpected Token in Place of \"call\"");
        }
        name();
    }

    private void exitStmt() {
        if (token.getTokenName().equals("exit")){
            getToken();
        }else {
            reportError(token , "Unexpected Token in Place of \"exit\"");
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
                   reportError(token , "Missing \"end\" Keyword");
               }
           }
           else {
               reportError(token , "Missing \"then\" Keyword:");
           }
       }
       else {
           reportError(token , "Unexpected Token in Place of \"if\"");
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
               reportError(token , "Missing \"then\" Keyword in \"elseif\" Clause:");
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
           reportError(token , "Unexpected Token for Relational Operation");
       }
    }

    private void nameValue() {
      if (isName(token)){
          name();
      }
      else if (isIntegerValue(token) || isRealValue(token)){
          value();
      }
      else {
          reportError(token , "Unexpected Token Type . Your Token is not name or value ");
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
                        reportError(token ,"Missing Closing Parenthesis");
                    }
                }
                else {
                    reportError(token , "Missing Opening Parenthesis After \"writeint\", \"writereal\", or \"writechar\"");
                }
        }
        else if (token.getTokenName().equals("writeln")){
            getToken();
        }
        else {
            reportError(token , "Unexpected Token in Place of \"writeint\", \"writereal\", \"writechar\", or \"writeln\"");
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
       if (isName(token)){
           name();
       }
       else if (isRealValue(token) || isIntegerValue(token)) {
           value();
       }
       else {
           reportError(token , "Unexpected Token Type.");
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
                   reportError(token , "Missing Closing Parenthesis");
               }
           }
           else {
               reportError(token , "Missing Opening Parenthesis After \"readint\", \"readreal\", or \"readchar\"");
           }
       }
       else if (token.getTokenName().equals("readln")) {
                getToken();
       }
       else {
           reportError(token , "Unexpected Token in Place of \"readint\", \"readreal\", \"readchar\", or \"readln\"");
       }
        
    }

    private void procedure_decl() {
        procedureHeading() ;
        declerations();
        block();
        name();
        if (token.getTokenName().equals(";")){
            getToken();
        }
        else {
            reportError(token , "Missing Semicolon at the End of Procedure Declaration.");
        }
    }

    private void procedureHeading() {
       if (token.getTokenName().equals("procedure")){
           getToken();
       }
       else {
           reportError(token , "Unexpected Token in Place of \"procedure\"");
       }

       name();

       if (token.getTokenName().equals(";")){
           getToken();
       }
       else {
           reportError(token , "Missing Semicolon After Procedure Name");
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
      // byte flag = 0 ;
        while (isName(token)) {
      //      flag =1 ;
            varItem();
            if (token.getTokenName().equals(";")){
                getToken();
            }
            else{
                reportError(token , "Missing Semicolon After Variable Item");
            }
        }

    }
    private boolean isStartOfVarItem(byte flag) {
        if (token == null || token.getTokenName() == null || token.getTokenName().isEmpty()) {
            return false;
        }
        if (LexecalAnlyzer.KEY_WORDS.contains(token.getTokenName()) && flag == 0) {
            return true ;
        }

        if (LexecalAnlyzer.KEY_WORDS.contains(token.getTokenName())) {
            return false;
        }
        return Character.isLetter(token.getTokenName().charAt(0));
    }





    private void varItem() {

       nameList() ;
       if (token.getTokenName().equals(":")){
           getToken();
       }
       else{

           reportError(token , "Missing Colon \":\" After Name List");
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

          reportError(token , "Unexpected Token for Data Type must be (integer , real , char) ");
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

        while (!LexecalAnlyzer.KEY_WORDS.contains(token.getTokenName())) {
            name();
            if (token.getTokenName().equals("=")) {
                getToken();
                value();
                if (token.getTokenName().equals(";")) {
                    getToken();

                } else {
                    reportError(token , "Missing Semicolon \";\" After Constant Value");
                }
            } else {
                reportError(token , "Missing Equals Sign \"=\" After Constant Name");
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
           reportError(token , "Unexpected Token Type for Value");
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
           reportError(token,"Unexpected Token in Place of \"module\"");
       }
       name();
       if (token.getTokenName().equals(";")){
           getToken();
       }
       else {
           reportError(token , "Missing Semicolon After Module Name");
       }
    }
    private boolean isIntegerValue(Token token) {
        String tokenName = token.getTokenName();
        return tokenName.matches("\\d+");
    }
    private boolean isRealValue(Token token) {
        String tokenName = token.getTokenName();
        return tokenName.matches("\\d+\\.\\d+");
    }
}
