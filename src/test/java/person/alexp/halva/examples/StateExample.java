package person.alexp.halva.examples;

import fj.P2;
import fj.Unit;
import fj.data.State;
import io.soabase.halva.alias.TypeAlias;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyList;
import io.soabase.halva.any.AnyType;
import io.soabase.halva.any.AnyVal;
import io.soabase.halva.sugar.ConsList;
import io.soabase.halva.tuple.Tuple;
import io.soabase.halva.tuple.details.Tuple2;
import org.junit.Assert;
import org.junit.Test;
import io.soabase.halva.matcher.Matcher;
import static io.soabase.halva.sugar.Sugar.List;

/** See http://eed3si9n.com/learning-scalaz/State.html
 * Created by Alex on 6/24/2016.
 */
public class StateExample {

    ///////////////////////////////////////////////////////////////////////////////////////

    // 1) Native implementation of stack

    //TODO: would be nice to declare "TypeAlias Stack" inside here vs in its own compilcation unit
    //TODO: would be nice to declare "StackAlias" and get "Stack" vs the other way round

    static Tuple2<Integer, StackAlias> pop(final StackAlias stack) {
        final Any<Integer> head = new AnyType<Integer>() {};
        //TODO: want to use StackAlias inside the Any here (see also the caseOf return val)
        // but if you do, fails with: "io.soabase.halva.matcher.MatchError: No matches found and no default provided for: [3, 5, 8, 2, 1]"
        final Any<ConsList<Integer>> tail = new AnyType<ConsList<Integer>>() {};
        return Matcher.match(stack)
                //TODO: use new .caseOf method for this?
                //TODO: doesn't seem to be type safe in the return value, eg change one of the types below and you get a runtime error
                .caseOf(Any.anyHeadAnyTail(head, tail), () -> Tuple.Tu(head.val(), StackAlias.StackAlias(tail.val())))
                .get();
    }

    static Tuple2<Unit, StackAlias> push(final Integer a, final StackAlias stack) {
        return Tuple.Tu(Unit.unit(), StackAlias.StackAlias(stack.cons(a)));
    }

    static Tuple2<Integer, StackAlias> stackManip(final StackAlias stack) {
        //TODO: would be nice to be able to do  the halva equivalent of " val (_, newStack1) = push(3, stack)" (Etc) here
        //(which  I think would just be a nicer simple version of match(rhs).caseOf(any_a, any_b) sort of thing?)
        final Tuple2<Unit, StackAlias> new_stack_1 = push(3, stack);
        final Tuple2<Integer, StackAlias> new_stack_2 = pop(new_stack_1._2);
        return pop(new_stack_2._2);
    }

    @Test
    public void test_basicStack() {
        //TODO shouldn't I be able to assign this to a StackAlias?
        // eg final StackAlias stack = List(5, 8, 2, 1); //gives compile error
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        Assert.assertEquals(Tuple.Tu(5, List(8, 2, 1)), stackManip(StackAlias.StackAlias(in_list)));
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    // 2) State based implementation of stack

    static State<StackAlias, Integer> pop() {
        return State.unit(s -> {
            final Tuple2<Integer, StackAlias> s_i = pop(s);
            //TODO: need a nicer PX<->TX utility...
            return new P2<StackAlias, Integer>() {
                @Override
                public StackAlias _1() { return s_i._2; }
                @Override
                public Integer _2() { return s_i._1; }
            };
        });
    }

    static State<StackAlias, Unit> push(final Integer a) {
        return State.unit(s -> {
            final Tuple2<Unit, StackAlias> s_i = push(a, s);
            //TODO: need a nicer PX<->TX utility...
            return new P2<StackAlias, Unit>() {
                @Override
                public StackAlias _1() { return s_i._2; }
                @Override
                public Unit _2() { return s_i._1; }
            };
        });
    }

    static State<StackAlias, Integer> stackManip() {
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
        //TODO shouldn't I be able to assign this to a StackAlias?
        // eg final StackAlias stack = List(5, 8, 2, 1); //gives compile error
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        final P2<StackAlias, Integer> res = stackManip().run(StackAlias.StackAlias(in_list));
        //TODO: need a nicer PX<->TX utility...
        Assert.assertEquals(Tuple.Tu(List(8, 2, 1), 5), Tuple.Tu(res._1(), res._2()));
        //(just double check original stack aka state hasn't changed:)
        Assert.assertEquals(List(5, 8, 2, 1), in_list);
    }
}
