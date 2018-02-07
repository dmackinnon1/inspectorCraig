package dmackinnon1.logic;

import java.util.Collection;

/**
 * A phrase is a simple proposition (non standard nomenclature).
 */
public interface Phrase {

    void addTo(Collection<Phrase> list);

    String internalToString();

    Phrase negate();

    boolean satisfies(Phrase phrase);

    Phrase resolve(Phrase phrase);

    Phrase bind(String a, String x);


}
