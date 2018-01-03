package dmackinnon1.craig;

public interface Util {
    static boolean debug = false;
    default void log(String s){
        if (debug) System.out.println(s);
    }
}
