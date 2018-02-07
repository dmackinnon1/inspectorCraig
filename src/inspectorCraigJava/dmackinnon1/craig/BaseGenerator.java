package dmackinnon1.craig;

import dmackinnon1.logic.Proposition;
import dmackinnon1.logic.Solver;
import dmackinnon1.logic.Union;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseGenerator {
    protected static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static Random rnd = new Random();
    public List<Proposition> props = new ArrayList<Proposition>();

    public BaseGenerator (int propSize) {
        if (propSize > alphabet.length())
            throw new RuntimeException("Number of propositions cannot exceed " + alphabet.length());
        for (int i = 0; i< propSize; i++){
            props.add(new Proposition("" + alphabet.charAt(i)));
        }
    }

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

    public static List<Problem> augmentProblems(List<Problem> ps){
        int inconsistentCount = 0;
        List<Problem> augmented = new ArrayList<Problem>();
        int count = 0;
        int tooBigCount = 0;
        for(Problem p:ps){
            int initialSize = p.problemSize();
            improve(p);
            if (!p.isConsistent()) {
                inconsistentCount++;
            } else if(p.problemSize() > 2*initialSize){
               tooBigCount ++;
            } else {
                p.description = "4" + p.description;
                augmented.add(p);
            }
        }
        System.out.println("inconsistent: " + inconsistentCount);
        System.out.println("problems considered too large: " + tooBigCount);
        System.out.println("problems generated: " + augmented.size());
        return augmented;
    }
}
