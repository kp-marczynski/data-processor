# JSONPlaceholder Processor
Goal of this app is to process posts from [JSONPlaceholder site](https://jsonplaceholder.typicode.com/) and save them to individual *.json files.

## How to run
Use gradle to build and run project:

    gradlew run
    
Alternatively you can use precompiled jar:

    java -jar json-placeholder-processor.jar

## How to build
To build fat jar run:
    
    gradlew clean build
    
## Used libraries
* [Fuel](https://github.com/kittinunf/fuel)

## Minimal solution
Following code could be used as minimal happy-path solution for the considered problem:

    import com.github.kittinunf.fuel.httpGet
    import com.github.kittinunf.fuel.json.responseJson
    import org.json.JSONObject
    import java.io.File
    
    fun minimalSolution() =
        "https://jsonplaceholder.typicode.com/posts".httpGet().responseJson().third.get().array().forEach {
            if (it is JSONObject) File("${(it)["id"]}.json").writeText(it.toString())
        }
