package dmackinnon1.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * Models a simple intersection of phrases (A and B).
 */

public class Intersection implements Phrase {
    protected List<Phrase> phrases;

    public Intersection(Phrase ... p){
        this.phrases = Arrays.asList(p);
    }

    public Intersection() {
        this.phrases = new ArrayList<Phrase>();
    }

    public String toString(){
        return "\"" + internalToString() +"\"";
    }

    public String internalToString() {
        return "<" + phrasesString() + ">";
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

    public void addTo(Collection<Phrase> list) {
        list.addAll(this.phrases);
    }

    public Intersection clone() {
        Intersection n = new Intersection();
        n.phrases.addAll(this.phrases);
        return n;
    }

    public Intersection without(Phrase p){
        Intersection other = this.clone();
        other.phrases.remove(p);
        return other;
    }

    public int hashCode() {
        return this.phrases.hashCode()*1001;
    }

    public boolean equals(Object other) {
        if (other instanceof Intersection){
            Intersection u = (Intersection) other;
            return u.phrases.containsAll(this.phrases) && this.phrases.containsAll(u.phrases);
        } else {
            return false;
        }
    }

    /*
     * Negation of an Intersection follows DeMorgan's law
     */
    public Phrase negate() {
        List<Phrase> negated = new ArrayList<Phrase>();
        for (Phrase p:this.phrases) {
            negated.add(p.negate());
        }
        return new Union(negated.toArray(new Phrase[negated.size()]));
    }

    public boolean satisfies(Phrase phrase) {
        return this.phrases.contains(phrase);
    }

    public Phrase resolve(Phrase phrase){
        if (satisfies(phrase)) return this;
        return null;
    }

    public Phrase bind(String a, String x){
        List<Phrase> bound = new ArrayList<Phrase>();
        for (Phrase p : this.phrases){
            bound.add(p.bind(a,x));
        }
        return new Intersection(bound.toArray(new Phrase[bound.size()]));
    }

    public List<Phrase>  getPhrases(){
        return phrases;
    }
}
