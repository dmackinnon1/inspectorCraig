package dmackinnon1.craig;

import dmackinnon1.logic.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Generates logic problems with structure based on those found in chapter 6 of
 * "What is the Name of this Book?" by Raymond Smullyan.
 */

public class Series7Generator extends BaseGenerator {

    public Series7Generator(int propSize) {
        super(propSize);
    }

    public List<Problem> generateAll(){
        List<Problem> problems = generate71();
        problems.addAll(generate71v2());
        problems.addAll(generate71v3());
        problems.addAll(generate72());
        problems.addAll(generate72v2());
        problems.addAll(generate72v3());
        problems.addAll(generate73());
        problems.addAll(generate73v2());
        problems.addAll(generate73v3());
        problems.addAll(generate73v4());
        problems.addAll(generate74());
        problems.addAll(generate74v2());
        problems.addAll(generate78());
        problems.addAll(generate78v2());
        problems.addAll(generate78v3());
        return problems;
    }

    /*
    Case 71 is for 3 suspects, ABC
        p|q|r
        p -> q
        r -> p|q
        result: q

     */
    public List<Problem> generate71() {
        String description = "710";
        List<Problem> problems = new ArrayList<Problem>();
        int counter = 1;
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);
                    Implication pq = new Implication(p, q);
                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication ripq = new Implication(r, u);

                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(pq)
                            .addSatisfier(ripq);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
     A variation on 71 for 3 suspects
        p|q|r
        p -> q
        r -> p|q
        q -> -r
        result: q, -r
     */

    public List<Problem> generate71v2() {
        String description = "711";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);
                    Implication pq = new Implication(p, q);
                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication ripq = new Implication(r, u);
                    Implication qinr = new Implication(q, r.negate());

                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(pq)
                            .addSatisfier(ripq)
                            .addSatisfier(qinr);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
    Another variation on 71 for 3 suspects
        p|q|r
        p -> q
        r -> p|q
        q -> -r
        p -> r
        result: q, -r , -p
     For more than 3, the result is that all assertions are unknown
     */

    public List<Problem> generate71v3() {
        String description = "712";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(q);
                remain2.remove(p);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);
                    Implication pq = new Implication(p, q);
                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication ripq = new Implication(r, u);
                    Implication qinr = new Implication(q, r.negate());
                    Implication pir = new Implication(p,r);

                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(pq)
                            .addSatisfier(ripq)
                            .addSatisfier(qinr)
                            .addSatisfier(pir);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
        p -> q|r
        -q
        result: r, -q
     */
    public List<Problem> generate72() {
        String description = "720";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {

                    Union u = new Union(remain.toArray(new Phrase[remain.size()]));
                    Implication piu = new Implication(p, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addPhrase(q.negate())
                            .addSatisfier(piu);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
      p -> q|r
      -q
      r -> -p
      results: -q, -p, r
   */
    public List<Problem> generate72v2() {
        String description = "721";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    Union u = new Union(remain.toArray(new Phrase[remain.size()]));
                    Implication piu = new Implication(p, u);
                    Implication rinp = new Implication(r, p.negate());
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addPhrase(q.negate())
                            .addSatisfier(piu)
                            .addSatisfier(rinp);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
     p -> q|r
     -q
     r -> p
     results: -q, p, r
  */
    public List<Problem> generate72v3() {
        String description = "722";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    Union u = new Union(remain.toArray(new Phrase[remain.size()]));
                    Implication piu = new Implication(p, u);
                    Implication rip = new Implication(r, p);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addPhrase(q.negate())
                            .addSatisfier(piu)
                            .addSatisfier(rip);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter++;
                }
            }
        }
        return problems;
    }

    /*
    p|q|r
    p-> q
    r-> q
    q -> -p*-r
    p -> -r
    result: q, -p, -r
     */
    public List<Problem> generate73() {
        String description = "730";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();

                    Implication piq = new Implication(p, q);
                    Implication riq = new Implication(r,q);
                    Implication pinr = new Implication(p, r.negate());
                    Implication qinpnr = new Implication(q, new Intersection(p.negate(),r.negate()));
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(piq)
                            .addSatisfier(riq)
                            .addSatisfier(pinr)
                            .addSatisfier(qinpnr);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter++;
                }
            }
        }
        return problems;
    }

    /*
   p|q|r
   p-> q
   r-> q
   p -> -r
   q -> -p
   result: q, -p
    */
    public List<Problem> generate73v2() {
        String description = "731";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();

                    Implication piq = new Implication(p, q);
                    Implication riq = new Implication(r,q);
                    Implication pinr = new Implication(p, r.negate());
                    Implication qinp = new Implication(q, p.negate());
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(piq)
                            .addSatisfier(riq)
                            .addSatisfier(pinr)
                            .addSatisfier(qinp);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }
    /*
     p|q|r
     p-> q
     r-> q
     p -> -r
     q-> r
     result: q, -p, r
      */
    public List<Problem> generate73v3() {
        String description = "732";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();

                    Implication piq = new Implication(p, q);
                    Implication riq = new Implication(r,q);
                    Implication pinr = new Implication(p, r.negate());
                    Implication qir = new Implication(q, r);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(piq)
                            .addSatisfier(riq)
                            .addSatisfier(pinr)
                            .addSatisfier(qir);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
    p|q|r
    p-> q
    r-> q
    p -> -r
    q-> p
    result: q, p, -r
     */
    public List<Problem> generate73v4() {
        String description = "733";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();

                    Implication piq = new Implication(p, q);
                    Implication riq = new Implication(r,q);
                    Implication pinr = new Implication(p, r.negate());
                    Implication qip = new Implication(q, p);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(piq)
                            .addSatisfier(riq)
                            .addSatisfier(pinr)
                            .addSatisfier(qip);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }


    /*
    p*-q ->r
    p->-r
    r->p|q
    result: q
     */

    public List<Problem> generate74() {
        String description = "740";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);

                    Intersection pnq = new Intersection(p, q.negate());
                    Implication npqir = new Implication(pnq,r);
                    Implication pinr = new Implication(p, r.negate());

                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication riu = new Implication(r, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(npqir)
                            .addSatisfier(pinr)
                            .addSatisfier(riu);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
    p*-q ->r
    p->-r
    r->p|q
    q-> p
    result: q, p
     */

    public List<Problem> generate74v2() {
        String description = "741";
        int counter = 0;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);

                    Intersection pnq = new Intersection(p, q.negate());
                    Implication npqir = new Implication(pnq,r);
                    Implication pinr = new Implication(p, r.negate());

                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication riu = new Implication(r, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(npqir)
                            .addSatisfier(pinr)
                            .addSatisfier(riu)
                            .addSatisfier(new Implication(q,p));
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter++;
                }
            }
        }
        return problems;
    }

