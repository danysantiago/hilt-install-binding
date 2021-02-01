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

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements

const val T = "\$T"
const val S = "\$S"

internal fun AnnotationMirror.getValue(propertyName: String): Any? {
    val typeElement = annotationType.asElement() as TypeElement
    val properties = ElementFilter.methodsIn(typeElement.enclosedElements)
        .associateBy { it.simpleName.toString() }
    return elementValues[properties[propertyName]]?.value
        ?: error("No annotation value found named '$propertyName'.")
}
internal fun TypeSpec.Builder.addGeneratedAnnotation(
    elements: Elements,
    sourceVersion: SourceVersion
) = apply {
    generatedAnnotationSpec(
        elements,
        sourceVersion,
        InstallBindingProcessor::class.java
    )?.let { generatedAnnotation ->
        addAnnotation(generatedAnnotation)
    }
}

private fun generatedAnnotationSpec(
    elements: Elements,
    sourceVersion: SourceVersion,
    processorClass: Class<*>
) = elements.getTypeElement(
    if (sourceVersion.compareTo(SourceVersion.RELEASE_8) > 0) {
        "javax.annotation.processing.Generated"
    } else {
        "javax.annotation.Generated"
    }
)?.let {
    AnnotationSpec.builder(ClassName.get(it))
        .addMember("value", S, processorClass.canonicalName)
        .build()
}