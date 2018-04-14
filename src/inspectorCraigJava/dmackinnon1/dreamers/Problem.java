package dmackinnon1.dreamers;
import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class Problem implements PuzzleJSON{

    Dreamer dreamerA;
    Dreamer dreamerB;

    boolean isConsistent = true;

    List<Phrase> dreamerAPhrases = new ArrayList<Phrase>();
    List<Phrase> dreamerBPhrases = new ArrayList<Phrase>();
    List<Phrase> solution = new ArrayList<Phrase>();
    List<Phrase> internalPhrases = new ArrayList<Phrase>();
    String description;

    public Problem(Dreamer a, Dreamer b){
        dreamerA = a;
        dreamerB = b;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }


    public Problem addAPhrase(Phrase p){
        dreamerAPhrases.add(p);
        return this;
    }

    public Problem addBPhrase(Phrase p){
        dreamerBPhrases.add(p);
        return this;
    }

    private String phrase1(Proposition a1){
        boolean isNegative = !a1.sign;
        if (a1.symbol.contains("a")){
            if (isNegative) {
                return "I am asleep.";
            } else {
                return "I am awake.";
            }
        } else {
            if (isNegative) {
                return "I am nocturnal.";
            } else {
                return "I am diurnal.";
            }
        }
    }

    private String phrase2(Proposition a2, Dreamer other){
        boolean isNegative = !a2.sign;
        if (a2.symbol.contains("a")){
            if (isNegative) {
                return other.dreamer + " is asleep.";
            } else {
                return other.dreamer + " is awake.";
            }
        } else {
            if (isNegative) {
                return other.dreamer+ " is nocturnal.";
            } else {
                return other.dreamer + " is diurnal.";
            }
        }
    }

    private String dreamerAphrase1(){
        return phrase1((Proposition) dreamerAPhrases.get(0));
    }

    private String dreamerBphrase1(){
        return phrase1((Proposition) dreamerBPhrases.get(0));
    }

    private String dreamerAphrase2(){
        return phrase2((Proposition) dreamerAPhrases.get(1), dreamerB);
    }

    private String dreamerBphrase2(){
        return phrase2((Proposition) dreamerBPhrases.get(1), dreamerA);
    }

    public Problem solve(){
        List<Proposition> solverProps = new ArrayList<Proposition>();
        solverProps.add(dreamerA.isAwake());
        solverProps.add(dreamerA.isDiurnal());
        solverProps.add(dreamerB.isAwake());
        solverProps.add(dreamerB.isDiurnal());

        List<Phrase> solverSatisfiers = new ArrayList<Phrase>();
        for (Phrase p: dreamerAPhrases){
            solverSatisfiers.addAll(dreamerA.phrasesFrom(p));
        }
        for (Phrase p: dreamerBPhrases){
            solverSatisfiers.addAll(dreamerB.phrasesFrom(p));
        }
        this.internalPhrases.addAll(solverSatisfiers);

        Solver solver = new Solver(solverProps, new ArrayList<Phrase>(), solverSatisfiers);
        solver.solve();
        isConsistent = solver.isConsistent();

        this.solution.addAll(solver.inputPhrases);
        return this;
    }

    public boolean isConsistent(){
        return isConsistent;
    }

    public List<Phrase> solution(){
        return solution;
    }
    /*
    {
    "description":"puzzle 1",
    "solution": ["Ad", "-Aa", "-Bd", "Ba"],
    "solution_text":"A is diurnal and asleep, B is nocturnal and awake",
    "islander_A": "I am awake. B is asleep",
    "islander_B": "I am asleep. A is awake",
    "all_propositions": ["Ad*As->Ad", "..."],
    "is_consistent": true
    }
     */

    public String toJSON(){
        String json = "{";
        json += "\"description\": \"" + this.getDescription() +"\"";
        json += ", \"is_consistent\": \"" + this.isConsistent() +"\"";
        json += ", \"islanderA_statement1\": \"" + dreamerAphrase1() +"\"";
        json += ", \"islanderA_statement2\": \"" +  dreamerAphrase2()+"\"";
        json += ", \"islanderB_statement1\": \"" + dreamerBphrase1() +"\"";
        json += ", \"islanderB_statement2\": \"" + dreamerBphrase2() +"\"";
        json += ", \"solution_text\": \"" + this.solutionText() +"\"";
        json += ", \"all_phrases\": " + this.allPhrases();
        json += "}";
        return json;
    }

    private List<Phrase> allPhrases(){
        return this.internalPhrases;
    }

    private String solutionText(){
        if (!isConsistent()) {
            return "No solution.";
        }
        String s = "";
        for (Phrase p: solution){
            Proposition prop = (Proposition) p;
            Dreamer d = dreamerA;
            if (prop.symbol.contains("B")) {
                d = dreamerB;
            }
            s += phrase2(prop, d);
            s += " ";
        }
        return s.trim();
    }

    public String toString(){
        String s = "A says: ";
        for (Phrase p: dreamerAPhrases){
            s += p;
            s += " ";
        }
        s += ", B says: ";
        for (Phrase p: dreamerBPhrases){
            s += p;
            s += " ";
        }
        s += ", so really: ";
        for (Phrase p: solution){
            s += p;
            s += " ";
        }
        return s;
    }



}
