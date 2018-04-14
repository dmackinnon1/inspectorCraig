package dmackinnon1.craig;
import dmackinnon1.logic.*;
import java.util.List;

public class Craig3SuspectGenerator implements Generator {

    public List<PuzzleJSON> generate(){
        List<PuzzleJSON> puzzles = new Series1Generator(3).generate();
        puzzles.addAll(new Series7Generator(3).generate());
        return puzzles;
    }
}
