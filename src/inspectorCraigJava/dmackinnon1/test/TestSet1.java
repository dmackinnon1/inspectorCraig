package dmackinnon1.test;

import dmackinnon1.logic.Implication;
import dmackinnon1.logic.Intersection;
import dmackinnon1.logic.Proposition;
import dmackinnon1.logic.Union;

public class TestSet1 {

    TestSuite set1 = new TestSuite();

    public void setup() {

        final Proposition a = new Proposition("A");
        final Proposition b = new Proposition("B");

        Test test1 = new Test() {
          public void run() {
              Implication ab = new Implication(a, b);
              assertTrue(ab.satisfies(a), "A failed to satisfy A->B");
              assertTrue(ab.resolve(a).equals(b), "A and A->B did not resolve to B");
          }
        };
        test1.name = "implication satisfaction, positive case";
        set1.add(test1);

        //-------------------------
        Test test2 = new Test() {
            public void run() {
                Implication ab = new Implication(a, b);
                assertTrue(ab.satisfies(b.negate()), "!B did not satisfy contrapositive of A->B ");
                assertTrue(ab.resolve(b.negate()).equals(a.negate()), "!B did not resolve to !A in A->B");
            }
        };
        test2.name = "simple contrapositive";
        set1.add(test2);

        //-------------------------
        Test test3 = new Test() {
            public void run() {
                Union ab = new Union(a, b);
                assertTrue(ab.satisfies(b.negate()), "!B did not satisfy AuB");
                assertTrue(ab.resolve(b.negate()).equals(a), "!B did not resolve to A in AuB");
            }
        };
        test3.name = "simple union";
        set1.add(test3);

        //-------------------------
        Test test4 = new Test() {
            public void run() {
                Union ab = new Union(a, b);
                assertTrue(ab.negate().equals(new Intersection(a.negate(), b.negate())), "!(AuB) != A!*!B");
                assertTrue(new Intersection(a, b).negate().equals(new Union(a.negate(), b.negate())), "!(A*B) != A!u!B");
            }
        };
        test4.name = "deMorgan's law";
        set1.add(test4);

        //-------------------------
        Test test5 = new Test() {
            public void run() {
                Implication ab = new Implication(a, b);
                assertTrue(ab.negate().equals(new Intersection(a, b.negate())), "!(A->B) != A*!B");
            }
        };
        test5.name = "negate implication";
        set1.add(test5);
    }


    public static void main(String[] args){
        System.out.println(">> Running TestSet1 <<");
        TestSet1 ts1 = new TestSet1();
        ts1.setup();
        ts1.set1.run();
    }




}
