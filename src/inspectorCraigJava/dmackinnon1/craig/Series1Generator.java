package dmackinnon1.craig;


import dmackinnon1.logic.Implication;
import dmackinnon1.logic.Proposition;
import dmackinnon1.logic.Solver;
import dmackinnon1.logic.PuzzleJSON;

import java.util.ArrayList;
import java.util.List;

public class Series1Generator extends BaseGenerator {
    public Series1Generator(int propSize) {
        super(propSize);
    }

    public List<PuzzleJSON> generate() {
        List<PuzzleJSON> problems = generate11();
        problems.addAll(generate12());
        problems.addAll(generate13());
        problems.addAll(generate14());
        return problems;
    }
    /*
    110) AvBvC
    !A -> !B
    C -> B
    A-> C

     */
    public List<PuzzleJSON> generate11() {
        String description = "110";
        List<PuzzleJSON> problems = new ArrayList<>();
        int counter = 1;
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    Implication npnq = new Implication(p.negate(),q.negate());
                    Implication rq = new Implication(r,q);
                    Implication pr = new Implication(p, r);
                    Solver s = new Solver().propositions(this.props)
                            .addSatisfier(totalUnion())
                            .addSatisfier(npnq)
                            .addSatisfier(rq)
                            .addSatisfier(pr);

                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
    AvBvC
    A -> !B
    C -> B
    B
    !A->C
    ⇒ !ABC
     */
    public List<PuzzleJSON> generate12() {
        String description = "120";
        List<PuzzleJSON> problems = new ArrayList<>();
        int counter = 1;
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    Implication pnq = new Implication(p,q.negate());
                    Implication npr = new Implication(p.negate(), r);
                    Solver s = new Solver().propositions(this.props)
                            .addSatisfier(totalUnion())
                            .addSatisfier(pnq)
                            .addSatisfier(npr)
                            .addPhrase(q);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
    !A -> B
    C -> B
    !B
     */
    public List<PuzzleJSON> generate13() {
        String description = "130";
        List<PuzzleJSON> problems = new ArrayList<>();
        int counter = 1;
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    Implication npq = new Implication(p.negate(),q);
                    Implication rq = new Implication(r,q);
                    Solver s = new Solver().propositions(this.props)
                            .addSatisfier(totalUnion())
                            .addSatisfier(npq)
                            .addSatisfier(rq)
                            .addPhrase(q.negate());
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
   !A -> B
   B-> C
   !B
   !C->!A
    */
    public List<PuzzleJSON> generate14() {
        String description = "140";
        List<PuzzleJSON> problems = new ArrayList<>();
        int counter = 1;
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    Implication npq = new Implication(p.negate(),q);
                    Implication qr = new Implication(q,r);
                    Implication nrnp = new Implication(r.negate(),p.negate());
                    Solver s = new Solver().propositions(this.props)
                            .addSatisfier(totalUnion())
                            .addSatisfier(npq)
                            .addSatisfier(qr)
                            .addSatisfier(nrnp)
                            .addPhrase(q.negate());
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }
}
