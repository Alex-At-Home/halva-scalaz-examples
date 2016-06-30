// Auto generated from person.alexp.halva.examples.ReaderOptionalForFactory by Soabase io.soabase.halva.comprehension.MonadicFor annotation processor
package person.alexp.halva.examples;

import fj.data.Reader;
import io.soabase.halva.any.AnyVal;
import io.soabase.halva.comprehension.MonadicForImpl;

import javax.annotation.Generated;
import java.util.Optional;
import java.util.function.Supplier;

/**
* TODO: this is the working generated code, just replace MFB in hte suppliers with Optional<MF_B>
 * @param <A>
 */
public class ReaderOptionalFor2<A> {
    private final MonadicForImpl<Reader> delegate;

    private ReaderOptionalFor2(MonadicForImpl<Reader> delegate) {
        this.delegate = delegate;
    }

    public static <A> ReaderOptionalFor2<A> start() {
        return new ReaderOptionalFor2<A>(new MonadicForImpl<Reader>(new ReaderOptionalForFactory()));
    }

    public <MF_A, MF_B> ReaderOptionalFor2<A> forComp(AnyVal<MF_B> any, Supplier<? extends Reader<MF_A, Optional<MF_B>>> supplier) {
        delegate.forComp(any, supplier);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <MF_A, MF_B> Reader<MF_A, Optional<MF_B>> yield(Supplier<MF_B> supplier) {
        return delegate.yield(supplier);
    }

    public <R> ReaderOptionalFor2<A> letComp(AnyVal<R> any, Supplier<R> supplier) {
        delegate.letComp(any, supplier);
        return this;
    }
}
