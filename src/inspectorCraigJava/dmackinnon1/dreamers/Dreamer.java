package dmackinnon1.dreamers;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

/**
 * On the isle of dreams, folks are either Diurnal or Nocturnal.
 * On this island, people are lucid in their dreaming, and their dreaming state
 * is continuous with their awake state.
 * Diurnal people are always correct when they are awake, and wrong when they are asleep.
 * Nocturnal people are always correct when they are asleep, and wrong when they are awake.
 * All islanders are always truthful in the sense that they say what they think is the truth
 * - they do not intentionally lie.
 */

public class Dreamer {

    public String dreamer;
    public Dreamer(String dreamer){
        this.dreamer = dreamer;
    }

    public Proposition isAwake(){
        return new Proposition(dreamer +"a");
    }

    public Proposition isDiurnal(){
        return new Proposition(dreamer +"d");
    }

    public Proposition isAsleep() {
        return isAwake().negate();
    }

    public Proposition isNocturnal() {
        return isDiurnal().negate();
    }

    public List<Phrase> phrasesFrom(Phrase p){
        Intersection aDiurnalAwake = new Intersection(isAwake(),isDiurnal());
        Intersection aDiurnalAsleep = new Intersection(isAsleep(), isDiurnal());
        Intersection aNocturnalAwake = new Intersection(isAwake(), isNocturnal());
        Intersection aNocturnalAsleep = new Intersection(isAsleep(),isNocturnal());
        List<Phrase> r = new ArrayList<Phrase>();
        r.add(new Implication(aDiurnalAwake,p));
        r.add(new Implication(aDiurnalAsleep, p.negate()));
        r.add(new Implication(aNocturnalAwake,p.negate()));
        r.add(new Implication(aNocturnalAsleep,p));
        return r;
    }

}

