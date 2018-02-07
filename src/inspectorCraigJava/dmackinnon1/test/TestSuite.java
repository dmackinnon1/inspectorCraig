package dmackinnon1.test;

import java.util.List;
import java.util.ArrayList;

public class TestSuite {

    public List<Test> tests = new ArrayList<Test>();
    public List<Test> fails = new ArrayList<Test>();

    public void add(Test test) {
        this.tests.add(test);
    }

    public void run(){
        for (Test test : tests){
            System.out.println("running test: " + test.name);
            test.run();
            if (test.hasError()){
                System.out.println(" - encountered error: " + test.message());
                fails.add(test);
            }
        }
        if (fails.size() == 0) {
            System.out.println("ran " + tests.size() + " tests with no errors");
        } else {
            System.out.println("Errors were encountered: ");
            for (Test test : fails) {
                System.out.println(test.name + ": " + test.message());
            }
        }


    }

}
