package dmackinnon1.tiger;

/*
 * Describes a problem, used in generating a dataset of all possible treasure/tiger puzzles
 * for a given phrase list.
 */
public class Descriptor {


    public int door1_inscription_number = 0;
    public int door2_inscription_number = 0;
    public int tiger_count = 0;
    public int treasure_count = 0;
    public boolean consistent = false;
    public int puzzle_number = 0;
    public String solution = "";

    public Descriptor(int pn, int d1,int d2, int tr, int tg, boolean c, String sol){
        puzzle_number = pn;
        door1_inscription_number = d1;
        door2_inscription_number = d2;
        tiger_count = tg;
        treasure_count = tr;
        consistent = c;
        solution = sol;
    }

    public static String headerRow(){
        return "puzzle, door_1, door_2, treasure_count, tiger_count, consistent, solution";
    }

    public String toString(){
        String r = "" + puzzle_number + ", ";
        r += door1_inscription_number + ", ";
        r += door2_inscription_number + ", ";
        r += treasure_count + ", ";
        r += tiger_count + ", ";
        r += consistent + ", ";
        r += solution;
        return r;
    }

}
