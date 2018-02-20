package dmackinnon1.logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Models the union of two (or more) propositions A union B.
 * Todo handle union of more complex propositions
 */

public class Union implements Phrase {

    protected List<Phrase> phrases;

    public Union(Phrase ... p){
        this.phrases = Arrays.asList(p);
    }

    public Union without(Phrase p){
        Union other = this.clone();
        other.phrases.remove(p);
        return other;
    }

    public Union addPhrase(Phrase x) {
        this.phrases.add(x);
        return this;
    }

    public int hashCode() {
        return this.phrases.hashCode()*1000;
    }

    public void addTo(Collection<Phrase> list){
        if (this.phrases.size() == 1){
            list.add(this.phrases.get(0));
        } else {
            list.add(this);
        }
        return;
    }

    public Union() {
        this.phrases = new ArrayList<Phrase>();
    }

    public boolean satisfies(Phrase p) {
        if (p instanceof Proposition){
            Proposition pp = (Proposition) p;
            Proposition np = pp.negate();
            return phrases.contains(np);
        } else {
            return false; // for now, a Union can only satisfy a Proposition
        }
    }

    public Union clone() {
        Union n = new Union();
        n.phrases.addAll(this.phrases);
        return n;
    }

    public Phrase resolve(Phrase p){
        if (satisfies(p)){
            Union n = this.clone();
            Proposition pp = (Proposition) p;
            n.phrases.remove(pp.negate());
            if (n.phrases.size()==1){
                return n.phrases.get(0);
            } else {
                return n;
            }
        } else {
            return null;
        }
    }

    public String toString(){
        return "\"" + internalToString() +"\"";
    }

    public String internalToString() {
        return "[" + phrasesString() + "]";
    }

    public String phrasesString(){
        String pString = "";
        boolean first = true;
        for (Phrase p: phrases){
            if (!first) {
                pString += ", ";
            }
            first = false;
            pString += p.internalToString();
        }
        return pString;
    }

    public boolean equals(Object other) {
        if (other instanceof Union){
            Union u = (Union) other;
            return u.phrases.containsAll(this.phrases) && this.phrases.containsAll(u.phrases);
        } else {
            return false;
        }
    }
    /*
     * Negation of a Union follows DeMorgan's law
     */
    public Phrase negate() {
        List<Phrase> negated = new ArrayList<Phrase>();
        for (Phrase p:this.phrases) {
            negated.add(p.negate());
        }
        return new Intersection(negated.toArray(new Phrase[negated.size()]));
    }

    public Phrase bind(String a, String x) {
        List<Phrase> bound = new ArrayList<Phrase>();
        for (Phrase p : this.phrases) {
            bound.add(p.bind(a, x));
        }
        return new Union(bound.toArray(new Phrase[bound.size()]));
    }

    public List<Phrase>  getPhrases(){
        return phrases;
    }
}
