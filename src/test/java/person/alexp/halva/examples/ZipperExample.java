package person.alexp.halva.examples;

import fj.data.*;
import io.soabase.halva.any.Any;
import io.soabase.halva.any.AnyVal;
import org.junit.Assert;
import org.junit.Test;


/** 1) http://eed3si9n.com/learning-scalaz/Tree.html
 *  2) http://eed3si9n.com/learning-scalaz/Zipper.html
 * Created by Alex on 6/29/2016.
 */
public class ZipperExample {

    ////////////////////////////////////////////////////////////////////

    // 1) http://eed3si9n.com/learning-scalaz/Tree.html

    final Tree<Character> freeTree =
            Tree.node('P', List.list(
                    Tree.node('O', List.list(
                            Tree.node('O', List.list(Tree.leaf('N'), Tree.leaf('T')))
                            ,
                            Tree.node('Y', List.list(Tree.leaf('S'), Tree.leaf('A')))
                    ))
                    ,
                    Tree.node('L', List.list(
                            Tree.node('W', List.list(Tree.leaf('C'), Tree.leaf('R')))
                            ,
                            Tree.node('A', List.list(Tree.leaf('A'), Tree.leaf('C')))
                    ))
                    )
                    );
    final String expected_old = "Tree(P,Tree(O,Tree(O,Tree(N),Tree(T)),Tree(Y,Tree(S),Tree(A))),Tree(L,Tree(W,Tree(C),Tree(R)),Tree(A,Tree(A),Tree(C))))";

    @Test
    public void test_tree() {
        Assert.assertEquals(expected_old, freeTree.toString());
    }

    <T> Option<TreeZipper<T>> moveInTree(final Tree<T> start, final int step_1, final int step_2) {
        final AnyVal<TreeZipper<T>> root = Any.make();
        final AnyVal<TreeZipper<T>> step_1_result = Any.make();
        final AnyVal<TreeZipper<T>> step_2_result = Any.make();
        return OptionFor.start()
                .forComp(root, () -> Option.some(TreeZipper.fromTree(start)))
                .forComp(step_1_result, () -> root.val().getChild(step_1))
                .forComp(step_2_result, () -> step_1_result.val().getChild(step_2))
                .yield(() -> step_2_result.val())
                ;
    }

    @Test
    public void test_treeZipper() {
        final String expected_new = "Tree(P,Tree(O,Tree(O,Tree(N),Tree(T)),Tree(Y,Tree(S),Tree(A))),Tree(L,Tree(P,Tree(C),Tree(R)),Tree(A,Tree(A),Tree(C))))";
        final Option<TreeZipper<Character>> tree_after_change = moveInTree(freeTree, 2, 1).bind(z -> Option.some(z.modifyLabel(c -> 'P')));
        Assert.assertEquals(expected_old, freeTree.toString());
        Assert.assertTrue(tree_after_change.isSome());
        Assert.assertEquals(expected_new, tree_after_change.some().toTree().toString());
        final Option<TreeZipper<Character>> tree_after_impossible_change = moveInTree(freeTree, 3, 1).bind(z -> Option.some(z.modifyLabel(c -> 'P')));
        Assert.assertTrue(tree_after_impossible_change.isNone());
    }

    ////////////////////////////////////////////////////////////////////

    // 2) http://eed3si9n.com/learning-scalaz/Zipper.html

    <T> Option<Zipper<T>> modifyStream(final Stream<T> stream, final T change_to) {
        final AnyVal<Zipper<T>> root = Any.make();
        final AnyVal<Zipper<T>> step_1_result = Any.make();
        final AnyVal<Zipper<T>> step_2_result = Any.make();
        return OptionFor.start()
                .forComp(root, () -> Zipper.fromStream(stream))
                .forComp(step_1_result, () -> root.val().next())
                .forComp(step_2_result, () -> step_1_result.val().next())
                .yield(() -> step_2_result.val().replace(change_to));
    }

    @Test
    public void test_zipper() {
        final Stream<Integer> in = Stream.stream(1, 2, 3, 4);
        final Option<Zipper<Integer>> mod_stream = modifyStream(in, 7);
        Assert.assertEquals(Stream.stream(1, 2, 3, 4), in);
        Assert.assertTrue(mod_stream.isSome());
        Assert.assertEquals(Stream.stream(1, 2, 7, 4), mod_stream.some().toStream());
    }
}
