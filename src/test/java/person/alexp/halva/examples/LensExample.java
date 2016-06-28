package person.alexp.halva.examples;

import fj.P;
import fj.P2;
import fj.data.Reader;
import fj.data.State;
import fj.data.optic.Lens;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyVal;
import io.soabase.halva.tuple.Tuple;
import org.junit.Assert;
import org.junit.Test;
import person.alexp.halva.examples.LensExampleTypes.Turtle;
import static person.alexp.halva.examples.LensExampleTypes.Turtle.Turtle;
import person.alexp.halva.examples.LensExampleTypes.Point;
import person.alexp.halva.examples.LensExampleTypes_.Point_;
import static person.alexp.halva.examples.LensExampleTypes.Point.Point;
import person.alexp.halva.examples.LensExampleTypes.Color;
import static person.alexp.halva.examples.LensExampleTypes.Color.Color;
import person.alexp.halva.examples.LensExampleTypes_.Color_;

import java.util.function.Function;

/** From: http://eed3si9n.com/learning-scalaz/Lens.html
 * Created by Alex on 6/28/2016.
 */
public class LensExample {

    @Test
    public void test_turtle() {
        final Turtle turtle = Turtle(Point(2.0, 3.0), 0.0, Color((byte) -1, (byte) -1, (byte) -1));
        Assert.assertEquals(2.0, turtle.position().x(), 1e-6);
        Assert.assertEquals(0.0, turtle.heading(), 1e-6);
        final Turtle turtle2 = turtle.copy().heading(1.0).build();
        Assert.assertEquals(0.0, turtle.heading(), 1e-6);
        Assert.assertEquals(1.0, turtle2.heading(), 1e-6);
    }

    Lens<Turtle, Point> turtlePosition() {
        //TODO: nasty cast needed here to resolve Point vs Point_
        return Lens.lens(t -> (Point) t.position(), p -> t -> t.copy().position(p).build());
    }
    Lens<Turtle, Double> turtleHeading() {
        return Lens.lens(t -> t.heading(), h -> t -> t.copy().heading(h).build());
    }

    Lens<Point, Double> turtlePointX() {
        return Lens.lens(p -> p.x(), d -> p -> p.copy().x(d).build());
    }
    Lens<Point, Double> turtlePointY() {
        return Lens.lens(p -> p.y(), d -> p -> p.copy().y(d).build());
    }

    Lens<Turtle, Double> turtleX() {
        return turtlePosition().composeLens(turtlePointX());
    }
    Lens<Turtle, Double> turtleY() {
        return turtlePosition().composeLens(turtlePointY());
    }

    @Test
    public void test_turtleLens() {
        final Turtle turtle = Turtle(Point(2.0, 3.0), 0.0, Color((byte) -1, (byte) -1, (byte) -1));
        Assert.assertEquals(2.0, turtleX().get(turtle), 1e-6);

        final Turtle turtle2 = turtleX().set(0.5).f(turtle);
        Assert.assertEquals(2.0, turtleX().get(turtle), 1e-6);
        Assert.assertEquals(0.5, turtleX().get(turtle2), 1e-6);

        final Turtle turtle3 = turtleX().modify(d -> d + 1.0).f(turtle);
        Assert.assertEquals(2.0, turtleX().get(turtle), 1e-6);
        Assert.assertEquals(3.0, turtleX().get(turtle3), 1e-6);
    }

    //TODO: Needs some thought as to whether this construct can be encoded into a LensFor to hide away  the details
    //TODO: not sure why this isn't built into Lens?
    <T, X> State<T, X> modifyState(final Lens<T, X> lens, final fj.F<X, X> f) {
        return State.unit((T t) -> {
            final T new_t = lens.modify(f).f(t);
            return P.p(new_t, lens.get(new_t));
        });
    }
    <T, X> State<T, X> getTurtleState(final Lens<T, X> lens) {
        return State.unit((T t) -> P.p(t, lens.get(t)));
    }

    /** The pay off after all that is that you can now monadically combine gets and sets to move the turtle around
     * @param dist
     * @return
     */
    State<Turtle, P2<Double, Double>> forward(final double dist) {
        final AnyVal<Double> heading = Any.make();
        final AnyVal<Double> new_x = Any.make();
        final AnyVal<Double> new_y = Any.make();
        return StateFor.start()
                .forComp(heading, () -> getTurtleState(turtleHeading()))
                .forComp(new_x, () -> modifyState(turtleX(), x -> x + dist*Math.cos(heading.val())))
                .forComp(new_y, () -> modifyState(turtleY(), y -> y + dist*Math.sin(heading.val())))
                .yield(() -> P.p(new_x.val(), new_y.val()));
    }

    @Test
    public void test_monadicTurtle() {
        final Turtle turtle = Turtle(Point(2.0, 3.0), 0.0, Color((byte) -1, (byte) -1, (byte) -1));
        final P2<Turtle, P2<Double, Double>> res = forward(10).run(turtle);
        Assert.assertEquals(P.p(12.0, 3.0), res._2());
        Assert.assertEquals(Turtle(Point(2.0, 3.0), 0.0, Color((byte) -1, (byte) -1, (byte) -1)), turtle);
        Assert.assertEquals(Turtle(Point(12.0, 3.0), 0.0, Color((byte) -1, (byte) -1, (byte) -1)), res._1());
    }
}
