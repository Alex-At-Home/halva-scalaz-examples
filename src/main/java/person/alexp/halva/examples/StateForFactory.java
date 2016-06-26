package person.alexp.halva.examples;

import fj.data.Java8;
import fj.data.Reader;
import fj.data.State;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import java.util.function.Function;

/** Factory to create for comprehension
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class StateForFactory implements MonadicForWrapper<State> {

    @SuppressWarnings("unchecked")
    public <A> State flatMap(final State m, final Function<A, ? extends State> flat_mapper) {
        return m.flatMap(Java8.Function_F(flat_mapper));
    }
    @SuppressWarnings("unchecked")
    public <A, B> State map(final State m, final Function<A, B> mapper) {
        return m.map(Java8.Function_F(mapper));
    }
}
