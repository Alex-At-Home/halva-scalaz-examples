package person.alexp.halva.examples;

import fj.data.Java8;
import java.util.Optional;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import java.util.function.Function;

/** Factory to create for comprehension
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class OptionalForFactory implements MonadicForWrapper<Optional> {

    @SuppressWarnings("unchecked")
    public <A> Optional flatMap(final Optional m, final Function<A, ? extends Optional> flat_mapper) {
        return m.flatMap(flat_mapper);
    }
    @SuppressWarnings("unchecked")
    public <A, B> Optional map(final Optional m, final Function<A, B> mapper) {
        return m.map(mapper);
    }
}
