package com.example.test_processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by liyunlei
 * on 2018/4/19.
 * email: YunLeiLi@sf-express.com
 *
 *      很明显这是一个 TestAnnotationClass 的集合
 *
 *      这个集合中的 TestAnnotationClass 的 canonicalName 是一样的
 *      即，所有 type 字段相同的注解，都在一个 TestGroupedClasses 中
 */

public class TestGroupedClasses {

    private static final String SUFFIX = "Factory";
    private String mCanonicalName;
    // id 对应着被注解的类
    private Map<String, TestAnnotationClass> mItemsMap = new HashMap<>();

    public TestGroupedClasses(String canonicalName) {
        this.mCanonicalName = canonicalName;
    }

    public void add(TestAnnotationClass annotationClass) throws IdAlreadyUsedException {
        String id = annotationClass.getId();
        TestAnnotationClass testAnnotationClass = mItemsMap.get(id);
        if (testAnnotationClass != null) {
            throw new IdAlreadyUsedException("注解的id不能相同", testAnnotationClass.getTypeElement());
        }

        mItemsMap.put(id, annotationClass);
    }

    /**
     * 使用 javapoet 来创建代码
     * @param elementUtils
     * @param filer
     * @throws IOException
     */
    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(mCanonicalName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;
//        String canonicalNameFactoryClassName = mCanonicalName + SUFFIX;
        PackageElement pkg = elementUtils.getPackageOf(superClassName);
        String packageName = pkg.getQualifiedName().toString();

        Modifier modifier = Modifier.ABSTRACT;

        MethodSpec.Builder method = MethodSpec.methodBuilder("create")
                .addModifiers()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "id")
                .returns(TypeName.get(superClassName.asType()));

        // check if id is null
        method.beginControlFlow("if (id == null)")
                .addStatement("throw new IllegalArgumentException($S)", "id is null!")
                .endControlFlow();

        // Generate items map
        for (TestAnnotationClass item : mItemsMap.values()) {
            method.beginControlFlow("if ($S.equals(id))", item.getId())
                    .addStatement("return new $L()", item.getTypeElement().getQualifiedName().toString())
                    .endControlFlow();
        }

        method.addStatement("throw new IllegalArgumentException($S + id)", "Unknown id = ");

        TypeSpec typeSpec = TypeSpec.classBuilder(factoryClassName).addMethod(method.build()).build();
        // Write file
        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }

}
