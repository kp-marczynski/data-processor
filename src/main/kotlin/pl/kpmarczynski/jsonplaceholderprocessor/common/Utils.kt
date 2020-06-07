//package pl.kpmarczynski.jsonplaceholderprocessor.common
//
//import org.reflections.Reflections
//
//fun <T: Any> getObjectInstances(sharedInterface: Class<T>): List<T> {
//    val parserPackageReflections = Reflections(sharedInterface.`package`.name)
//    return parserPackageReflections.getSubTypesOf(sharedInterface).mapNotNull { it.kotlin.objectInstance }
//}
