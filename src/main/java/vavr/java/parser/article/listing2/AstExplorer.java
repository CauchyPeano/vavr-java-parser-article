package vavr.java.parser.article.listing2;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class AstExplorer {

    public static boolean hasMethodNamed(TypeDeclaration<?> typeDeclaration, String methodName) {
        List<BodyDeclaration<?>> bodyDeclarations = List.ofAll(typeDeclaration.getMembers());
        List<Option<String>> map = bodyDeclarations
                .map(AstExplorer::getBodyDeclarationOptionFunction);
        return map
                .map(n -> n.isDefined() && n.get().equals(methodName))
                .reduce((a, b) -> a || b);
    }

    private static Option<String> getBodyDeclarationOptionFunction(BodyDeclaration member) {
        return Match(member).of(
                Case($(instanceOf(MethodDeclaration.class)), t -> Option.of(t.getName().asString())),
                Case($(), t -> Option.<String>none())
        );
    }

    public static List<TypeDeclaration<?>> getTypesWithThisMethod(CompilationUnit cu, String methodName) {
        return List.ofAll(cu.getTypes())
                .filter(t -> hasMethodNamed(t, methodName));
    }

    public List<TypeDeclaration<?>> getTypesWithThisMethod2(CompilationUnit cu, String methodName) {
        Function2<TypeDeclaration, String, Boolean> originalFunction = AstExplorer::hasMethodNamed;
        Function2<String, TypeDeclaration, Boolean> originalFunctionReversed =
                originalFunction.reversed();
        Function1<String, Function1<TypeDeclaration, Boolean>> originalFunctionReversedAndCurried =
                originalFunction.reversed().curried();

        Function1<TypeDeclaration, Boolean> originalFunctionReversedAndCurriedAndAppliedToMethodName =
                originalFunction.reversed().curried().apply(methodName);

        return List.ofAll(cu.getTypes())
                .filter(originalFunctionReversedAndCurriedAndAppliedToMethodName::apply);
    }

    private static <T> Predicate<T> asPredicate(Function1<T, Boolean> function) {
        return function::apply;
    }
}
