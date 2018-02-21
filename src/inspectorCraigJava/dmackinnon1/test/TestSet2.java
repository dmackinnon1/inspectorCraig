package dmackinnon1.test;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class TestSet2 {

    TestSuite set1 = new TestSuite();

    public void setup() {

        final Proposition a = new Proposition("A");
        final Proposition b = new Proposition("B");
        final Proposition c = new Proposition("C");

        final List<Proposition> props = new ArrayList<>();
        props.add(a);
        props.add(b);
        props.add(c);

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

        //--------------
        Test test6 = new Test() {
            public void run() {
                Solver k = s.clone();
                k.addSatisfier(new Implication(a,b));
                k.addSatisfier(new Implication(b,c));
                k.addSatisfier(a);
                k.solve();
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from implication");
                assertTrue(k.inputPhrases.contains(c), "Solver failed to deduce from composed implication");
            }
        };
        test6.name = "solver, composed implication";
        set1.add(test6);

         //--------------
        Test test7 = new Test() {
            public void run() {
                Solver k = s.clone();
                Intersection aIb = new Intersection(a,b);
                Intersection naInb = new Intersection(a.negate(), b.negate());
                Union u = new Union(aIb,naInb);
                Implication imp1 = new Implication(a,u);
                Implication imp2 = new Implication(a.negate(),a);
                k.addSatisfier(imp1);
                k.addSatisfier(imp2);
                System.out.println(k);
                System.out.println(k);
                assertTrue(k.inputPhrases.contains(a), "Solver failed to deduce from contradiction");
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce nested union and intersection");
            }
        };
        test7.name = "solver, nested intersection";
            set1.add(test7);

        Test test8 = new Test() {
            public void run() {
                Solver k = s.clone();
                Intersection aIb = new Intersection(a,b);
                k.addSatisfier(aIb);
                k.solve();
                assertTrue(k.inputPhrases.contains(a), "Solver failed to deduce from intersection");
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce from intersection");
            }
        };
        test8.name = "solver, basic intersection";
        set1.add(test8);

    }

    public static void main(String[] args){
        System.out.println(">> Running TestSet2 - simple deductions <<");
        TestSet2 ts1 = new TestSet2();
        ts1.setup();
        ts1.set1.run();
    }

}
