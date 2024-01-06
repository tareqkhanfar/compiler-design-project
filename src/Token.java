public class Token {


    private String type ;
    private String tokenName ;

    private long lineNumber ;

    public Token(String type, String tokenName, long lineNumber) {
        this.type = type;
        this.tokenName = tokenName;
        this.lineNumber = lineNumber;
    }
    public Token() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }
}
