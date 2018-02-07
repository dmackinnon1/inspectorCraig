package dmackinnon1.test;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class TestSet2 {

    TestSuite set1 = new TestSuite();

    public void setup() {

        final Proposition a = new Proposition("A");
        final Proposition b = new Proposition("B");
        final List<Proposition> props = new ArrayList<>();
        props.add(a);
        props.add(b);

        Solver s = new Solver().propositions(props);

        Test test1 = new Test() {
          public void run() {
              Solver k = s.clone();
              k.addSatisfier(new Union(a,b));
              k.addSatisfier(a.negate());
              k.solve();
              assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from union");
          }
        };
        test1.name = "solver, basic union case";
        set1.add(test1);

        //--------------
        Test test2 = new Test() {
            public void run() {
                Solver k = s.clone();
                k.addSatisfier(new Implication(a,b));
                k.addSatisfier(a);
                k.solve();
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from implication");
            }
        };
        test2.name = "solver, basic implication case";
        set1.add(test2);

        //--------------
        Test test3 = new Test() {
            public void run() {
                Solver k = s.clone();
                k.addSatisfier(new Implication(a,b));
                k.addSatisfier(a.negate());
                k.solve();
                assertTrue(k.inputPhrases.contains(a.negate()), "Solver failed to deduce from contrapositive");
            }
        };
        test3.name = "solver, basic contrapositive case";
        set1.add(test3);

        //--------------
        Test test4 = new Test() {
            public void run() {
                Solver k = s.clone();
                k.addSatisfier(new Implication(a,b));
                k.addSatisfier(new Union(a,b));
                k.solve();
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from union and implication");
            }
        };
        test4.name = "solver, basic union + implication case";
        set1.add(test4);

        //--------------
        Test test5 = new Test() {
            public void run() {
                Solver k = s.clone();
                k.addSatisfier(new Implication(a,a.negate()));
                k.addSatisfier(new Union(a,b));
                k.solve();
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from union and contradiction");
            }
        };
        test5.name = "solver, union and contradiction";
        set1.add(test5);
    }


    public static void main(String[] args){
        System.out.println(">> Running TestSet2 <<");
        TestSet2 ts1 = new TestSet2();
        ts1.setup();
        ts1.set1.run();
    }




}
