import java.util.Set;

/**
 * Klasa reprezentuje pojedyncza zaleĹźnoĹÄ funkcyjnÄ X->Y.
 */
public abstract class AbstractFunctionalDependency {
    /**
     * Atrybuty lewej strony zaleĹźnoĹci funkcyjnej (X).
     * @return lewa strona zaleĹźnoĹci funkcyjnej.
     */
    public abstract Set<String> getDeterminantSet();

    /**
     * Atrybuty prawej strony zaleĹźnoĹci funkcyjnej (Y).
     * @return prawa strona zaleĹźnoĹci funkcyjnej.
     */
    public abstract Set<String> getDependentAttributes();
}