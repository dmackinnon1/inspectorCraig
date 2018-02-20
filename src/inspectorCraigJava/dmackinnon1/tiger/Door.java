package dmackinnon1.tiger;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;

public class Door {
    public Phrase phrase;
    public Proposition prop;

    public boolean trueOnTreasure = true;
    public Door(Proposition prop, Phrase phrase ){
        this.phrase = phrase;
        this.prop = prop;
    }

    public Phrase getTreasureImplication(){
        if (trueOnTreasure){
            return new Implication(this.prop, this.phrase);
        } else {
            return new Implication(this.prop, this.phrase.negate());
        }
    }

    public Phrase getTigerImplication(){
        if (trueOnTreasure){
            return new Implication(this.prop.negate(), this.phrase.negate());
        } else {
            return new Implication(this.prop.negate(), this.phrase);
        }
    }

    public List<Phrase> getImplications(){
        List<Phrase> phrases = new ArrayList<Phrase>();
        phrases.add(this.getTreasureImplication());
        phrases.add(this.getTigerImplication());
        return phrases;

    }

    public String simplePropTranslation(Proposition p, String clue){

        boolean treasure = p.sign;
        if (p.symbol == this.prop.symbol){
            clue += "this room has ";
        } else {
            clue += "the other room has ";
        }
        if (treasure) {
            clue += "treasure";
        } else {
            clue += "a tiger";
        }
        return clue;
    }

    public boolean bothPositiveInIntersection(Intersection i){
        Phrase a = i.getPhrases().get(0);
        Phrase b = i.getPhrases().get(1);
        if (a instanceof Proposition && b instanceof Proposition) {
            Proposition pa = (Proposition) a;
            Proposition pb = (Proposition) b;
            if (pa.sign && pb.sign){
                return true;
            }
        }
        return false;
    }

    public boolean bothNegativeInIntersection(Intersection i){
        Phrase a = i.getPhrases().get(0);
        Phrase b = i.getPhrases().get(1);
        if (a instanceof Proposition && b instanceof Proposition) {
            Proposition pa = (Proposition) a;
            Proposition pb = (Proposition) b;
            if (!pa.sign && !pb.sign){
                return true;
            }
        }
        return false;
    }

    public boolean mixedInIntersection(Intersection i) {
        Phrase a = i.getPhrases().get(0);
        Phrase b = i.getPhrases().get(1);
        if (a instanceof Proposition && b instanceof Proposition) {
            Proposition pa = (Proposition) a;
            Proposition pb = (Proposition) b;
            if (pa.sign && pb.sign || !pa.sign && !pb.sign){
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public String simpleUnionTranslation(Union u, String clue){
        Phrase a = u.getPhrases().get(0);
        Phrase b = u.getPhrases().get(1);
        if (a instanceof Proposition && b instanceof Proposition) {
            Proposition pa = (Proposition) a;
            Proposition pb = (Proposition) b;
            if (pa.sign && pb.sign){
                clue += "at least one room has treasure";
            } else if ( !pa.sign && !pb.sign) {
                clue += "at least one room has a tiger";
            } else {
                clue = simplePropTranslation(pa, clue);
                clue +=" or " + simplePropTranslation(pb,"");
            }
            return clue;
        }

        if (a instanceof Intersection && b instanceof Intersection){
            Intersection ia = (Intersection) a;
            Intersection ib = (Intersection) b;
            boolean hasPositive = bothPositiveInIntersection(ia) || bothPositiveInIntersection(ib);
            boolean hasNegative = bothNegativeInIntersection(ia) || bothNegativeInIntersection(ib);
            if(hasPositive && hasNegative){
                clue += "both rooms have treasure or both rooms have a tiger";
            }
            if (mixedInIntersection(ia) && mixedInIntersection(ib)){
                clue += "one room has the treasure and the other has a tiger";
            }
            return clue;
        }

        if (a instanceof Union){
            clue = simpleUnionTranslation((Union)a, clue);
        } else if (a instanceof Intersection) {
            clue = simpleIntersectionTranslation((Intersection)a, clue);
        }
        clue += " or ";
        if (b instanceof Union){
            clue = simpleUnionTranslation((Union)b, clue);
        } else if (b instanceof Intersection) {
            clue = simpleIntersectionTranslation((Intersection)b, clue);
        }
        return clue;
    }

    public String simpleIntersectionTranslation(Intersection i, String clue){
        Phrase a = i.getPhrases().get(0);
        Phrase b = i.getPhrases().get(1);
        if (a instanceof Proposition && b instanceof Proposition) {
            Proposition pa = (Proposition) a;
            Proposition pb = (Proposition) b;
            if (pa.sign && pb.sign){
                clue += "both rooms have treasure";
            } else if ( !pa.sign && !pb.sign) {
                clue += "both rooms have a tiger";
            } else {
                clue = simplePropTranslation(pa, clue);
                clue +=" and " + simplePropTranslation(pb,"");
            }
            return clue;
        }
        if (a instanceof Union){
            clue = simpleUnionTranslation((Union)a, clue);
        } else if (a instanceof Intersection) {
            clue = simpleIntersectionTranslation((Intersection)a, clue);
        }
        clue += " and ";
        if (b instanceof Union){
            clue = simpleUnionTranslation((Union)b, clue);
        } else if (b instanceof Intersection) {
            clue = simpleIntersectionTranslation((Intersection)b, clue);
        }
        return clue;
    }

    public String translation(){
        String clue = "";
        if (this.phrase instanceof Proposition){
            Proposition p = (Proposition) this.phrase;
            return simplePropTranslation(p, clue);
        } else if (this.phrase instanceof Union){
            return simpleUnionTranslation((Union) this.phrase,clue);
        } else if (this.phrase instanceof Intersection){
            return simpleIntersectionTranslation((Intersection) this.phrase,clue);
        }
        return clue;
    }

    public int hashCode(){
        int hash = 0;
        return this.prop.hashCode()*100 + this.phrase.hashCode();
    }

    public boolean equals(Object o){
        if (o instanceof Door){
            Door d = (Door) o;
            return d.hashCode() == this.hashCode();
        } else {
            return false;
        }
    }

}
