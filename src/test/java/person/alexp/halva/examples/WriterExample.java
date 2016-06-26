package person.alexp.halva.examples;

import fj.Monoid;
import fj.P2;
import fj.Unit;
import fj.data.Writer;
import fj.data.List;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyVal;
import org.junit.Assert;
import org.junit.Test;

/** See http://eed3si9n.com/learning-scalaz/Writer.html
 * Created by Alex on 6/23/2016.
 */
public class WriterExample {

    private Writer<List<String>, Integer> logNumber(final Integer x) {
        return Writer.unit(x, Monoid.<String>listMonoid()).tell(List.list("Got number: " + x));
    }

    @Test
    public void test_basicWriter() {
        {
            final AnyVal<Integer> a = Any.make();
            final AnyVal<Integer> b = Any.make();
            final Writer<List<String>, Integer> multWithLog =
                    WriterFor.start()
                            .forComp(a, () -> logNumber(3))
                            .forComp(b, () -> logNumber(5))
                    .yield(() -> a.val()*b.val())
                    ;

            Assert.assertEquals(15, multWithLog.value().intValue());
            Assert.assertEquals(List.list("Got number: 3", "Got number: 5"), multWithLog.log());
        }
    }

    private Writer<List<String>, Integer> gcd(final Integer a, final Integer b) {
        final AnyVal<Unit> _U = Any.make();
        if (b == 0)
            return WriterFor.start()
                .forComp(_U, () -> Writer.unit(Unit.unit(), Monoid.<String>listMonoid()).tell(List.list("Finished with " + a)))
                .yield(() -> a)
                    ;
        else
            return gcd(b, a % b).tell(List.list("" + a + " mod " + b + " " + (a % b)));
    }

    @Test
    public void test_gcd() {

        final P2<List<String>, Integer> result = gcd(8, 3).run();
        Assert.assertEquals(List.list("Finished with 1","2 mod 1 0","3 mod 2 1","8 mod 3 2"), result._1());
        Assert.assertEquals(1, result._2().intValue());
    }

}
