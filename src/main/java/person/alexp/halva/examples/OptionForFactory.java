package person.alexp.halva.examples;

import fj.data.Java8;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import fj.data.Option;
import java.util.function.Function;

/** Factory to create for comprehension
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class OptionForFactory implements MonadicForWrapper<Option> {

    @SuppressWarnings("unchecked")
    public <A> Option flatMap(final Option m, final Function<A, ? extends Option> flat_mapper) {
        return m.bind(Java8.Function_F(flat_mapper));
    }
    @SuppressWarnings("unchecked")
    public <A, B> Option map(final Option m, final Function<A, B> mapper) {
        return m.map(Java8.Function_F(mapper));
    }
}
