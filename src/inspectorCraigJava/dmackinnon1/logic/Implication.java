package dmackinnon1.logic;
import java.util.Collection;

/**
 * Models an implication A -> B.
 */

public class Implication implements Phrase {

    protected Phrase antecedent;
    protected Phrase consequent;

    public Implication(Phrase ant, Phrase cons){
        this.antecedent = ant;
        this.consequent = cons;
    }

    @Override
    public Implication clone(){
        return new Implication(this.antecedent, this.consequent);
    }

    @Override
    public String toString() {
        return "\"" + internalToString() + "\"";
    }

    @Override
    public String internalToString(){
        String toString = "(" + this.antecedent.internalToString();
        toString += " -> ";
        toString += this.consequent.internalToString() + ")";
        return toString;
    }
    @Override
    public int hashCode(){
        return 10000*this.antecedent.hashCode() + this.consequent.hashCode();
    }

    @Override
    public boolean satisfies(Phrase phrase) {
        boolean satisfies = this.antecedent.equals(phrase);
        if (satisfies) return true;
        if (this.antecedent instanceof Union) {
            Union u = (Union) this.antecedent;
            if (u.phrases.contains(phrase)) return true;
        }
        if (intersectionallySatisfies(phrase)) return true;
        //will also handle simple contrapositive
        return reverseSatisfies(phrase);
    }

    protected boolean intersectionallySatisfies(Phrase phrase) {
        if (this.antecedent instanceof Intersection) {
            Intersection i = (Intersection) this.antecedent;
            return i.phrases.contains(phrase);
        }
        return false;
    }

    protected boolean reverseSatisfies(Phrase p) {
        return this.consequent.negate().equals(p);
    }

    @Override
    public Phrase resolve(Phrase p){
        if (reverseSatisfies(p)) return this.antecedent.negate();
        if (intersectionallySatisfies(p)) {
            Intersection i = (Intersection) this.antecedent;
            if(i.phrases.size() < 2) {
                return this.consequent;
            } else {
                Intersection ni = i.without(p);
                if (ni.phrases.size() == 1){
                    return new Implication(ni.phrases.get(0), this.consequent);
                } else {
                    return new Implication(ni, this.consequent);
                }
            }
        }
        if (satisfies(p)) return this.consequent;
        return this;
    }

    /*
     * A -> B is equivalent to !A or B
     */
    public Union asUnion(){
        return new Union(this.antecedent.negate(), this.consequent);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Implication ){
            Implication i = (Implication) o;
            return this.consequent.equals(i.consequent) && this.antecedent.equals(i.antecedent);
        } else if (o instanceof Union){
            Union u = (Union) o;
            return this.asUnion().equals(u);
        } else {
            return false;
        }
    }

    @Override
    public Phrase negate() {
        return this.asUnion().negate();
    }

    @Override
    public void addTo(Collection<Phrase> list) {
        list.add(this);
    }

    @Override
    public Phrase bind(String a, String x) {
        return new Implication(this.antecedent.bind(a,x), this.consequent.bind(a,x));
    }

    @Override
    public boolean isContradictory() {
        return this.antecedent.equals(this.consequent.negate());
    }

}
