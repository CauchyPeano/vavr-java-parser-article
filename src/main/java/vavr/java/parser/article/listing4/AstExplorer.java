package vavr.java.parser.article.listing4;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.util.function.Function;
import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public class AstExplorer {

    public static boolean hasAtLeastOneMethodThat(TypeDeclaration typeDeclaration,
                                                  Function1<MethodDeclaration, Boolean> condition) {
        return hasAtLeastOneMethodThat(condition).apply(typeDeclaration);
    }

    public static Function1<TypeDeclaration, Boolean> hasAtLeastOneMethodThat(Function1<MethodDeclaration, Boolean> condition) {
        return t -> List.ofAll((NodeList<BodyDeclaration>) t.getMembers())
                .map(m -> Match(m)
                        .of(Case($(instanceOf(MethodDeclaration.class)), match -> condition.apply(match)),
                                Case($(), match -> Boolean.FALSE)))
                .reduce((a, b) -> a || b);
    }

    public static boolean hasMethodNamed(TypeDeclaration typeDeclaration, String methodName) {
        return hasAtLeastOneMethodThat(typeDeclaration, m -> m.getName().equals(methodName));
    }

    private static <T> Predicate<T> asPredicate(Function1<T, Boolean> function) {
        return v -> function.apply(v);
    }

    public static List<TypeDeclaration<?>> typesThat(CompilationUnit cu, Function1<TypeDeclaration, Boolean> condition) {
        return List.ofAll(cu.getTypes())
                .filter(asPredicate(condition));
    }

    public static Function1<TypeDeclaration, Boolean> methodHasName(String methodName) {
        Function2<TypeDeclaration, String, Boolean> originalFunction = AstExplorer::hasMethodNamed;
        return originalFunction.reversed().curried().apply(methodName);
    }

    public static List<TypeDeclaration<?>> typesWithThisMethod(CompilationUnit cu, String methodName) {
        return typesThat(cu, methodHasName(methodName));
    }

}