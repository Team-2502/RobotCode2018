package com.team2502.robot2018.utils.change;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeProcesor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        for(TypeElement typeElement : annotations)
        {
            for(Element element : roundEnv.getElementsAnnotatedWith(typeElement))
            {
                Change annotation = element.getAnnotation(Change.class);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "WARNING [Change]: " + typeElement.getQualifiedName() + " needs to be changed for the following reason: " + annotation.reason());

            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return new HashSet<>(Collections.singletonList(Change.class.getName()));
    }
}
