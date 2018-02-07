package dmackinnon1.logic;
import java.util.Collection;

/**
 * Models an implication A -> B.
 * TODO: handle negation of A->B, more involved phrases, better contrapositive.
 */

public class Implication implements Phrase {

    protected Phrase antecedent;
    protected Phrase consequent;

    public Implication(Phrase ant, Phrase cons){
        this.antecedent = ant;
        this.consequent = cons;
    }

    public Implication clone(){
        return new Implication(this.antecedent, this.consequent);
    }

    public String toString() {
        return "\"" + internalToString() + "\"";
    }

    public String internalToString(){
        String toString = "(" + this.antecedent.internalToString();
        toString += " -> ";
        toString += this.consequent.internalToString() + ")";
        return toString;
    }

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
            if (i.phrases.contains(phrase)){
                return true;
            }
        }
        return false;
    }

    protected boolean reverseSatisfies(Phrase p) {
        if (!(this.antecedent instanceof Proposition)) return false; //can't handle more complex cases
        if (this.consequent instanceof Proposition && p instanceof Proposition) {
            Proposition c = (Proposition) this.consequent;
            Proposition pp = (Proposition) p;
            return c.negate().equals(pp);
        } else {
            return false;
        }
    }

    @Override
    public Phrase resolve(Phrase p){
        if (reverseSatisfies(p)) return ((Proposition) this.antecedent).negate();
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
        return null;
    }

    /*
     * A -> B is equivalent to !A or B
     */
    public Union asUnion(){
        return new Union(this.antecedent.negate(), this.consequent);
    }

    public Phrase negate() {
        return this.asUnion().negate();
    }

    @Override
    public void addTo(Collection<Phrase> list) {
        list.add(this);
    }
}
