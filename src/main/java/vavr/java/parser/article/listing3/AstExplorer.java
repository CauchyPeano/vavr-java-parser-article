package vavr.java.parser.article.listing3;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.function.Function;
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

    /**
     * Get all the types in a CompilationUnit which satisfies the given condition
     */
    public List<TypeDeclaration<?>> getTypes(CompilationUnit cu, Function1<TypeDeclaration, Boolean> condition) {
        return List.ofAll(cu.getTypes()).filter(asPredicate(condition));
    }

    /**
     * It returns a function which tells has if a given TypeDeclaration has a method with a given name.
     */
    public Function1<TypeDeclaration, Boolean> hasMethodWithName(String methodName) {
        Function2<TypeDeclaration, String, Boolean> originalFunction = AstExplorer::hasMethodNamed;
        return originalFunction.reversed().curried().apply(methodName);
    }

    /**
     * We could combine previous function to get this one and solve our original question.
     */
    public List<TypeDeclaration<?>> getTypesWithThisMethod(CompilationUnit cu, String methodName) {
        return getTypes(cu, hasMethodWithName(methodName));
    }

    private static <T> Predicate<T> asPredicate(Function1<T, Boolean> function) {
        return v -> function.apply(v);
    }

    public static List<TypeDeclaration<?>> typesThat(
            CompilationUnit cu, Function1<TypeDeclaration,
            Boolean> condition) {
        return List.ofAll(cu.getTypes()).filter(asPredicate(condition));
    }

    /**
     * This function returns true if the TypeDeclaration has at
     * least one method satisfying the given condition.
     */
    public static boolean hasAtLeastOneMethodThat(TypeDeclaration<?> typeDeclaration, Function1<MethodDeclaration, Boolean> condition) {
        Function<? super BodyDeclaration, Boolean> mapFunction = m -> Match(m).of(Case($(instanceOf(MethodDeclaration.class)), match -> condition.apply(match)),
                Case($(), match -> Boolean.FALSE));
        return List.ofAll(typeDeclaration.getMembers())
                .map(mapFunction)
                .reduce((a, b)-> a || b);
    }

    /**
     * We refactor this function to reuse hasAtLeastOneMethodThat
     */
    public static boolean hasMethodWithName(TypeDeclaration typeDeclaration, String methodName) {
        return hasAtLeastOneMethodThat(typeDeclaration, m -> m.getName().equals(methodName));
    }
//
//    public static Function1<TypeDeclaration, Boolean> methodHasName(String methodName) {
//        Function2<TypeDeclaration, String, Boolean> originalFunction = AstExplorer::hasMethodNamed;
//        return originalFunction.reversed().curried().apply(methodName);
//    }

//    public static List<TypeDeclaration<?>> typesWithThisMethod(CompilationUnit cu, String methodName) {
//        return typesThat(cu, methodHasName(methodName));
//    }

}
