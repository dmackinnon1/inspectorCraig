package dmackinnon1.craig;

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

}
