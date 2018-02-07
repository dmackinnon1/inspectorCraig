package dmackinnon1.logic;

import java.util.Collection;
/**
 * A Proposition models a simple proposition: "Socrates is mortal"
 * or "A is guilty".
 * A proposition can be negated.
 */

public class Proposition implements Phrase {
    public String symbol;
    public boolean sign;

    public Proposition (String symbol){
        this.symbol = symbol;
        sign = true;
    }

    public Proposition (String symbol, boolean sign){
        this(symbol);
        this.sign = sign;
    }

    public void addTo(Collection<Phrase> list) {
        list.add(this);
    }

    public int hashCode(){
        int hash = 0;
        if (sign) {
            hash = 1;
        }
        return (hash + symbol.hashCode()*100);
    }

    public String toString(){
       return "\"" + internalToString() +"\"";
    }

    public String internalToString(){
        String toString = "";
        if (!this.sign){
            toString += "-";
        }
        toString += this.symbol;
        return toString;
    }

    public Proposition clone(){
        return new Proposition(this.symbol, this.sign);
    }

    public boolean equals(Object o){
        if (o instanceof Proposition){
            Proposition c = (Proposition) o;
            return (c.symbol.equals(this.symbol) && (c.sign == this.sign));
        } else {
            return false;
        }
    }

    public Proposition negate() {
        Proposition n = this.clone();
        n.sign = !n.sign;
        return n;
    }

    public boolean satisfies(Phrase phrase){
        return this.equals(phrase);
    }

    public Phrase resolve(Phrase phrase){
        if (this.satisfies(phrase)) return this;
        return null;
    }

    public Phrase bind(String a, String x){
        Proposition p =  this.clone();
        if (this.symbol.equals(x)){
            p.symbol = a;
        }
        return p;
    }
}
