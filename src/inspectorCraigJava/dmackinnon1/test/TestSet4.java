package dmackinnon1.test;

import dmackinnon1.logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/* Tests for the Isle of Dreams
 * A and B are two islanders
 * Aa means A is awake
 * not Aa means A is asleep
 *
 * Ad means A is diurnal
 * not Ad means A is nocturnal
 *
 * If A says a statement S then
 * Aa*Ad -> S (diurnal believe true things when awake)
 * Aa*~Ad -> ~S (nocturnal are wrong when awake)
 * ~Aa*Ad -> ~S (diurnal are wrong when asleep)
 * ~Aa*~Ad -> S (nocturnal believe true things when sleeping)
 *
 */
public class TestSet4 {

    TestSuite set1 = new TestSuite();

    public void setup() {

        final Proposition aa = new Proposition("Aa");
        //final Proposition ba = new Proposition("Ba");
        final Proposition ad = new Proposition("Ad");
        //final Proposition bd = new Proposition("Bd");


        final List<Proposition> props = new ArrayList<>();
        props.add(aa);
        //props.add(ba);
        props.add(ad);
        //props.add(bd);

        Solver s = new Solver().propositions(props);

        final Intersection aDiurnalAwake = new Intersection(aa,ad);
        final Intersection aDiurnalAsleep = new Intersection(ad, aa.negate());
        final Intersection aNocturnalAwake = new Intersection(ad.negate(), aa);
        final Intersection aNocturnalAsleep = new Intersection(ad.negate(),aa.negate());

        Function<Proposition,List<Phrase>> islandMappings;
        islandMappings = p -> {
            List<Phrase> r = new ArrayList<Phrase>();
            r.add(new Implication(aDiurnalAwake,p));
            r.add(new Implication(aDiurnalAsleep, p.negate()));
            r.add(new Implication(aNocturnalAwake,p.negate()));
            r.add(new Implication(aNocturnalAsleep,p));
            return r;
        };

        Test test1 = new Test() {
          public void run() {
              Solver k = s.clone();
              for (Phrase p: islandMappings.apply(aa)) {
                  k.addSatisfier(p);
              }
              k.solve();
              assertTrue(k.inputPhrases.contains(ad), "Solver failed to deduce diurnal from believing awake");
          }
        };
        test1.name = "dreamers1: diurnal from awake";
        set1.add(test1);

        Test test2 = new Test() {
            public void run() {
                Solver k = s.clone();
                for (Phrase p: islandMappings.apply(ad)) {
                    k.addSatisfier(p);
                }
                k.solve();
                assertTrue(k.inputPhrases.contains(aa), "Solver failed to deduce awake from believing diurnal");
            }
        };
        test2.name = "dreamers2: awake from diurnal";
        set1.add(test2);

        Test test3 = new Test() {
            public void run() {
                Solver k = s.clone();
                for (Phrase p: islandMappings.apply(aa.negate())) {
                    k.addSatisfier(p);
                }
                k.solve();
                assertTrue(k.inputPhrases.contains(ad.negate()), "Solver failed to deduce nocturnal from believing asleep");
            }
        };
        test3.name = "dreamers3: nocturnal from asleep";
        set1.add(test3);

        Test test4 = new Test() {
            public void run() {
                Solver k = s.clone();
                for (Phrase p: islandMappings.apply(ad.negate())) {
                    k.addSatisfier(p);
                }
                k.solve();
                assertTrue(k.inputPhrases.contains(aa.negate()), "Solver failed to deduce asleep from believing nocturnal");
            }
        };
        test4.name = "dreamers4: asleep from nocturnal";
        set1.add(test4);
    }

    public static void main(String[] args){
        System.out.println(">> Running TestSet4 - the isle of dreams <<");
        TestSet4 ts1 = new TestSet4();
        ts1.setup();
        ts1.set1.run();
    }


}
