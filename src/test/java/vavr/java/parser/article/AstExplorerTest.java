package vavr.java.parser.article;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.vavr.Function1;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;


import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static vavr.java.parser.article.listing4.AstExplorer.*;
 
public class AstExplorerTest {
 
    @Test
    public void typesNamedA() throws ParseException {
        InputStream is = AstExplorerTest.class.getResourceAsStream("/SomeJavaFile.java");
        CompilationUnit cu = JavaParser.parse(is);
        Function1<MethodDeclaration, Boolean> isNamedBar = m -> m.getName().asString().equals("bar");
        List<TypeDeclaration<?>> res = typesThat(cu, hasAtLeastOneMethodThat(isNamedBar));
        assertEquals(2, res.length());
        assertEquals("A", res.get(0).getName().asString());
        assertEquals("B", res.get(1).getName().asString());
    }
 
    @Test
    public void typesHavingAMethodNamedBar() throws ParseException {
        InputStream is = AstExplorerTest.class.getResourceAsStream("/SomeJavaFile.java");
        CompilationUnit cu = JavaParser.parse(is);
        Function1<MethodDeclaration, Boolean> isNamedBar = m -> m.getName().asString().equals("bar");
        List<TypeDeclaration<?>> res = typesThat(cu, hasAtLeastOneMethodThat(isNamedBar));
        assertEquals(2, res.length());
        assertEquals("A", res.get(0).getName().asString());
        assertEquals("B", res.get(1).getName().asString());
    }
 
    @Test
    public void typesHavingAMethodNamedBarWhichTakesZeroParams() throws ParseException {
        InputStream is = AstExplorerTest.class.getResourceAsStream("/SomeJavaFile.java");
        CompilationUnit cu = JavaParser.parse(is);
        Function1<MethodDeclaration, Boolean> hasZeroParam = m -> m.getParameters().size() == 0;
        Function1<MethodDeclaration, Boolean> isNamedBar = m -> m.getName().asString().equals("bar");
        List<TypeDeclaration<?>> res = typesThat(cu, hasAtLeastOneMethodThat(m ->
                hasZeroParam.apply(m) && isNamedBar.apply(m)));
        assertEquals(1, res.length());
        assertEquals("A", res.get(0).getName().asString());
    }
 
    @Test
    public void typesHavingAMethodNamedBarWhichTakesOneParam() throws ParseException {
        InputStream is = AstExplorerTest.class.getResourceAsStream("/SomeJavaFile.java");
        CompilationUnit cu = JavaParser.parse(is);
        Function1<MethodDeclaration, Boolean> hasOneParam = m -> m.getParameters().size() == 1;
        Function1<MethodDeclaration, Boolean> isNamedBar = m -> m.getName().asString().equals("bar");
        List<TypeDeclaration<?>> res = typesThat(cu, hasAtLeastOneMethodThat(m ->
                hasOneParam.apply(m) && isNamedBar.apply(m)));
        assertEquals(1, res.length());
        assertEquals("B", res.get(0).getName().asString());
    }
 
}