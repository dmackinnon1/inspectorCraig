package dmackinnon1.craig;
import dmackinnon1.logic.*;

import java.util.*;

/**
 * Models a logic problem.
 * propositions - the atoms of the problem
 * premises - statements involving the propositons using logical connectives
 * conclusion - results of calculating with the premises;
 */

public class Problem {

    List<Proposition> propositions;
    Set<Phrase> premisePhrases = new HashSet<>();
    Set<Phrase> premiseSatisfiers = new HashSet<>();
    Set<Phrase> solution = new HashSet<>();
    public String description = "";

    Solver solver;

    public Problem(List<Proposition> propositions, Solver solver){
        this.propositions = propositions;
        this.solver = solver;
        this.premisePhrases.addAll(this.solver.inputPhrases);
        this.premiseSatisfiers.addAll(this.solver.inputSatisfiers);
    }

    protected void resetSolver(){
        Solver solver = new Solver(this.propositions, this.premisePhrases, this.premiseSatisfiers);
        this.solver = solver;
    }

    public Problem description(String s){
        this.description = s;
        return this;
    }

    public Problem solve(){
        resetSolver();
        this.solver.solve();
        this.solution.addAll(this.solver.inputPhrases);
        return this;
    }

    public boolean isConsistent(){
        return this.solver.isConsistent();
    }

    protected String premiseStrings(){
        List<String> psList = new ArrayList<String>();
        for (Phrase p: this.premisePhrases){
            psList.add(p.toString());
        }
        for (Phrase s: this.premiseSatisfiers){
            psList.add(s.toString());
        }
        return psList.toString();
    }

    public String toJson(){
        String json = "{";
        json += "\"propositions\": " + this.propositions; //json array
        json += ", \"premises\": " + this.premiseStrings(); //json array;
        json += ", \"solution\": " + this.solution; //json array
        json += ", \"description\": \"" + this.description +"\"";
        json += "}";
        return json;
    }

    public String toString() {
        return this.toJson();
    }


    protected Proposition randomProposition(){
        Random rnd = new Random();
        return this.propositions.get(rnd.nextInt(this.propositions.size()));
    }

    public void addRandomNegation(){
        this.premisePhrases.add(randomProposition().negate());
    }

    public void addRandomAssertion(){
        this.premisePhrases.add(randomProposition());
    }

    protected void addRandomNegationOrAssertion(){
        Random rnd = new Random();
        if (rnd.nextBoolean()){
            addRandomNegation();
        } else {
            addRandomAssertion();
        }
    }

    protected List<Proposition> randomList(int i) {

        Random rnd = new Random();
        List<Proposition> stack = new ArrayList<Proposition>();
        for (Proposition p: this.propositions){
            stack.add(p.clone());
        }
        for (Proposition p : stack) {
            if (rnd.nextBoolean()) p.sign = false;
        }
        Collections.shuffle(stack);
        return stack.subList(0, i);
    }


    protected void addRandomImplication(){
        List<Proposition> list = randomList(2);
        Proposition p = list.get(0);
        Proposition q = list.get(1);
        this.premiseSatisfiers.add(new Implication(p,q));
    }

    protected void addRandomImplicationUnion(){
        List<Proposition> list = randomList(3);
        Proposition p = list.get(0);
        Proposition q = list.get(1);
        Proposition r = list.get(2);
        this.premiseSatisfiers.add(new Implication(p,new Union(q,r)));
    }

    protected void addRandomUnionImplication(){
        List<Proposition> list = randomList(3);
        Proposition p = list.get(0);
        Proposition q = list.get(1);
        Proposition r = list.get(2);
        this.premiseSatisfiers.add(new Implication(new Union(p,q),r));
    }

    protected void addRandomImplicationIntersection(){
        List<Proposition> list = randomList(3);
        Proposition p = list.get(0);
        Proposition q = list.get(1);
        Proposition r = list.get(2);
        this.premiseSatisfiers.add(new Implication(p, new Intersection(q,r)));
    }

    public int solutionCount() {
        return this.solution.size();
    }

    public int problemSize() {
        return this.premiseSatisfiers.size() + this.premisePhrases.size();
    }

    public boolean shouldImprove(){
        return solutionCount() < (this.propositions.size()+1)/2;
    }

    //add an implication, plus something else
    private void addRandomStatements(){
        Random rnd = new Random();
        int randomInt = rnd.nextInt(3);
        if (randomInt == 0){
            addRandomUnionImplication();
        } else if (randomInt == 1){
            addRandomImplicationUnion();
        } else {
            addRandomImplicationIntersection();
        }
    }

    public boolean improve() {
        this.solve();
        if (!this.isConsistent()) return false;
        //first attempt
        if (shouldImprove()){
            addRandomImplication();
        } else{
            return true;
        }
        //second attempt
        this.solve();
        if (!this.isConsistent()) return false;
        if (shouldImprove()){
            addRandomStatements();
        } else {
            return true;
        }
        //third attempt
        this.solve();
        if (!this.isConsistent()) return false;
        if (shouldImprove()){
            addRandomStatements();
        } else {
            return true;
        }
        //fourth attempt
        this.solve();
        if (!this.isConsistent()) return false;
        if (shouldImprove()){
            addRandomImplication();
        } else {
            return true;
        }

        return !shouldImprove();
    }
}
