package person.alexp.halva.examples;

import fj.P;
import fj.P3;
import fj.data.Reader;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyVal;
import io.soabase.halva.tuple.Tuple;
import org.junit.Assert;
import org.junit.Test;
import static io.soabase.halva.sugar.Sugar.Map;

import java.util.Map;
import java.util.Optional;

/** See
 * 1) http://eed3si9n.com/learning-scalaz/Reader.html
 * 2) http://eed3si9n.com/learning-scalaz/Monad+transformers.html
 * Created by Alex on 6/24/2016.
 */
public class ReaderExample {

    ///////////////////////////////////////////////////////////////////

    // 1) http://eed3si9n.com/learning-scalaz/Reader.html

    public Reader<Integer, Integer> addStuff() {
        final AnyVal<Integer> a = Any.make();
        final AnyVal<Integer> b = Any.make();
        return ReaderFor.start()
                .forComp(a, () -> Reader.unit((Integer x) -> x*2))
                .forComp(b, () -> Reader.unit((Integer x) -> x + 10))
                .yield(() -> a.val() + b.val())
                ;
    }

    @Test
    public void test_basicReader() {
        Assert.assertEquals(19, addStuff().f(3).intValue());
        Assert.assertEquals(19, addStuff().getFunction().f(3).intValue());
    }

    ///////////////////////////////////////////////////////////////////

    // 2) http://eed3si9n.com/learning-scalaz/Monad+transformers.html

    Reader<String, String> myName(final String step) {
        return Reader.unit(a -> step + ", I am " + a);
    }

    Reader<String, P3<String, String, String>> localExample() {
        final AnyVal<String> a = Any.make();
        final AnyVal<String> b = Any.make();
        final AnyVal<String> c = Any.make();
        return ReaderFor.start()
                .forComp(a, () -> myName("First"))
                .forComp(b, () -> myName("Second").andThen(x -> x + "dy"))
                .forComp(c, () -> myName("Third"))
                .yield(() -> P.p(a.val(), b.val(), c.val()));
    }

    @Test
    public void test_stringReader() {
        Assert.assertEquals(P.p("First, I am Fred", "Second, I am Freddy", "Third, I am Fred"), localExample().f("Fred"));
    }

    // fj doesn't support the full scalaz Monad Transformer capability, here's an approximation:

    static Reader<Map<String, String>, Optional<String>> configure(final String key) {
        return Reader.unit(x -> Optional.ofNullable(x.get(key)));
    }

    static Reader<Map<String, String>, Optional<P3<String, String, String>>> setupConnection_manualChain() {
        final AnyVal<Optional<String>> maybe_host = Any.make();
        final AnyVal<Optional<String>> maybe_user = Any.make();
        final AnyVal<Optional<String>> maybe_password = Any.make();
        final AnyVal<String> host = Any.make();
        final AnyVal<String> user = Any.make();
        final AnyVal<String> password = Any.make();
        return ReaderFor.start()
                    .forComp(maybe_host, () -> configure("host"))
                    .forComp(maybe_user, () -> configure("user"))
                    .forComp(maybe_password, () -> configure("password"))
                .yield(() -> OptionalFor.start() // Switch from a P3 of optionals, to an optional P3
                            .forComp(host, () -> maybe_host.val())
                            .forComp(user, () -> maybe_user.val())
                            .forComp(password, () -> maybe_password.val())
                        .yield(() -> P.p(host.val(), user.val(), password.val()))
                    )
                //(could also have done this on a pattern match vs T3(AnySome(host), ...) but this was nicer
                ;
    }

    static Reader<Map<String, String>, Optional<P3<String, String, String>>> setupConnection_monadicTransform() {
        final AnyVal<String> host = Any.make();
        final AnyVal<String> user = Any.make();
        final AnyVal<String> password = Any.make();
        return ReaderOptionalFor.start()
                .forComp(host, () -> configure("host"))
                .forComp(user, () -> configure("user"))
                .forComp(password, () -> configure("password"))
                .yield(() -> P.p(host.val(), user.val(), password.val()));
    }

    @Test
    public void test_complexReader() {
        final Map<String, String> good_config = Map(
                Tuple.Pair("host", "eed3si9n.com"),
                Tuple.Pair("user", "sa"),
                Tuple.Pair("password", "****"));

        Assert.assertEquals(Optional.of(P.p("eed3si9n.com", "sa", "****")), setupConnection_manualChain().f(good_config));
        Assert.assertEquals(Optional.of(P.p("eed3si9n.com", "sa", "****")), setupConnection_monadicTransform().f(good_config));

        final Map<String, String> bad_config = Map(
                Tuple.Pair("host", "example.com"),
                Tuple.Pair("user", "sa"));

        Assert.assertEquals(Optional.empty(), setupConnection_manualChain().f(bad_config));
        Assert.assertEquals(Optional.empty(), setupConnection_monadicTransform().f(bad_config));
    }


}