/*
  p|q|r
  -p | q - > r
   -p -> -r
result: p, by contradiction
 */
    public List<Problem> generate78() {
        String description = "780";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);

                    Union npq = new Union(p.negate(), q);
                    Implication npqir = new Implication(npq,r);
                    Implication npnr = new Implication(p.negate(), r.negate());

                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication riu = new Implication(r, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(npqir)
                            .addSatisfier(npnr);
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter ++;
                }
            }
        }
        return problems;
    }

    /*
  p|q|r
  -p | q - > r
   -p -> -r
   p-> q
result: p, by contradiction, q and r
 */
    public List<Problem> generate78v2() {
        String description = "781";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);

                    Union npq = new Union(p.negate(), q);
                    Implication npqir = new Implication(npq,r);
                    Implication npnr = new Implication(p.negate(), r.negate());

                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication riu = new Implication(r, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(npqir)
                            .addSatisfier(npnr)
                            .addSatisfier(new Implication(p,q));
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter++;
                }
            }
        }
        return problems;
    }

    /*
p|q|r
-p | q - > r
-p -> -r
p-> -q
result: p, by contradiction, -q
*/
    public List<Problem> generate78v3() {
        String description = "782";
        int counter = 1;
        List<Problem> problems = new ArrayList<Problem>();
        for (Proposition p : this.props) {
            List<Proposition> remain = new ArrayList<Proposition>();
            remain.addAll(this.props);
            remain.remove(p);
            for (Proposition q : remain) {
                List<Proposition> remain2 = new ArrayList<Proposition>();
                remain2.addAll(this.props);
                remain2.remove(p);
                remain2.remove(q);
                for (Proposition r : remain2) {
                    List<Proposition> remain3 = new ArrayList<Proposition>();
                    remain3.addAll(this.props);
                    remain3.remove(r);

                    Union npq = new Union(p.negate(), q);
                    Implication npqir = new Implication(npq,r);
                    Implication npnr = new Implication(p.negate(), r.negate());

                    Union u = new Union(remain3.toArray(new Phrase[remain3.size()]));
                    Implication riu = new Implication(r, u);
                    Solver s = new Solver().propositions(this.props).addSatisfier(this.totalUnion())
                            .addSatisfier(npqir)
                            .addSatisfier(npnr)
                            .addSatisfier(new Implication(p,q.negate()));
                    problems.add(new Problem(this.props, s).solve()
                            .description(description + counter));
                    counter++;
                }
            }
        }
        return problems;
    }

}

