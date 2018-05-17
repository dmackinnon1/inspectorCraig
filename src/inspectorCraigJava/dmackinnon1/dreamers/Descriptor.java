package dmackinnon1.dreamers;

/*
 * Describes a problem, used in generating a dataset of all possible treasure/tiger puzzles
 * for a given phrase list.
 */
public class Descriptor {


    public int islanderA_statement = 0;
    public int islanderB_statement = 0;
    public int solvable = 0; // -1 = not solvable, 1=partially solvable, 2=completely solvable

    public int puzzle_number = 0;

    public Descriptor(int pn, int a, int b, int s){
        puzzle_number = pn;
        islanderA_statement = a;
        islanderB_statement = b;
        solvable = s;
    }

    public static String headerRow(){
        return "puzzle, islanderA, islanderB, solvability";
    }

    public String toString(){
        String r = "" + puzzle_number + ", ";
        r += islanderA_statement + ", ";
        r += islanderB_statement + ", ";
        r += solvable;
        return r;
    }

}
