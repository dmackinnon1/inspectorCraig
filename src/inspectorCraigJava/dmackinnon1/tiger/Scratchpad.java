package dmackinnon1.tiger;

import dmackinnon1.logic.*;
import java.util.List;
import java.util.ArrayList;


public class Scratchpad {

    public static void main(String[] args) {

        Generator g = new Generator();
        List<Problem> problems = g.generate();
        List<String> s = new ArrayList<String>();
        s.add("[");
        int count = 0;
        for (Problem p : problems) {
            if (count < problems.size() - 1) {
                s.add("\t" + p.toJson() + ",");
            } else {
                s.add("\t" + p.toJson());
            }
            count++;
        }
        s.add("]");

        for(String line: s){
            System.out.println(line);
        }

    }
}
