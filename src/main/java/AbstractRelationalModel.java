import java.util.Collection;
import java.util.Set;

public abstract class AbstractRelationalModel {
    /**
     * Metoda ustala zbiĂłr atrybutĂłw naleĹźÄcych do relacji. Nazwy atrybutĂłw w sÄ
     * unikalne. MogÄ one skĹadaÄ siÄ z jednego lub wiÄkszej liczby znakĂłw.
     *
     * @param attributes zbiĂłr zawierajÄcy nazwy atrybutĂłw
     */
    public abstract void setAttributes(Set<String> attributes);

    /**
     * Metoda ustala zbiĂłr zaleĹźnoĹci funkcyjnych speĹnianych przez danÄ relacjÄ.
     *
     * @param functionalDependencies zbiĂłr zaleĹźnoĹci funkcyjnych
     */
    public abstract void setFunctionalDependencies(Set<AbstractFunctionalDependency> functionalDependencies);

    /**
     * Metoda generuje zbiĂłr caĹkowicie nietrywialnych zaleĹźnoĹci funkcyjnych,
     * prawdziwych dla relacji zawierajÄcej atrybuty zebrane w zbiorze atributes.
     *
     * @param attributes zbiĂłr atrybutĂłw, bÄdÄcy podzbiorem atrybutĂłw ustawionych za
     *                   pomocÄ setAttributes.
     *
     * @return zbiĂłr zaleĹźnoĹci funkcyjnych wygenerowany na podstawie zbioru
     *         zaleĹźnoĹci ustawionych za pomocÄ setFunctionalDependencies i
     *         obowiÄzujÄcy dla relacji zawierajÄcej atrybuty attributes.
     */
    public abstract Set<AbstractFunctionalDependency> projection(Set<String> attributes);
}