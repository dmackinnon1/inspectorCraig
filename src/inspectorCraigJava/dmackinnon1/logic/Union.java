package dmackinnon1.logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Models the union of two (or more) propositions A union B.
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

    @Override
    public int hashCode() {
        return this.phrases.hashCode()*1000;
    }

    @Override
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

    @Override
    public boolean satisfies(Phrase p) {
        return phrases.contains(p.negate());
    }

    @Override
    public Union clone() {
        Union n = new Union();
        n.phrases.addAll(this.phrases);
        return n;
    }

    @Override
    public Phrase resolve(Phrase p){
        if (satisfies(p)){
            Union n = this.clone();
            n.phrases.remove(p.negate());
            if (n.phrases.size()==1){
                return n.phrases.get(0);
            } else {
                return n;
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString(){
        return "\"" + internalToString() +"\"";
    }

    @Override
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

    @Override
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
    @Override
    public Phrase negate() {
        List<Phrase> negated = new ArrayList<Phrase>();
        for (Phrase p:this.phrases) {
            negated.add(p.negate());
        }
        return new Intersection(negated.toArray(new Phrase[negated.size()]));
    }

    @Override
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
