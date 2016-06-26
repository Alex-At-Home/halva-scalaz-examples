package person.alexp.halva.examples;

import io.soabase.halva.alias.TypeAlias;
import io.soabase.halva.sugar.ConsList;

/**
 * Created by Alex on 6/26/2016.
 */
//TODO: have to make this ConsList<Integer> (interface with different implementation) not [fj.data.]List<Integer>
@TypeAlias
interface Stack extends ConsList<Integer> {}

