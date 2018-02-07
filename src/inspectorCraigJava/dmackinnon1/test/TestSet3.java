package dmackinnon1.test;

import dmackinnon1.logic.Implication;
import dmackinnon1.logic.Intersection;
import dmackinnon1.logic.Proposition;
import dmackinnon1.logic.Union;

public class TestSet3 {

    TestSuite set1 = new TestSuite();

    public void setup() {

        final Proposition a = new Proposition("A");
        final Proposition b = new Proposition("B");

        final Proposition x = new Proposition("X");
        final Proposition y = new Proposition("Y");

        Test test1 = new Test() {
            public void run() {

                assertTrue(x.bind("A","X").equals(a), "binding A to X failed");
            }
        };
        test1.name = "simple bind";
        set1.add(test1);
        //-------------------------------

        Test test2 = new Test() {
            public void run() {

                assertTrue(x.bind("A","Y").equals(x), "not binding A to X failed when it was to go to Y");
            }
        };
        test2.name = "simple non-bind";
        set1.add(test2);
        //-------------------------------

        Test test3 = new Test() {
            public void run() {
                Implication xy = new Implication(x,y);
                Implication ab = new Implication(a,b);
                assertTrue(xy.bind("A","X").bind("B","Y").equals(ab), "binding vars to X->Y failed");
            }
        };
        test3.name = "binding to implication";
        set1.add(test3);
        //-------------------------------

        Test test4 = new Test() {
            public void run() {
                Union xy = new Union(x,y);
                Union ab = new Union(a,b);
                assertTrue(xy.bind("A","X").bind("B","Y").equals(ab), "binding vars to XuY failed");
            }
        };
        test4.name = "binding to union";
        set1.add(test4);
        //-------------------------------

        Test test5 = new Test() {
            public void run() {
                Intersection xy = new Intersection(x,y);
                Intersection ab = new Intersection(a,b);
                assertTrue(xy.bind("A","X").bind("B","Y").equals(ab), "binding vars to X*Y failed");
            }
        };
        test5.name = "binding to intersection";
        set1.add(test5);
        //-------------------------------
    }


    public static void main(String[] args){
        System.out.println(">> Running TestSet3 - bindings <<");
        TestSet3 ts1 = new TestSet3();
        ts1.setup();
        ts1.set1.run();
    }




}
