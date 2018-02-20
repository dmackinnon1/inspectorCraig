package dmackinnon1.tiger;
import dmackinnon1.logic.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Models a problem of the type from "The Lady or the Tiger" where
 * the solver must identify the contents the rooms behind two doors.
 * The rooms may contain treasure, or a tiger.
 * The rooms have inscriptions on them, which may be true or false.
 * If door 1 leads to treasure, its statement is true; otherwise it is false.
 * If door 2 leads to a tiger, its statement is true; otherwise it is false.
 * Based on the clues, the solver must decide if the door leads to treasure,
 * a tiger, or if the contents of the room cannot be determined based on
 * the statements.
 */
public class Problem {
    Door door1, door2;
    Solver solver;
    String description;

    public Problem(Door d1, Door d2){
        this.door1 = d1;
        this.door2 = d2;
        door1.trueOnTreasure = true;
        door2.trueOnTreasure = false;
    }

    public boolean solve(){
        List<Phrase> solution = new ArrayList<>();
        List<Phrase> doorImplications = new ArrayList<>();
        doorImplications.add(door1.getTigerImplication());
        doorImplications.add(door1.getTreasureImplication());
        doorImplications.add(door2.getTigerImplication());
        doorImplications.add(door2.getTreasureImplication());

        List<Proposition> props = new ArrayList<>();
        props.add(door1.prop);
        props.add(door2.prop);

        solver = new Solver(props, solution, doorImplications);
        solver.solve();

        return isGood();
    }

    public void setDescription(String text){
        this.description = text;
    }

    public boolean isGood(){
        if (solver == null) throw new RuntimeException("Programming Error: Problem has not been solved.");
        return solver.isConsistent() && solver.inputPhrases.size() > 0;
    }

    public String solutionText(){
        String solution ="";
        if (solver.inputPhrases.contains(door1.prop)) {
            solution += "Door 1 has treasure.";
        } else if (solver.inputPhrases.contains(door1.prop.negate())) {
            solution += "Door 1 has a tiger.";
        } else {
            solution += "Door 1 has unknown contents.";
        }
        solution += " ";
        if (solver.inputPhrases.contains(door2.prop)) {
            solution += "Door 2 has treasure.";
        } else if (solver.inputPhrases.contains(door2.prop.negate())) {
            solution += "Door 2 has a tiger.";
        } else {
            solution += "Door 2 has unknown contents.";
        }
        return solution;
    }

    public String toJson(){
        String json = "{";
        json += "\"door1_clue\": \"" + this.door1.translation() +"\"";
        json += ", \"door2_clue\": \"" + this.door2.translation() +"\"";
        json += ", \"door1_propositions\": " + this.door1.getImplications();
        json += ", \"door2_propositions\": " + this.door2.getImplications();
        json += ", \"solution\": " + this.solver.inputPhrases ;
        json += ", \"solution_text\": \"" + this.solutionText() +"\"";
        json += ", \"description\": \"" + this.description +"\"";
        json += "}";
        return json;
    }

    public int hashCode(){
        int hash = 0;
        return this.door1.hashCode()*1000 + this.door2.hashCode();
    }

    public boolean equals(Object o){
        if (o instanceof Problem){
            Problem p = (Problem) o;
            return p.hashCode() == this.hashCode();
        } else {
            return false;
        }
    }

}
