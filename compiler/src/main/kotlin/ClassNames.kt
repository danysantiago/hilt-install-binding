import com.squareup.javapoet.ClassName

object ClassNames {
    val BINDS = ClassName.get("dagger", "Binds")
    val INSTALL_BINDING = ClassName.get("dany.hilt", "InstallBinding")
    val INSTALL_IN = ClassName.get("dagger.hilt", "InstallIn")
    val MODULE = ClassName.get("dagger", "Module")
}