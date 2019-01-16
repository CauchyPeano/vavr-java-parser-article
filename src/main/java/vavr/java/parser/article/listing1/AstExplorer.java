package vavr.java.parser.article.listing1;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.function.Function;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class AstExplorer {

    public static boolean hasMethodNamed(TypeDeclaration typeDeclaration, String methodName) {
        return List.ofAll((NodeList<BodyDeclaration>) typeDeclaration.getMembers())
                .map(member -> Match(member).of(
                        Case($(instanceOf(MethodDeclaration.class)), t -> Option.of(t.getName().asString())),
                        Case($(), t -> Option.<String>none())
                ))
                .map(n -> n.isDefined() && n.get().equals(methodName))
                .reduce((a, b) -> a || b);
    }

    public static List<TypeDeclaration<?>> getTypesWithThisMethod(CompilationUnit cu, String methodName) {
        return List.ofAll(cu.getTypes())
                .filter(t -> hasMethodNamed(t, methodName));
    }


}
