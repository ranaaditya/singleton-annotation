package com.ranaaditya.singleton.compiler;

import com.ranaaditya.singleton.annotation.Singleton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class Processor extends AbstractProcessor {

    private ProcessingEnvironment processingenvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        processingenvironment = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        for (TypeElement typeElement : ElementFilter.typesIn(roundEnvironment.getElementsAnnotatedWith(Singleton.class))) {
            if (!checkForPrivateConstructors(typeElement)) return false;
            if (!checkForGetInstanceMethod(typeElement)) return false;
        }

        return true;
    }

    private boolean checkForPrivateConstructors(TypeElement typeElement) {
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(typeElement.getEnclosedElements());
        for (ExecutableElement constructor : constructors) {
            if (constructor.getModifiers().isEmpty() || !constructor.getModifiers().contains(Modifier.PRIVATE)) {
                processingenvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "constructor of a singleton class must be private", constructor);
                return false;
            }
        }
        return true;
    }

    private boolean checkForGetInstanceMethod(TypeElement typeElement) {
        List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
        for (ExecutableElement method : methods) {

            // check for name
            if (method.getSimpleName().contentEquals("getInstance")) {

                // check for return type
                if (processingenvironment.getTypeUtils().isSameType(method.getReturnType(), typeElement.asType())) {

                    // check for modifiers
                    if (method.getModifiers().contains(Modifier.PRIVATE)) {
                        processingenvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "getInstance method can't have a private modifier", method);
                        return false;
                    }
                    if (!method.getModifiers().contains(Modifier.STATIC)) {
                        processingenvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "getInstance method should have a static modifier", method);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {{
            add(Singleton.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
