package person.alexp.halva.examples;

import fj.P;
import fj.P2;
import fj.Unit;
import fj.data.State;
import io.soabase.halva.alias.TypeAlias;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyType;
import io.soabase.halva.any.AnyVal;
import io.soabase.halva.container.TypeContainer;
import io.soabase.halva.sugar.ConsList;
import org.junit.Assert;
import org.junit.Test;
import io.soabase.halva.matcher.Matcher;
import static io.soabase.halva.sugar.Sugar.List;
import person.alexp.halva.examples.StateExampleTypes.Stack;

/** See http://eed3si9n.com/learning-scalaz/State.html
 * Created by Alex on 6/24/2016.
 */
@TypeContainer(unsuffix = "", suffix = "Types", renameContained = true)
public class StateExample {

    @TypeAlias(unsuffix = "_", suffix = "") interface Stack_ extends ConsList<Integer> {}

    ///////////////////////////////////////////////////////////////////////////////////////

    // 1) Native implementation of stack

    static P2<Integer, Stack> pop(final Stack stack) {
        final Any<Integer> head = new AnyType<Integer>() {};
        final Any<Stack> tail = new AnyType<Stack>() {};
        return Matcher.match(stack)
                //TODO: doesn't seem to be type safe in the return value, eg change one of the types below and you get a runtime error
                .caseOf(Any.anyHeadAnyTail(head, tail), () -> P.p(head.val(), Stack.Stack(tail.val())))
                .get();
    }

    static P2<Unit, Stack> push(final Integer a, final Stack stack) {
        return P.p(Unit.unit(), Stack.Stack(stack.cons(a)));
    }

    static P2<Integer, Stack> stackManip(final Stack stack) {
        //TODO: would be nice to be able to do  the halva equivalent of " val (_, newStack1) = push(3, stack)" (Etc) here
        //(which  I think would just be a nicer simple version of match(rhs).caseOf(any_a, any_b) sort of thing?)
        final P2<Unit, Stack> new_stack_1 = push(3, stack);
        final P2<Integer, Stack> new_stack_2 = pop(new_stack_1._2());
        return pop(new_stack_2._2());
    }

    @Test
    public void test_basicStack() {
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        Assert.assertEquals(P.p(5, List(8, 2, 1)), stackManip(Stack.Stack(in_list)));
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    // 2) State based implementation of stack

    static State<Stack, Integer> pop() {
        return State.unit(s -> pop(s).swap());
    }

    static State<Stack, Unit> push(final Integer a) {
        return State.unit(s -> push(a, s).swap());
    }

    static State<Stack, Integer> stackManip() {
        final AnyVal<Unit> u = Any.make();
        final AnyVal<Integer> a = Any.make();
        final AnyVal<Integer> b = Any.make();
        return StateFor.start()
                .forComp(u, () -> push(3))
                .forComp(a, () -> pop())
                .forComp(b, () -> pop())
                .yield(() -> b.val());
    }

    @Test
    public void test_stateStack() {
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        final P2<Stack, Integer> res = stackManip().run(Stack.Stack(in_list));

        Assert.assertEquals(P.p(List(8, 2, 1), 5), res);
        //(just double check original stack aka state hasn't changed:)
        Assert.assertEquals(List(5, 8, 2, 1), in_list);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    // 3) Using get/put

    static State<Stack, Unit> stackyStack() {
        final AnyVal<Stack> stackNow = Any.make();
        final AnyVal<Unit> r = Any.make();
        return StateFor.start()
                .forComp(stackNow, () -> State.init())
                //(equivalent to:)
                //.forComp(stackNow, () -> State.<Stack, Stack>gets(s -> s))
                .forComp(r, () -> {
                    if (stackNow.val().equals(Stack.Stack(List(1, 2, 3)))) {
                        return State.put(Stack.Stack(List(8, 3, 1)));
                    }
                    else {
                        return State.put(Stack.Stack(List(9, 2, 1)));
                    }
                })
                .yield(() -> r.val());
    }

    @Test
    public void test_stateStackyStack() {
        Assert.assertEquals(Stack.Stack(List(8, 3, 1)), stackyStack().run(Stack.Stack(List(1, 2, 3)))._1());
        Assert.assertEquals(Stack.Stack(List(9, 2, 1)), stackyStack().run(Stack.Stack(List(1, 2, 4)))._1());
    }

    // Now reimplement push and pop using get/put

    static State<Stack, Integer> alternative_pop() {
        final AnyVal<Stack> s_start = Any.make();
        //final AnyVal<Stack> s_end = Any.make(); //(see below - not needed in this construction)
        final AnyVal<Integer> x = Any.make();
        final AnyVal<Unit> u = Any.make();
        return StateFor.start()
                .forComp(s_start, () -> State.init())
                //TODO would have been interesting to have been able to do: (there's a similar comment elsewhere about wanting to support assignment for tuples)
                //.letComp(Any.anyHeadAnyTail(x, s_end), () -> s_start.val())
                // ie from scala:  "val (x :: xs) = s", but this gives a compile error on the type var R.
                .letComp(x, () -> s_start.val().head())
                .forComp(u, () -> State.put(s_start.val().tail()))
                .yield(() -> x.val());
    }

    static State<Stack, Unit> alternative_push(final Integer x) {
        final AnyVal<Stack> xs = Any.make();
        final AnyVal<Unit> r = Any.make();
        return StateFor.start()
                    .forComp(xs, () -> State.init())
                    .forComp(r, () -> State.put(xs.val().cons(x)))
                    .yield(() -> r.val());
    }

    static State<Stack, Integer> alternative_stackManip() {
        final AnyVal<Unit> u = Any.make();
        final AnyVal<Integer> a = Any.make();
        final AnyVal<Integer> b = Any.make();
        return StateFor.start()
                .forComp(u, () -> alternative_push(3))
                .forComp(a, () -> alternative_pop())
                .forComp(b, () -> alternative_pop())
                .yield(() -> b.val());
    }

    @Test
    public void test_stateGetPutStack() {
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        final P2<Stack, Integer> res = alternative_stackManip().run(Stack.Stack(in_list));

        Assert.assertEquals(P.p(List(8, 2, 1), 5), res);
        //(just double check original stack aka state hasn't changed:)
        Assert.assertEquals(List(5, 8, 2, 1), in_list);
    }
}
