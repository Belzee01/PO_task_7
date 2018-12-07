import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RelationalModel extends AbstractRelationalModel {

    private List<String> attributes;
    private List<AbstractFunctionalDependency> keyValues;

    public RelationalModel() {
        this.attributes = new ArrayList<>();
        this.keyValues = new ArrayList<>();
    }

    public String[] calcClosure(String[] args) {
        List<String> current = new ArrayList<>(Arrays.asList(args));
        List<AbstractFunctionalDependency> keyValuesCopy = new ArrayList<>(this.keyValues);

        for (int i = 0; i < current.size(); i++) {
            boolean changed = false;
            for (int j = 0; j < keyValuesCopy.size(); j++) {
                AbstractFunctionalDependency currentPair = keyValuesCopy.get(j);

                List<String> relationKeyAsList = new ArrayList<>(currentPair.getDeterminantSet());
                List<String> relationValueAsList = new ArrayList<>(currentPair.getDependentAttributes());

                if (current.containsAll(relationKeyAsList)) {
                    if (!currentPair.getDeterminantSet().equals(currentPair.getDependentAttributes()) && !current.containsAll(relationValueAsList)) {
                        AtomicInteger index = new AtomicInteger(current.indexOf(relationKeyAsList.get(relationKeyAsList.size() - 1)));
                        index.getAndIncrement();
                        relationValueAsList.stream().filter(rv -> !current.contains(rv)).forEach(rv -> current.add(index.getAndIncrement(), rv));
                    }
                    changed = true;
                    keyValuesCopy.remove(currentPair);
                }
            }
            if (!changed)
                break;
        }

        return current.toArray(new String[0]);
    }

    public boolean isKey(String[] args, boolean minimal) {

        boolean isKey = false;
        List<String> closure = Arrays.asList(this.calcClosure(args));
        ;

        Collections.sort(this.attributes);
        Collections.sort(closure);

        if (this.attributes.equals(closure))
            isKey = true;

        if (isKey && minimal) {
            List<String> currentSet = new ArrayList<>(Arrays.asList(args));
            for (AbstractFunctionalDependency currentPair : this.keyValues) {
                List<String> keyValueCombined = new ArrayList<>(currentPair.getDeterminantSet());
                List<String> relationValueAsList = new ArrayList<>(currentPair.getDependentAttributes());

                keyValueCombined.addAll(relationValueAsList);
                List<String> keyValueCombinedAndReduced = keyValueCombined.stream().distinct().collect(Collectors.toList());

                if (keyValueCombinedAndReduced.size() > 1 && currentSet.containsAll(keyValueCombinedAndReduced))
                    return false;
            }
        }

        return isKey;
    }

    @Override
    public void setAttributes(Set<String> atributes) {
        this.attributes = new ArrayList<>(atributes);
    }

    @Override
    public void setFunctionalDependencies(Set<AbstractFunctionalDependency> functionalDependencies) {
        this.keyValues.addAll(functionalDependencies);
    }

    @Override
    public Set<AbstractFunctionalDependency> projection(Set<String> attributes) {
        List<String> args = new ArrayList<>(attributes);
        Set<AbstractFunctionalDependency> result = new HashSet<>();
        for (int i = 1; i <= args.size(); i++) {
            List<List<String>> combinations = combination(args, i);

            combinations.forEach(c -> {
                ArrayList<String> closure = new ArrayList<>(Arrays.asList(calcClosure(c.toArray(new String[0]))));
                closure.removeAll(c);
                Set<String> dependants = args
                        .stream()
                        .filter(closure::contains)
//                        .filter(d -> !d.equals(c))
                        .collect(Collectors.toCollection(HashSet::new));

                if (!new HashSet<>(c).equals(dependants) && !dependants.isEmpty())
                    result.add(new MyFunctionalDependency(new HashSet<>(c), dependants));
            });
        }
        return result;
    }

    public Collection<Set<String>> getMinimalKeys() {
        return checkMinimalKeyForEachCombination(this.attributes);
    }

    public boolean isBase(Set<AbstractFunctionalDependency> base) {
        RelationalModel model = new RelationalModel();
        model.setFunctionalDependencies(base);
        try {
            this.keyValues.forEach(kv -> {
                String[] closureWithRespectToFD = model.calcClosure(kv.getDeterminantSet().toArray(new String[0]));

                if (!Arrays.asList(closureWithRespectToFD).containsAll(kv.getDependentAttributes()))
                    throw new RuntimeException("Given set is not a base for initial set of functional dependencies.\n" +
                            "Failed on : " + kv.getDeterminantSet() + " -> " + kv.getDependentAttributes() + "\n" +
                            "Closure for : " + kv.getDeterminantSet() + " => " + Arrays.deepToString(closureWithRespectToFD));
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static <T> List<T> subSet(ArrayList<T> set, List<T> toBeRemoved) {
        List<T> subSet;
        set.removeAll(toBeRemoved);
        subSet = new ArrayList<>(set);
        return subSet;
    }

    private List<Set<String>> checkMinimalKeyForEachCombination(List<String> args) {
        List<Set<String>> result = new ArrayList<>();
        for (int i = 1; i <= args.size(); i++) {
            List<List<String>> possibleMinimalKeys = combination(args, i); // check minimal keys for first iteration
            List<List<String>> minimalKeys = possibleMinimalKeys.stream()
                    .filter(pmk ->
                            (isKey(pmk.toArray(new String[0]), true))
                    ).collect(Collectors.toList());
            if (!minimalKeys.isEmpty()) {
                minimalKeys.forEach(mk -> result.add(new HashSet<>(mk)));
                break;
            }
        }
        return result;
    }

    public List<List<String>> combination(List<String> values, int size) {

        if (size == 0)
            return Collections.singletonList(Collections.emptyList());

        if (values.isEmpty())
            return Collections.emptyList();

        List<List<String>> currentCombination = new ArrayList<>();
        final String first = values.get(0);

        List<String> subSet = new ArrayList<>(values);
        subSet.remove(first);

        List<List<String>> subSetCombination = combination(subSet, size - 1);

        subSetCombination.forEach(ssc -> {
            List<String> newSet = new ArrayList<>(ssc);
            newSet.add(0, first);
            currentCombination.add(newSet);
        });
        currentCombination.addAll(combination(subSet, size));

        return currentCombination;
    }

    public static class MyFunctionalDependency extends AbstractFunctionalDependency {

        private Set<String> dependent;
        private Set<String> determinant;

        public MyFunctionalDependency(Set<String> determinant, Set<String> dependent) {
            this.dependent = dependent;
            this.determinant = determinant;
        }

        public void setDependent(Set<String> dependent) {
            this.dependent = dependent;
        }

        public void setDeterminant(Set<String> determinant) {
            this.determinant = determinant;
        }

        @Override
        public Set<String> getDependentAttributes() {
            return dependent;
        }

        @Override
        public Set<String> getDeterminantSet() {
            return determinant;
        }
    }
}