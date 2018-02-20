package dmackinnon1.tiger;

import dmackinnon1.logic.*;
import java.util.ArrayList;
import java.util.List;

public class Generator {


    List<Phrase> phrases(Proposition a, Proposition b) {
        List<Phrase> phrases = new ArrayList<>();
        Proposition x = a;
        Proposition y = b;
        phrases.add(x);
        phrases.add(y);
        phrases.add(new Union(x, y));
        phrases.add(new Intersection(x, y));
        phrases.add(x.negate());
        phrases.add(y.negate());
        phrases.add(new Union(x.negate(), y.negate()));
        phrases.add(new Intersection(x.negate(), y.negate()));
        phrases.add(new Union(x, y.negate()));
        phrases.add(new Union(x.negate(), y));
        phrases.add(new Intersection(x, y.negate()));
        phrases.add(new Intersection(x.negate(), y));
        //either both have treasure or both have tigers
        phrases.add(new Union(new Intersection(x, y), new Intersection(x.negate(), y.negate())));
        //one room has treasure, the other has tiger
        phrases.add(new Union(new Intersection(x, y.negate()), new Intersection(x.negate(), y)));
        return phrases;
    }


    public List<Problem> generate() {
        List<Problem> problems = new ArrayList<>();
        Proposition door1Prop = new Proposition("D1");
        Proposition door2Prop = new Proposition("D2");
        List<Phrase> phrases = phrases(door1Prop, door2Prop);
        int count = 0;
        for (Phrase p : phrases) {
            for (Phrase q : phrases) {
                Door door1 = new Door(door1Prop, p);
                Door door2 = new Door(door2Prop, q);
                Problem problem = new Problem(door1, door2);
                problem.solve();
                if (problem.isGood()) {
                    count ++;
                    problem.setDescription("Puzzle " + count);
                    problems.add(problem);
                }
            }
        }
        return problems;
    }
}
