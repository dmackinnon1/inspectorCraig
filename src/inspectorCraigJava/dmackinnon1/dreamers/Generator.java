package dmackinnon1.dreamers;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class Generator {

    static Dreamer dreamerA = new Dreamer("A");
    static Dreamer dreamerB = new Dreamer("B");

    public List<Problem> generate() {
        Phrase[] aList = {dreamerA.isAwake(), dreamerA.isAwake().negate(),
                dreamerA.isDiurnal(), dreamerA.isDiurnal().negate()};
        Phrase[] bList = {dreamerB.isAwake(), dreamerB.isAwake().negate(),
                dreamerB.isDiurnal(), dreamerB.isDiurnal().negate()};

        List<Problem> problems = new ArrayList<Problem>();
        int count = 0;
        for (Phrase a1 : aList) {
            for (Phrase b1 : bList) {
                for (Phrase b2 : bList) {
                    for (Phrase a2 : aList) {
                        Problem p = new Problem(dreamerA, dreamerB);
                        p.setDescription("Problem " + ++count);
                        p.addAPhrase(a1);
                        p.addAPhrase(b1);
                        p.addBPhrase(b2);
                        p.addBPhrase(a2);
                        p.solve();
                        problems.add(p);
                    }
                }
            }
        }
        return problems;
    }

    public static void main(String[] args){
        Generator g = new Generator();
        List<Problem> probs = g.generate();
        for(Problem p: probs){
            System.out.println(p.toJson());
        }
    }
}
