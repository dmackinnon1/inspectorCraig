package dmackinnon1.logic;

import java.util.Collection;
/**
 * A Proposition models a simple proposition: "Socrates is mortal"
 * or "A is guilty".
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

    @Override
    public void addTo(Collection<Phrase> list) {
        list.add(this);
    }

    @Override
    public int hashCode(){
        int hash = 0;
        if (sign) {
            hash = 1;
        }
        return (hash + symbol.hashCode()*100);
    }

    @Override
    public String toString(){
       return "\"" + internalToString() +"\"";
    }

    @Override
    public String internalToString(){
        String toString = "";
        if (!this.sign){
            toString += "-";
        }
        toString += this.symbol;
        return toString;
    }

    @Override
    public Proposition clone(){
        return new Proposition(this.symbol, this.sign);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Proposition){
            Proposition c = (Proposition) o;
            return (c.symbol.equals(this.symbol) && (c.sign == this.sign));
        } else {
            return false;
        }
    }

    @Override
    public Proposition negate() {
        Proposition n = this.clone();
        n.sign = !n.sign;
        return n;
    }

    @Override
    public boolean satisfies(Phrase phrase){
        return this.equals(phrase);
    }

    @Override
    public Phrase resolve(Phrase phrase){
        if (this.satisfies(phrase)) return this;
        return null;
    }

    @Override
    public Phrase bind(String a, String x){
        Proposition p =  this.clone();
        if (this.symbol.equals(x)){
            p.symbol = a;
        }
        return p;
    }
}
