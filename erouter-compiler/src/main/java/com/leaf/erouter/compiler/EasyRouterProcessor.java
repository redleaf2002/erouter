package com.leaf.erouter.compiler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.leaf.erouter.annotation.EasyRouter;
import com.leaf.erouter.annotation.bean.RouterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by suhong01 on 2018/7/4.
 */

@AutoService(Processor.class)
public class EasyRouterProcessor extends AbstractProcessor {

    private Messager messager;
    private ProcessingEnvironment processingEnvironment;
    private Map<String, RouterBean> infoMap = new HashMap<>();
    private Elements elementUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
        this.messager = processingEnvironment.getMessager();
        this.elementUtil = processingEnvironment.getElementUtils();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        infoMap.clear();

        MethodSpec.Builder loadInfoBuild = MethodSpec.methodBuilder("loadInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(Map.class, "infoMap", Modifier.FINAL)
                .returns(void.class);

        for (Element element : roundEnvironment.getElementsAnnotatedWith(EasyRouter.class)) {
            EasyRouter eRouter = element.getAnnotation(EasyRouter.class);
            TypeElement typeElement = (TypeElement) element;
            String fullName = typeElement.getQualifiedName().toString();
            String path = eRouter.path();

            messager.printMessage(Diagnostic.Kind.NOTE,
                    "Annotation path = " + fullName + "  asType=" + typeElement.asType());
            loadInfoBuild.addStatement("infoMap.put($S,new $T($S,$T.class))", path, RouterBean.class, path,
                    ClassName.get(typeElement.asType())
            );
        }

        MethodSpec loadInfoSpec = loadInfoBuild.build();
        TypeElement iRouterElement = elementUtil.getTypeElement("com.leaf.erouter.api.template.IRouter");
        ClassName iRouter = ClassName.get(iRouterElement);

        TypeSpec routerSpec = TypeSpec.classBuilder("EasyRouterManager")
                .addMethod(loadInfoSpec)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(iRouter)
                .build();
        JavaFile javaFile = JavaFile.builder("com.leaf.erouter.router", routerSpec)
                .build();
        try {
            javaFile.writeTo(processingEnvironment.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EasyRouter.class.getCanonicalName());
    }
}
