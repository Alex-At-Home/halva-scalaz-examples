package person.alexp.halva.examples;

import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;
import java.util.function.Function;

import fj.data.Writer;
import fj.data.Java8;

/** Factory to create for comprehension
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class WriterForFactory implements MonadicForWrapper<Writer> {

    @SuppressWarnings("unchecked")
    public <A> Writer flatMap(final Writer m, final Function<A, ? extends Writer> flat_mapper) {
        return m.flatMap(Java8.Function_F(flat_mapper));
    }
    @SuppressWarnings("unchecked")
    public <A, B> Writer map(final Writer m, final Function<A, B> mapper) {
        return m.map(Java8.Function_F(mapper));
    }
}
