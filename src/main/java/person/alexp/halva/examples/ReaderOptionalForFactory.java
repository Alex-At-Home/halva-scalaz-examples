package person.alexp.halva.examples;

import fj.data.Reader;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import java.util.Optional;
import java.util.function.Function;

/** Factory to create for comprehension for a monadic transformer reader -> optional
 * Created by Alex on 6/24/2016.
 */
@MonadicFor
public class ReaderOptionalForFactory<A>  implements MonadicForWrapper<Reader<A, Optional>> {

    @SuppressWarnings("unchecked")
    public <B> Reader<A, Optional/*C*/> flatMap(final Reader<A, Optional/*B*/> m, final Function<B, ? extends Reader<A, Optional/*C*/>> flat_mapper) {
        // Java type inference needs a _lot_ of help here
        // But the basic idea is very simple - the flatMap "m" takes an optional as input ... if it's present then run the function as before, else return the constant "empty"
        return m.flatMap(maybe_b -> {
            if (maybe_b.isPresent()) { //(do this in horrible imperative fahsion in order to avoid more type inference issues)
                final Reader<A, Optional> ret = flat_mapper.apply((B) maybe_b.get());
                return ret;
            }
            else return Reader.constant(Optional.empty());
        });
    }

    @SuppressWarnings("unchecked")
    public <B, C> Reader<A, Optional/*C*/> map(final Reader<A, Optional/*B*/> m, final Function<B, C> mapper) {
        // Same as the above basically
        return m.map(maybe_b -> {
            if (maybe_b.isPresent()) { //(do this in horrible imperative fahsion in order to avoid more type inference issues)
                final C ret = mapper.apply((B) maybe_b.get());
                return Optional.of(ret);
            }
            else return Optional.empty();
        });
    }
}
