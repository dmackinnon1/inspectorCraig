package dmackinnon1.puzzles;
import dmackinnon1.craig.BaseGenerator;
import dmackinnon1.logic.Problem;
import dmackinnon1.craig.Series1Generator;
import dmackinnon1.craig.Series7Generator;

import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Entry point for generating json files containing puzzle descriptions.
 * Todo: write a method for generating 4 phrse problems based on Scratchpad#good4Problems
 */

public class PuzzleWriter {
    public static String FILENAME_3_PUZZLES = "data/craig3.json";
    public static String FILENAME_4_PUZZLES = "data/craig4.json";

    public static void main(String[] input) throws Exception {

        System.out.println("generating 3 phrase problems...");
        Path file = Paths.get(System.getProperty("user.dir"), FILENAME_3_PUZZLES);
        List<String> jsons = threePhrasesProblems();
        Files.write(file, jsons, Charset.forName("UTF-8"));

        System.out.println("generating 4 phrase problems...");
        file = Paths.get(System.getProperty("user.dir"), FILENAME_4_PUZZLES);
        jsons = fourPhrasesProblems();
        Files.write(file, jsons, Charset.forName("UTF-8"));

    }

    public static List<String> threePhrasesProblems(){
        Series1Generator g = new Series1Generator(3);
        Series7Generator g7 = new Series7Generator(3);
        List<Problem> ps = g.generateAll();
        ps.addAll(g7.generateAll());
        List<String> s = new ArrayList<String>();
        s.add("[");
        int count = 0;
        for (Problem p: ps){
            if (count < ps.size()-1) {
                s.add("\t" + p.toJson() + ",");
            } else {
                s.add("\t" + p.toJson());
            }
            count ++;
        }
        s.add("]");
        return s;
    }

    public static List<String> fourPhrasesProblems(){
        Series1Generator g = new Series1Generator(4);
        Series7Generator g7 = new Series7Generator(4);
        List<Problem> ps = g.generateAll();
        ps.addAll(g7.generateAll());
        List<Problem> augmented = BaseGenerator.augmentProblems(ps);
        List<String> s = new ArrayList<String>();
        s.add("[");
        int count = 0;
        for (Problem p: augmented){
            if (count < augmented.size()-1) {
                s.add("\t" + p.toJson() + ",");
            } else {
                s.add("\t" + p.toJson());
            }
            count ++;
        }
        s.add("]");
        return s;
    }
}
