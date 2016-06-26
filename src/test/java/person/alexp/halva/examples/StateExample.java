package person.alexp.halva.examples;

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

    //TODO: would be nice to declare "TypeAlias Stack" inside here vs in its own compilcation unit
    //TODO: would be nice to declare "StackAlias" and get "Stack" vs the other way round

    static Tuple2<Integer, StackAlias> pop(final StackAlias stack) {
        final Any<Integer> head = new AnyType<Integer>() {};
        final Any<Stack> tail = new AnyType<Stack>() {};
        return Matcher.match(stack)
                //TODO: use new .caseOf method for this
                //TODO: this fails with: "io.soabase.halva.matcher.MatchError: No matches found and no default provided for: [3, 5, 8, 2, 1]"
                .caseOf(Any.anyHeadAnyTail(head, tail), () -> Tuple.Tu(head.val(), tail.val()))
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
        //TODO shouldn't I be able to assign this to a Stack?
        final ConsList<Integer> in_list = List(5, 8, 2, 1);
        Assert.assertEquals(Tuple.Tu(4, List(8, 2, 1)), stackManip(StackAlias.StackAlias(in_list)));
    }
}
