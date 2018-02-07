package dmackinnon1.logic;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;

/**
 * Solver is the main logic engine that will combine phrases and satisfiers to
 * reduce them down and obtain the results that are generated by the inputs provided.
 *
 * A set of phrases (propositions) and satisfiers (implications, etc) are provided.
 * Calling the evaluate() method will apply the phrases to the implications to obtain
 * additional phrases (processing may add and remove satisfiers). So, the method used
 * could be described as "forward chaining."
 *
 * The solution is the modified list of phrases. There is no guarantee of consistency,
 * but consistency can be checked afterwards (isConsistent()).
 *
 */

public class Solver {
    public Set<Phrase> inputPhrases ;
    public Set<Phrase> inputSatisfiers;
    public List<Proposition> props;
    public int recursionLevel = 1;

    public Solver(){
        this.inputPhrases = new HashSet<>();
        this.inputSatisfiers = new HashSet<>();
    }

    public Solver(List<Proposition> props, Collection<Phrase> ip, Collection<Phrase> is){
        this();
        this.props = props;
        this.inputPhrases.addAll(ip);
        this.inputSatisfiers.addAll(is);
    }

    public Solver propositions(List<Proposition> props){
        this.props = props;
        return this;
    }

    public void recursionLevel(int r){
        this.recursionLevel = r;
    }

    protected String indent() {
        String indent = "-";
        for (int i = 0; i < recursionLevel; i++){
            indent += "-";
        }
        return indent;
    }

    public Solver clone(){
        return new Solver(this.props, this.inputPhrases, this.inputSatisfiers);
    }

    void copyInto(Solver s){
        this.inputPhrases = s.inputPhrases;
        this.inputSatisfiers = s.inputSatisfiers;
    }

    public Solver addPhrase(Phrase p) {
        this.inputPhrases.add(p);
        return this;
    }

    public Solver addSatisfier(Phrase s) {
        this.inputSatisfiers.add(s);
        return this;
    }

    public void separate(){
        Set<Phrase> newPhrases = new HashSet<>();
        for (Phrase p: this.inputPhrases){
            if (p instanceof Proposition) {
                newPhrases.add(p);
            } else {
                this.addSatisfier(p);            }
        }
        Set<Phrase> satisfiers = new HashSet<>();
        for (Phrase p: this.inputSatisfiers){
            if (p instanceof Proposition) {
                newPhrases.add(p);
            } else {
                satisfiers.add(p);            }
        }
        this.inputPhrases = newPhrases;
        this.inputSatisfiers = satisfiers;
    }


    protected boolean checkForContradictions(Proposition p){
       Solver c = this.clone();
       c.inputPhrases.add(p);
       c.evaluate();
       if (!c.isConsistent()){
           addPhrase(p.negate());
           return true;
       }
       return false;
    }

    protected void checkForContradictions(){
        for(Proposition p: this.props){
            boolean result = checkForContradictions(p);
            if(!result){
                checkForContradictions(p.negate());
            }
        }
    }

    public void traverse(){
        Set<Phrase> newPhrases = new HashSet<>();
        Set<Phrase> removable = new HashSet<>();
        for (Phrase s: this.inputSatisfiers){
            for (Phrase p: this.inputPhrases){
                if (s.satisfies(p)){
                    s.resolve(p).addTo(newPhrases);
                    removable.add(s);
                }
            }
            //special union case
            if (s instanceof Union) {
                Union u = (Union)s;
                traverseUnion(u, newPhrases);
            }
        }
        this.inputSatisfiers.removeAll(removable);
        this.inputPhrases.addAll(newPhrases);
        this.separate();
    }

    public void traverseUnion(Union u, Collection<Phrase> newPhrases){
        if (u.phrases.size() == 0) {
            return;
        }
        if (u.phrases.size() == 1){
            newPhrases.add(u.phrases.get(0));
            return;
        }
        //satisfiers without the union
        Set<Phrase> others = new HashSet<>(this.inputSatisfiers);
        others.remove(u);

        boolean first = true;
        Set<Phrase> intersection = new HashSet<>();
        for(Phrase p: u.phrases){

            Set<Phrase> pcopy = new HashSet<>(this.inputPhrases);
            pcopy.add(p);
            Solver singular = new Solver(this.props, pcopy, others);
            singular.recursionLevel(this.recursionLevel ++);
            singular.evaluate();
            if (first){
                intersection.addAll(singular.inputPhrases);
                first = false;
            } else {
                intersection.retainAll(singular.inputPhrases);
            }
        }
        newPhrases.addAll(intersection);
    }



    public void solve(){
        checkForContradictions();
        evaluate();
    }

    public void evaluate() {
        Set<Phrase> phraseCopy = new HashSet<>();
        Set<Phrase> satisfierCopy = new HashSet<>();
        this.separate();
        if(!this.isConsistent()) return;
        while ((!phraseCopy.equals(this.inputPhrases))
                || (!satisfierCopy.equals(inputSatisfiers))) {
            phraseCopy = new HashSet<>(this.inputPhrases);
            satisfierCopy = new HashSet<Phrase>(this.inputSatisfiers);
            this.traverse();
            this.separate();
        }
    }

    public boolean isConsistent(){
        Set<Phrase> others;
        for (Phrase p: this.inputPhrases){
            if (p instanceof Proposition) {
                others = new HashSet<>(this.inputPhrases);
                others.remove(p);
                Proposition np = ((Proposition) p).negate();
                for (Phrase q: others) {
                    if (q.equals(np)) return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        return "p: " + this.inputPhrases + ", s: " +this.inputSatisfiers;
    }
}
