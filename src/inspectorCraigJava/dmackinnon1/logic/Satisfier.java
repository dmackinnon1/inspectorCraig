package dmackinnon1.logic;

/**
 * A satisfier is a more complex proposition than a phrase (non standard nomenclature).
 * When a satisfier is resolved with a phrase, it returns another phrase.
 * TODO: merge phrases and satisfiers
 */
public interface Satisfier {
    boolean satisfies(Phrase phrase);
    Phrase resolve(Phrase phrase);
}
