package dmackinnon1.craig;

import java.util.List;

/**
 * Just a class to try stuff out.
 */
public class Scratchpad {

    public static void main(String args[]){

        System.out.println(PuzzleWriter.threePhrasesProblems());

    }

    public static void good4Problems(){
        Series7Generator g = new Series7Generator(4);
        int inconsistentCount = 0;
        int maxProblemSize = 0;
        int minProblemSize = 100;
        int pSCount = 0;
        List<Problem> ps = g.generate72v3();
        for(Problem p:ps){
            g.improve(p);
            if (!p.isConsistent()) inconsistentCount++;
            if (p.problemSize() > maxProblemSize ) maxProblemSize = p.problemSize();
            if (p.problemSize() < minProblemSize ) minProblemSize = p.problemSize();
            pSCount += p.problemSize();
            p.description = "consistent: "+ p.isConsistent();
        }
        boolean first = true;
        System.out.println("[");
        for (Problem p:ps){
            if (!first){
                System.out.print(",");
                System.out.println();
            }
            first = false;
            System.out.print(p);
        }
        System.out.println();
        System.out.println("]");
        System.out.println("problems: " + ps.size());
        System.out.println("inconsistent: " + inconsistentCount);
        System.out.println("largest problem size: "+ maxProblemSize);
        System.out.println("min problem size: " + minProblemSize);
        System.out.println("average problem size: "+ pSCount/ps.size());
    }

    public static void random3Problem(){
        Series7Generator g = new Series7Generator(3);
        Problem p = g.randomProblem();
        System.out.println(p.toJson());
        System.out.println(p.isConsistent());
    }

    public static void random2Problem(){
        Series7Generator g = new Series7Generator(2);
        Problem p = g.randomProblem();
        System.out.println(p.toJson());
        System.out.println(p.isConsistent());
    }
    public static void all3series(){
        Series7Generator g = new Series7Generator(3);
        List<Problem> ps = g.generateAll();
        System.out.println("[");
        boolean first = true;
        for (Problem p: ps){
            if (!first){
                System.out.print(",");
                System.out.println();
            }
            first = false;
            System.out.print("\t" + p.toJson());
        }
        System.out.println();
        System.out.println("]");
        System.out.println("Number of problems: " + ps.size());
    }


    //requires contradiction checking
    public static void problem78(){
        Series7Generator g = new Series7Generator(3);
        List<Problem> ps = g.generate78();
        for (Problem p: ps){
            System.out.println(p.toJson());
        }
    }

    //requires contradiction checking
    public static void problem78v2(){
        Series7Generator g = new Series7Generator(3);
        List<Problem> ps = g.generate78v2();
        for (Problem p: ps){
            System.out.println(p.toJson());
        }
    }
    //requires contradiction checking
    public static void problem78v3(){
        Series7Generator g = new Series7Generator(3);
        List<Problem> ps = g.generate78v3();
        for (Problem p: ps){
            System.out.println(p.toJson());
        }
    }

}
