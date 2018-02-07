package dmackinnon1.test;

public abstract class Test {

    protected boolean hasError = false;
    protected String message = "";
    public String name = "";
    public boolean hasError() {
        return hasError;
    }

    public String message() {
        return message;
    }

    public void name(String name){
        this.name = name;
    }

    public abstract void run();

    public void assertTrue(boolean assertion, String message){
        this.hasError = this.hasError || !assertion;
        if (this.hasError) {
            this.message += " " + message;
        }
    }

}
