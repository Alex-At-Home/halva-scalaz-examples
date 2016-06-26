package person.alexp.halva.examples;

import fj.data.Java8;
import fj.data.Reader;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import java.util.function.Function;

/** Factory to create for comprehension
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class StateForFactory implements MonadicForWrapper<Reader> {

    @SuppressWarnings("unchecked")
    public <A> Reader flatMap(final Reader m, final Function<A, ? extends Reader> flat_mapper) {
        return m.flatMap(Java8.Function_F(flat_mapper));
    }
    @SuppressWarnings("unchecked")
    public <A, B> Reader map(final Reader m, final Function<A, B> mapper) {
        return m.map(Java8.Function_F(mapper));
    }
}
