package person.alexp.halva.examples;

import fj.data.Reader;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyVal;
import org.junit.Assert;
import org.junit.Test;

/** See http://eed3si9n.com/learning-scalaz/Reader.html
 * Created by Alex on 6/24/2016.
 */
public class ReaderExample {

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
}
