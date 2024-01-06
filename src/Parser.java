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

}
