package dmackinnon1.craig;
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
    }

    public static List<String> threePhrasesProblems(){
        Generator g = new Generator(3);
        List<Problem> ps = g.all3Series();
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
}
