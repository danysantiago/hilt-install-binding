import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

const val T = "\$T"

fun AnnotationMirror.getValue(propertyName: String): Any? {
    val typeElement = annotationType.asElement() as TypeElement
    val properties = ElementFilter.methodsIn(typeElement.enclosedElements)
        .associateBy { it.simpleName.toString() }
    return elementValues[properties[propertyName]]?.value
        ?: error("No annotation value found named '$propertyName'.")
}