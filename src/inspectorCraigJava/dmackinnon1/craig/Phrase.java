package dmackinnon1.craig;

import java.util.Collection;

/**
 * A phrase is a simple proposition (non standard nomenclature).
 */
public interface Phrase {
    void addTo(Collection<Phrase> list);

    String internalToString();
}
