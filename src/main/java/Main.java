import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        System.out.println("Przyklad pierwszy");
        RelationalModel model = new RelationalModel();

        Set<String> atributtes = new HashSet<>(Arrays.asList(
                "A", "B", "C", "D", "E", "F"
        ));

        Set<AbstractFunctionalDependency> initialDependencies = new HashSet<>(Arrays.asList(
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("A")), new HashSet<>(Arrays.asList("B"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("A", "C")), new HashSet<>(Arrays.asList("D"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("B")), new HashSet<>(Arrays.asList("E"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("D")), new HashSet<>(Arrays.asList("A"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("E")), new HashSet<>(Arrays.asList("F"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("F")), new HashSet<>(Arrays.asList("D")))
        ));

        model.setAttributes(atributtes);
        model.setFunctionalDependencies(initialDependencies);

        Set<AbstractFunctionalDependency> subDependencies = model.projection(new HashSet<>(Arrays.asList("A", "C", "F")));

        subDependencies.forEach(d -> {
            System.out.println(d.getDeterminantSet().toString() + " -> " + d.getDependentAttributes().toString());
        });

        //// ------------------------------------------------
        System.out.println("\n\nPrzyklad drugi");
        model = new RelationalModel();

        atributtes = new HashSet<>(Arrays.asList(
                "A", "B", "C", "D", "E", "F"
        ));

        initialDependencies = new HashSet<>(Arrays.asList(
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("A")), new HashSet<>(Arrays.asList("B"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("B")), new HashSet<>(Arrays.asList("D"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("C")), new HashSet<>(Arrays.asList("E"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("D", "E")), new HashSet<>(Arrays.asList("F"))),
                new RelationalModel.MyFunctionalDependency(new HashSet<>(Arrays.asList("F")), new HashSet<>(Arrays.asList("C")))
        ));

        model.setAttributes(atributtes);
        model.setFunctionalDependencies(initialDependencies);

        subDependencies = model.projection(new HashSet<>(Arrays.asList("A", "C", "D", "F")));

        subDependencies.forEach(d -> {
            System.out.println(d.getDeterminantSet().toString() + " -> " + d.getDependentAttributes().toString());
        });
    }

}
