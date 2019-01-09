package vavr.java.parser.article.listing3;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.Function1;
import io.vavr.collection.List;

import java.util.function.Predicate;

public class AstExplorer {

//    public static boolean hasAtLeastOneMethodThat(
//            TypeDeclaration typeDeclaration,
//            Function1<MethodDeclaration, Boolean> condition) {
//        return hasAtLeastOneMethodThat(condition).apply(typeDeclaration);
//    }

//    public static Function1<TypeDeclaration<?>, Boolean> hasAtLeastOneMethodThat(
//            Function1<MethodDeclaration, Boolean> condition) {
//        return t -> List.ofAll(t.getMembers())
//                .map(v -> Match(v).of(Case($(instanceOf(MethodDeclaration.class)), f -> Option.of(t.getName().asString())),
//                        Case($(), f -> Option.<String>none())))
//                .reduce((a, b) -> a || b);
//    }

//    public static boolean hasMethodNamed(TypeDeclaration typeDeclaration, String methodName) {
//        return hasAtLeastOneMethodThat(typeDeclaration, m -> m.getName().equals(methodName));
//    }

    private static <T> Predicate<T> asPredicate(Function1<T, Boolean> function) {
        return v -> function.apply(v);
    }

    public static List<TypeDeclaration<?>> typesThat(
            CompilationUnit cu, Function1<TypeDeclaration,
            Boolean> condition) {
        return List.ofAll(cu.getTypes()).filter(asPredicate(condition));
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
