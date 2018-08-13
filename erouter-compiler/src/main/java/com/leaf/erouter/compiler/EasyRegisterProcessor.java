package com.leaf.erouter.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.leaf.erouter.annotation.EasyRegister;
import com.leaf.erouter.annotation.bean.RegisterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by suhong01 on 2018/7/4.
 */

@AutoService(Processor.class)
public class EasyRegisterProcessor extends AbstractProcessor {

    private Messager messager;
    private ProcessingEnvironment processingEnvironment;
    private Map<String, Map<String, RegisterBean>> eventMap = new HashMap<>();

    private Elements elementUtil;
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
        this.messager = processingEnvironment.getMessager();
        this.elementUtil = processingEnvironment.getElementUtils();
        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null) {
            moduleName = options.get("moduleName");
            messager.printMessage(Diagnostic.Kind.NOTE,
                    "EasyRegisterProcessor moduleName = " + moduleName);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        eventMap.clear();
        MethodSpec.Builder loadInfoBuild = MethodSpec.methodBuilder("loadInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(Map.class, "mEventMap", Modifier.FINAL)
                .returns(void.class);

        for (Element element : roundEnvironment.getElementsAnnotatedWith(EasyRegister.class)) {
            //            EasyRouter eRouter = element.getAnnotation(EasyRouter.class);
            ExecutableElement executableElement = (ExecutableElement) element;
            String methodName = executableElement.getSimpleName().toString();

            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullName = typeElement.getQualifiedName().toString();

            List<? extends VariableElement> methodParameters = executableElement.getParameters();
            List<String> types = new ArrayList<>();

            TypeMirror methodParameterType;
            for (VariableElement variableElement : methodParameters) {
                methodParameterType = variableElement.asType();
                if (methodParameterType instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) methodParameterType;
                    methodParameterType = typeVariable.getUpperBound();
                }
                //                String parameterName = variableElement.getSimpleName().toString();
                String parameteKind = methodParameterType.toString();
                types.add(parameteKind);
            }

            if (types.size() > 0) {
                messager.printMessage(Diagnostic.Kind.NOTE,
                        "Annotation fullName = " + fullName + "  methodName=" + methodName + " parastype=" + types.get
                                (0));
                String key = types.get(0);
                loadInfoBuild.addStatement(" if (mEventMap.containsKey($S)) {", key);
                loadInfoBuild.addStatement("  $T map =($T) mEventMap.get($S)", Map.class, Map.class, key);
                loadInfoBuild.addStatement("map.put($S,new $T($T.class,$S,$T.class))", fullName, RegisterBean
                                .class, ClassName.get(typeElement.asType()), methodName,
                        ClassName.get(methodParameters.get(0).asType()));
                loadInfoBuild.addStatement("  } else {");
                loadInfoBuild.addStatement("   $T map = new $T<>()", Map.class, HashMap.class);
                loadInfoBuild.addStatement("map.put($S,new $T($T.class,$S,$T.class))", fullName, RegisterBean
                                .class, ClassName.get(typeElement.asType()), methodName,
                        ClassName.get(methodParameters.get(0).asType()));
                loadInfoBuild.addStatement("  mEventMap.put($S, map)", key);
                loadInfoBuild.addStatement("  }");

            }

        }

        MethodSpec loadInfoSpec = loadInfoBuild.build();

        TypeElement iRegisterElement = elementUtil.getTypeElement("com.leaf.erouter.api.template.IRegister");
        ClassName iRegister = ClassName.get(iRegisterElement);

        String rooterClass = "EasyRegisterManager$$" + this.moduleName;
        TypeSpec routerSpec = TypeSpec.classBuilder(rooterClass)
                .addMethod(loadInfoSpec)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(iRegister)
                .build();
        JavaFile javaFile = JavaFile.builder("com.leaf.erouter.register", routerSpec)
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
        return Collections.singleton(EasyRegister.class.getCanonicalName());
    }
}
