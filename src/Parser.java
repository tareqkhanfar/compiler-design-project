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
           reportError(token , "Your Token not equal the '.' ");
       }
   }

    private void reportError(Token token , String correctError) {
        System.err.println("Error with : "+token.getTokenName() + " at line : " + token.getLineNumber() +" Excpected Error : " + correctError);
        System.exit(1);
    }

    private void name() {
        if (isName(token)){
            getToken();
        }
        else {
            reportError(token , "Your Token is Not a Name. The Name must be contain a letter and digits only ");
        }
    }

    private boolean isName(Token token) {
        String tokenName = token.getTokenName();


        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }
        if (Scanner.KEY_WORDS.contains(tokenName)){
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

       System.out.println("token start block : " + token.getTokenName());
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

       System.out.println("Exist from Block with token : " + token.getTokenName());

    }

    private void stmtList() {
        statement() ;
        System.out.println("Start with stmt list = " + token.getTokenName());
        while (token.getTokenName().equals(";")){
            getToken();
            statement();
        }
        System.out.println("exist from statment with token : " + token.getTokenName());
    }

    private void statement() {
       System.out.println("Token from statement : "+ token.getTokenName() + " with Type = " + token.getType());

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
           System.out.println("MAAAAAAAAAAAAARS");
           reportError(token , "Your Token is Not equal \";\" ");
       }

        System.out.println("Token from statement : "+ token.getTokenName());

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
                reportError(token , "Missing Semicolon After Variable Item");
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
          System.out.println("error 3 ");

          reportError(token , "Unexpected Token for Data Type must be (integer , real , char) ");
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
        return tokenName.matches("\\d+");  // Regex to match one or more digits
    }
    private boolean isRealValue(Token token) {
        String tokenName = token.getTokenName();
        return tokenName.matches("\\d+\\.\\d+");  // Regex to match real numbers with a decimal point
    }
}
