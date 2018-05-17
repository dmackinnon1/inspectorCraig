package dmackinnon1.dreamers;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class DreamersGenerator implements Generator{

    static Dreamer dreamerA = new Dreamer("A");
    static Dreamer dreamerB = new Dreamer("B");

    public List<PuzzleJSON> generate() {
        Phrase[] aList = {dreamerA.isAwake(), dreamerA.isAwake().negate(),
                dreamerA.isDiurnal(), dreamerA.isDiurnal().negate()};
        Phrase[] bList = {dreamerB.isAwake(), dreamerB.isAwake().negate(),
                dreamerB.isDiurnal(), dreamerB.isDiurnal().negate()};

        List<PuzzleJSON> problems = new ArrayList<>();
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

    public List<String> descriptors(){
        List<String> statements = new ArrayList<String>();
        List<String> descriptors = new ArrayList<String>();
        List<PuzzleJSON> problems = generate();
        int count = 0;
        descriptors.add(Descriptor.headerRow());
        for (PuzzleJSON pj : problems){
            Problem p = (Problem) pj;
            String aPhrase = p.dreamerAGenericPhrase();
            String bPhrase = p.dreamerBGenericPhrase();
            if (!statements.contains(aPhrase)) statements.add(aPhrase);
            if (!statements.contains(bPhrase)) statements.add(bPhrase);
            int solvable = 0;
            if (!p.isConsistent()) {
                solvable = -1;
            } else if (p.isPartial()){
                solvable = 1;
            } else if (p.isFull()){
                solvable = 2;
            }
            Descriptor d = new Descriptor(count, statements.indexOf(aPhrase), statements.indexOf(bPhrase), solvable);
            descriptors.add(d.toString());
            count ++;
        }
        System.out.println("statements");
        int scount = 0;
        for (String s: statements){
            System.out.println("" + scount+ ": " + s);
            scount++;
        }
        System.out.println("------------------------");

        return descriptors;
    }

    public static void main(String[] args){
        DreamersGenerator g = new DreamersGenerator();

        List<String> descriptors = g.descriptors();
        System.out.println("----------------------------");
        for(String d: descriptors){
            System.out.println(d);
        }
        }


}
