package person.alexp.halva.examples;

import io.soabase.halva.any.AnyVal;
import io.soabase.halva.comprehension.MonadicFor;
import io.soabase.halva.comprehension.MonadicForImpl;

import fj.data.Writer;
import fj.data.Java8;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Alex on 6/23/2016.
 */
public class WriterFor {

    // Things that need to change

    public static class ForWrapper implements MonadicFor.MonadicForWrapper<Writer> {

        public <A> Writer flatMap(final Writer m, final Function<A, Writer> flat_mapper) {
            return m.flatMap(Java8.Function_F(flat_mapper));
        }
        public <A, B> Writer map(final Writer m, final Function<A, B> mapper) {
            return m.map(Java8.Function_F(mapper));
        }
    }
    public static <W, A> WriterFor forComp(final AnyVal<A> any, final Writer<W, A> starting_monad) {
        return new WriterFor(new MonadicForImpl(any, starting_monad, _wrapper));
    }
    public <W, A> WriterFor forComp(final AnyVal<A> any, final Supplier<Writer<W, A>> monad_supplier) {
        _delegate.forComp(any, (Supplier<Writer>)(Supplier<?>)monad_supplier);
        return this;
    }
    public <W, A> Writer<W, A> yield(Supplier<A> yield_supplier) {
        return _delegate.yield(yield_supplier);
    }

    // Things that can stay the same (can be templated/annotated)

    // External

    public <T> WriterFor letComp(final AnyVal<T> any, final Supplier<T> let_supplier) {
        _delegate.letComp(any, let_supplier);
        return this;
    }

    // Internal

    private final MonadicFor<Writer> _delegate;
    private WriterFor(final MonadicFor<Writer> delegate) {
        _delegate = delegate;
    }
    private static final WriterFor.ForWrapper _wrapper = new WriterFor.ForWrapper();
}
