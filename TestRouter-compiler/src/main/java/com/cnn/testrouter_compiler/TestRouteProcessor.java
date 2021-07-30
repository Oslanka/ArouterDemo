package com.cnn.testrouter_compiler;

import com.cnn.testrouter_annotation.CRoute;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class TestRouteProcessor extends AbstractProcessor {
    Messager messager;
    Filer filer;
    Elements elements;
    Types types;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        elements = processingEnvironment.getElementUtils();
        types = processingEnvironment.getTypeUtils();

    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(CRoute.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        print("CRouter begin");
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(CRoute.class);

        for (Element element : elements) {
            print("CRouter element--->"+element.toString());
            print(element.asType().toString());

            CRoute alias = element.getAnnotation(CRoute.class);

            try {
                generateCode(element, alias, (TypeElement) element);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void generateCode(Element e, CRoute alias, TypeElement clazz) throws IOException {

        print("CRouter ====>映射关系"+alias.path()+":"+e.toString());
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        FieldSpec.Builder holder = FieldSpec.builder(String.class, "holder").initializer("$S", alias.path()+":"+ e.toString());

        holder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        FieldSpec android = holder
                .build();


        TypeSpec helloWorld = TypeSpec.classBuilder(alias.path().replace("/","C$")+"HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addField(android)
                .build();

        JavaFile javaFile = JavaFile.builder("com.cnn.crouter", helloWorld)
                .build();

//        "com.360.android.arouter.routes"+alias.path().replace("/","$$"
//        testHello();
//        JavaFileObject f = filer.createSourceFile("CRoutter"+"$$"+clazz.getQualifiedName());
//        print("Create inner class===========:" + f.toUri());
        javaFile.writeTo(filer);

//
//        Writer w = f.openWriter();
//        try {
//            String qualifiedName = clazz.getQualifiedName().toString();
//            PrintWriter pw = new PrintWriter(w);
//            pw.println("package " + qualifiedName.substring(0 , qualifiedName.lastIndexOf(".")) + ";");
//            pw.println("\n class " +  clazz.getSimpleName() + "GenCRoute { ");
//            pw.println("\n protected final void showValue() {");
//            pw.println("\n System.out.println(\"value:"+ alias.path() +"\");");
//            pw.println("      }");
//            pw.println("}");
//        }finally {
//            w.close();
//        }

    }

    private void print(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}