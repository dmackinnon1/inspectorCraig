package dmackinnon1.craig;

import dmackinnon1.logic.Proposition;
import dmackinnon1.logic.Solver;
import dmackinnon1.logic.Union;
import dmackinnon1.logic.Generator;
import dmackinnon1.logic.PuzzleJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseGenerator implements Generator {
    protected static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static Random rnd = new Random();
    public List<Proposition> props = new ArrayList<Proposition>();

    public BaseGenerator(){
        super();
    }

    public BaseGenerator (int propSize) {
        if (propSize > alphabet.length())
            throw new RuntimeException("Number of propositions cannot exceed " + alphabet.length());
        for (int i = 0; i< propSize; i++){
            props.add(new Proposition("" + alphabet.charAt(i)));
        }
    }

    public abstract List<PuzzleJSON> generate();

    protected Union totalUnion(){
        return new Union(this.props.toArray(new Proposition[this.props.size()]));
    }

    public static Problem improve(Problem p){
        while(p.shouldImprove() && p.isConsistent()){
            p.improve();
        }
        return p;
    }

    public Problem randomProblem(){
        Solver s = new Solver().propositions(this.props);
        s.addSatisfier(this.totalUnion());
        Problem p = new Problem(this.props, s);

        while(p.shouldImprove() && p.isConsistent()){
            p.improve();
        }
        return p;
    }

    public static List<PuzzleJSON> augmentProblems(List<PuzzleJSON> ps){
        int inconsistentCount = 0;
        List<PuzzleJSON> augmented = new ArrayList<>();
        int count = 0;
        int tooBigCount = 0;
        for(PuzzleJSON p:ps){
            Problem converted = (Problem) p;
            int initialSize = converted.problemSize();
            improve(converted);
            if (!converted.isConsistent()) {
                inconsistentCount++;
            } else if(converted.problemSize() > 2*initialSize){
               tooBigCount ++;
            } else {
                converted.description = "4" + converted.description;
                augmented.add(converted);
            }
        }
        return augmented;
    }
}
