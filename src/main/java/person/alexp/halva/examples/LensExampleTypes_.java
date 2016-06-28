package person.alexp.halva.examples;

import io.soabase.halva.caseclass.CaseClass;
import io.soabase.halva.container.TypeContainer;

/**
 * Created by Alex on 6/28/2016.
 */
@TypeContainer(unsuffix="_" , renameContained = true)
public interface LensExampleTypes_ {
    @CaseClass(unsuffix = "_") interface Point_ { double x(); double y(); }
    @CaseClass(unsuffix = "_") interface Color_ { byte r(); byte g(); byte b(); }
    @CaseClass(unsuffix = "_") interface Turtle_ { Point_ position(); double heading(); Color_ color(); }
}
