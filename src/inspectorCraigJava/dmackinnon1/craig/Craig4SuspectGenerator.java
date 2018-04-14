package dmackinnon1.craig;

import dmackinnon1.logic.Generator;
import dmackinnon1.logic.PuzzleJSON;

import java.util.List;

public class Craig4SuspectGenerator extends BaseGenerator implements Generator {

    public List<PuzzleJSON> generate(){
        List<PuzzleJSON> puzzles = new Series1Generator(4).generate();
        puzzles.addAll(new Series7Generator(4).generate());
        return augmentProblems(puzzles);
    }


}
