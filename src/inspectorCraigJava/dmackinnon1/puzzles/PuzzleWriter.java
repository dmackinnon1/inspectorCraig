package dmackinnon1.puzzles;
import dmackinnon1.craig.*;
import dmackinnon1.logic.*;
import dmackinnon1.tiger.*;
import dmackinnon1.dreamers.*;
import dmackinnon1.test.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for generating json files containing puzzle descriptions.
 */

public class PuzzleWriter {
    public static String FILENAME_3_PUZZLES = "data/craig3.json";
    public static String FILENAME_4_PUZZLES = "data/craig4.json";
    public static String FILENAME_TIGER_PUZZLES = "data/tiger.json";
    public static String FILENAME_TIGER_INSCRIPTIONS = "report/inscriptions.csv";
    public static String FILENAME_TIGER_REPORT = "report/tiger_report.csv";
    public static String FILENAME_DREAMER_PUZZLES = "data/dreamers.json";
    public static String FILENAME_DREAMER_REPORT = "report/dreamer_report.csv";

    public static void main(String[] input) throws Exception {
        if (input.length > 0 && input[0].trim().equals("test")) {
            TestSet1.main(null);
            TestSet2.main(null);
            TestSet3.main(null);
            TestSet4.main(null);
        } else if (input.length > 0 && input[0].trim().equals("report")){
            System.out.println("writing out tiger/treasure inscriptions...");
            Path file = Paths.get(System.getProperty("user.dir"), FILENAME_TIGER_INSCRIPTIONS);
            Files.write(file, tigerInscriptions(), Charset.forName("UTF-8"));

            System.out.println("writing out tiger/treasure report data...");
            file = Paths.get(System.getProperty("user.dir"), FILENAME_TIGER_REPORT);
            Files.write(file, tigerReportData(), Charset.forName("UTF-8"));

            System.out.println("writing out dreamers report data...");
            file = Paths.get(System.getProperty("user.dir"), FILENAME_DREAMER_REPORT);
            Files.write(file, dreamerReportData(), Charset.forName("UTF-8"));


        }else {
            writePuzzleProblems("generating Inspector Craig 3 Suspect puzzles...",
                    FILENAME_3_PUZZLES, new Craig3SuspectGenerator());

            writePuzzleProblems("generating Inspector Craig 4 Suspect puzzles...",
                    FILENAME_4_PUZZLES, new Craig4SuspectGenerator());

            writePuzzleProblems("generating Tiger & Treasure puzzles...",
                    FILENAME_TIGER_PUZZLES, new TigerGenerator());

            writePuzzleProblems("generating Isle of Dreams puzzles...",
                    FILENAME_DREAMER_PUZZLES, new DreamersGenerator());
        }
    }

    public static void writePuzzleProblems(String message, String filename, Generator generator){
        System.out.println(message);
        List<String> jsonArray = jsonArrayAsList(generator.generate());
        Path file = Paths.get(System.getProperty("user.dir"), filename);
        try {
            Files.write(file, jsonArray, Charset.forName("UTF-8"));
        } catch (IOException ioe){
            System.out.println("Problem writing to file: "  + file);
            ioe.printStackTrace();
        }
    }

    public static List<String> tigerReportData(){
        return new TigerGenerator().descriptors();
    }

    public static List<String> dreamerReportData(){
        return new DreamersGenerator().descriptors();
    }

    public static List<String> jsonArrayAsList(List<PuzzleJSON> puzzleJSONs){
        List<String> s = new ArrayList<>();
        s.add("[");
        int count = 0;
        for (PuzzleJSON p: puzzleJSONs){
            if (count < puzzleJSONs.size()-1) {
                s.add("\t" + p.toJSON() + ",");
            } else {
                s.add("\t" + p.toJSON());
            }
            count ++;
        }
        s.add("]");
        System.out.println("problems generated: " + count);
        return s;

    }

    /*
     * Used in data analysis of tiger problems
     */
    public static List<String> tigerInscriptions() {
        TigerGenerator g = new TigerGenerator();
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

}
