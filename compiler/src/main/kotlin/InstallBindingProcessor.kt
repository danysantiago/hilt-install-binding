/*
 * Copyright 2021 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType.ISOLATING
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

@AutoService(Processor::class)
@IncrementalAnnotationProcessor(ISOLATING)
class InstallBindingProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = setOf(ClassNames.INSTALL_BINDING.canonicalName())

    override fun getSupportedSourceVersion() = SourceVersion.latest()

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val processingAnnotation = annotations.firstOrNull() ?: return false
        roundEnv.getElementsAnnotatedWith(processingAnnotation).forEach {
            processElement(it as TypeElement)
        }
        return false
    }

    private fun processElement(typeElement: TypeElement) {
        val annotation = typeElement.annotationMirrors.first {
            val annotationElement = it.annotationType.asElement() as TypeElement
            return@first annotationElement.qualifiedName.toString() == ClassNames.INSTALL_BINDING.canonicalName()
        }
        val component = annotation.getValue("component") as DeclaredType
        val boundType = if (typeElement.interfaces.size > 1) {
            val declaredBoundType = annotation.getValue("boundType") as DeclaredType
            if (TypeName.get(declaredBoundType) == TypeName.OBJECT) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "$typeElement implements multiple interfaces and no boundType was specified.",
                    typeElement
                )
                return
            }
            declaredBoundType
        } else {
            typeElement.interfaces.first()
        }
        val bindingClassName = ClassName.get(typeElement)
        val boundTypeName = TypeName.get(boundType)
        val moduleClassName = ClassName.get(
            bindingClassName.packageName(),
            "${bindingClassName.simpleName()}_HiltBindingModule"
        )
        val hiltModuleSpec = TypeSpec.interfaceBuilder(moduleClassName)
            .addOriginatingElement(typeElement)
            .addGeneratedAnnotation(processingEnv.elementUtils, processingEnv.sourceVersion)
            .addAnnotation(ClassNames.MODULE)
            .addAnnotation(
                AnnotationSpec.builder(ClassNames.INSTALL_IN)
                    .addMember("value", "$T.class", TypeName.get(component))
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassNames.ORIGINATING_ELEMENT)
                    .addMember(
                        "topLevelClass",
                        "$T.class",
                        bindingClassName.topLevelClassName()
                    )
                    .build()
            )
            .addModifiers(Modifier.PUBLIC)
            .addMethod(
                MethodSpec.methodBuilder("bind")
                    .addAnnotation(ClassNames.BINDS)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(boundTypeName)
                    .addParameter(bindingClassName, "impl")
                    .build()
            )
            .build()
        JavaFile.builder(bindingClassName.packageName(), hiltModuleSpec)
            .build()
            .writeTo(processingEnv.filer)
    }
}