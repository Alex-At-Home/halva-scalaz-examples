package person.alexp.halva.examples;

import fj.data.Java8;
import fj.data.Reader;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForWrapper;

import java.util.Optional;
import java.util.function.Function;


/** Factory to create for comprehension for a monadic transformer reader -> optional
 * Created by Alex on 6/24/2016.
 */
//TODO: currently fails with NPE during annotation
//COMMENT THIS IN TO SEE HOW
//@MonadicFor
public class ReaderOptionalForFactory implements MonadicForWrapper<Reader<?, Optional>> {

    @SuppressWarnings("unchecked")
    public <B> Reader<?/*A*/, Optional/*C*/> flatMap(final Reader<?/*A*/, Optional/*B*/> m, final Function<B, ? extends Reader<?/*A*/, Optional/*C*/>> flat_mapper) {
        // Java type inference needs a _lot_ of help here
        // But the basic idea is very simple - the flatMap "m" takes an optional as input ... if it's present then run the function as before, else return the constant "empty"
        /**/
//        return (Reader<?, Optional>)(Reader)flatMapHelper((Reader<Object, Optional>)m, (Function<B, ? extends Reader<Object, Optional<Object>>>)flat_mapper);
        return null;
    }
    /**/
//    @SuppressWarnings("unchecked")
//    private static <B, C> Reader<Object, Optional<C>> flatMapHelper(final Reader<Object, Optional> m, final Function<B, ? extends Reader<Object, Optional<C>>> flat_mapper) {
//        return m.flatMap(maybe_b -> {
//            if (maybe_b.isPresent()) { //(do this in horrible imperative fahsion in order to avoid more type inference issues)
//                final Reader<Object, Optional<C>> ret = flat_mapper.apply((B) maybe_b.get());
//                return ret;
//            }
//            else return Reader.constant(Optional.empty());
//        });
//    }

    @SuppressWarnings("unchecked")
    public <B, C> Reader<?/*A*/, Optional/*C*/> map(final Reader<?/*A*/, Optional/*B*/> m, final Function<B, C> mapper) {
        // Same as the above basically
    /**/
//        return (Reader<?, Optional>)(Reader)mapHelper((Reader<Object, Optional>)m, mapper);
        return null;
    }
    /**/
//    @SuppressWarnings("unchecked")
//    private static <B, C> Reader<Object, Optional<C>> mapHelper(final Reader<Object, Optional> m, final Function<B, C> mapper) {
//        return m.map(maybe_b -> {
//            if (maybe_b.isPresent()) { //(do this in horrible imperative fahsion in order to avoid more type inference issues)
//                final C ret = mapper.apply((B) maybe_b.get());
//                return Optional.of(ret);
//            }
//            else return Optional.empty();
//        });
//    }
}
