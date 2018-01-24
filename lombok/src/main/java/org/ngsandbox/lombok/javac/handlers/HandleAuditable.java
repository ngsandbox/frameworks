package org.ngsandbox.lombok.javac.handlers;


import org.ngsandbox.lombok.Auditable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

public class HandleAuditable extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Auditable.class)) {

//            if (element.getKind() != ElementKind.CLASS) {
//                messager.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.");
//                return true;
//            }
//
//            TypeElement typeElement = (TypeElement) element;
//            activitiesWithPackage.put(
//                    typeElement.getSimpleName().toString(),
//                    elements.getPackageOf(typeElement).getQualifiedName().toString());
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return null;
    }
}

