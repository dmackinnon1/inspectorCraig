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
                Intersection aIb = new Intersection(a,b); //a*b
                Intersection naInb = new Intersection(a.negate(), b.negate()); //an*bn
                Union u = new Union(aIb,naInb); //union of a*b and an*bn
                Implication imp1 = new Implication(a,u);
                Implication imp2 = new Implication(a.negate(),a);
                k.addSatisfier(imp1);
                k.addSatisfier(imp2);
                k.solve();
                assertTrue(k.isConsistent(), "Solver failed to get consistent solution " + k.toString());
                assertTrue(k.inputPhrases.contains(a), "Solver failed to deduce from contradiction " + k.toString());
                assertTrue(k.inputPhrases.contains(b), "Solver failed to deduce nested union and intersection "+ k.toString());
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

        /*
         "door1_propositions": [
            "(D1 -> [-D1, -D2])",
            "(-D1 -> <D1, D2>)"
            ],
            "door2_propositions": [
            "(D2 -> <[-D1, -D2], [D1, D2]>)",
            "(-D2 -> [<D1, D2>, <-D1, -D2>])"
           ],
            "solution": [
            "D1",
            "-D2"
            ],
            should actually be no solution
         */
        Test test9 = new Test() {
            public void run() {
                Solver k = s.clone();
                Implication i1 = new Implication(a, new Union(a.negate(), b.negate()));
                Implication i2 = new Implication(a.negate(), new Intersection(a, b));
                Implication i4 = new Implication(b.negate(), new Union(new Intersection(a,b),new Intersection(a.negate(),b.negate())));
                k.addSatisfier(i1);
                k.addSatisfier(i2);
                k.addSatisfier(i4);
                k.solve();
                assertTrue(!k.isConsistent(), "Solver failed to find contradiction " + k.toString());

            }
        };
        test9.name = "solver, late contradiction";
        set1.add(test9);

        Test test10 = new Test() {
            public void run() {
                Solver k = s.clone();
                Implication i1 = new Implication(a, a.negate());
                k.addSatisfier(i1);
                k.solve();
                //assertTrue(!k.isConsistent(), "Solver failed to find contradiction " + k.toString());
                assertTrue(k.inputPhrases.contains(a.negate()), "Solver failed to find contradiction " + k.toString());

            }
        };
        test10.name = "solver, contradictory implication";
        set1.add(test10);
    }

    public static void main(String[] args){
        System.out.println(">> Running TestSet2 - simple deductions <<");
        TestSet2 ts1 = new TestSet2();
        ts1.setup();
        ts1.set1.run();
    }


}
