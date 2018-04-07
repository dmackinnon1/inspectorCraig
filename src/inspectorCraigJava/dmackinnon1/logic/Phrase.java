package dmackinnon1.logic;

import java.util.Collection;

/**
 * A phrase is a logical statement, such as A or B, A -> B, A, not A, etc.
 */
public interface Phrase {

    void addTo(Collection<Phrase> list);

    String internalToString();

    Phrase negate();

    boolean satisfies(Phrase phrase);

    Phrase resolve(Phrase phrase);

    Phrase bind(String a, String x);
}
