package dmackinnon1.puzzles;
import dmackinnon1.craig.BaseGenerator;
import dmackinnon1.craig.Problem;
import dmackinnon1.craig.Series1Generator;
import dmackinnon1.craig.Series7Generator;
import dmackinnon1.logic.Phrase;
import dmackinnon1.logic.Proposition;
import dmackinnon1.test.TestSet1;
import dmackinnon1.test.TestSet2;
import dmackinnon1.test.TestSet3;
import dmackinnon1.tiger.Descriptor;
import dmackinnon1.tiger.Door;
import dmackinnon1.tiger.Generator;

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
    public static String FILEMAME_TIGER_PUZZLES = "data/tiger.json";
    public static String FILENAME_TIGER_INSCRIPTIONS = "report/inscriptions.csv";
    public static String FILENAME_TIGER_REPORT = "report/tiger_report.csv";

    public static void main(String[] input) throws Exception {
        if (input.length > 0 && input[0].trim().equals("test")) {
            TestSet1.main(null);
            TestSet2.main(null);
            TestSet3.main(null);
        } else if (input.length > 0 && input[0].trim().equals("report")){
            System.out.println("writing out tiger/treasure inscriptions...");
            Path file = Paths.get(System.getProperty("user.dir"), FILENAME_TIGER_INSCRIPTIONS);
            Files.write(file, tigerInscriptions(), Charset.forName("UTF-8"));

            System.out.println("writing out tiger/treasure report data...");
            file = Paths.get(System.getProperty("user.dir"), FILENAME_TIGER_REPORT);
            Files.write(file, tigerReportData(), Charset.forName("UTF-8"));

        }else {
            System.out.println("generating 3 phrase problems...");
            Path file = Paths.get(System.getProperty("user.dir"), FILENAME_3_PUZZLES);
            List<String> jsons = threePhrasesProblems();
            Files.write(file, jsons, Charset.forName("UTF-8"));

            System.out.println("generating 4 phrase problems... (this may take a while)");
            file = Paths.get(System.getProperty("user.dir"), FILENAME_4_PUZZLES);
            jsons = fourPhrasesProblems();
            Files.write(file, jsons, Charset.forName("UTF-8"));

            System.out.println("generating tiger & treasure problems...");
            file = Paths.get(System.getProperty("user.dir"), FILEMAME_TIGER_PUZZLES);
            jsons = tigerProblems();
            Files.write(file, jsons, Charset.forName("UTF-8"));
        }
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
        System.out.println("problems generated: " + count);
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

    public static List<String> tigerProblems(){
        Generator g = new Generator();
        List<dmackinnon1.tiger.Problem> problems = g.generate();
        List<String> s = new ArrayList<>();
        s.add("[");
        int count = 0;
        for (dmackinnon1.tiger.Problem p : problems) {
            if (count < problems.size() - 1) {
                s.add("\t" + p.toJson() + ",");
            } else {
                s.add("\t" + p.toJson());
            }
            count++;
        }
        s.add("]");
        System.out.println("problems generated: " + count);
        return s;
    }

    public static List<String> tigerInscriptions() {
        Generator g = new Generator();
        Proposition door1Prop = new Proposition("D1");
        Proposition door2Prop = new Proposition("D2");
        List<Phrase> phrases = g.phrases(door1Prop, door2Prop);
        int count = 0;
        List<String> doorText = new ArrayList<>();
        doorText.add("inscription");
        for (Phrase p: phrases){
            Door door = new Door(door1Prop, p);
            doorText.add(door.translation());
        }
        return doorText;
    }

    public static List<String> tigerReportData(){
        return new Generator().descriptors();
    }

}
