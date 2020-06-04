# JSONPlaceholder Processor
Goal of this app is to process posts from [JSONPlaceholder site](https://jsonplaceholder.typicode.com/) and save them to individual *.json files.

## How to run

    gradlew run
    
or

    gradlew clean build
    java -jar json-placeholder-processor-<VERSION_NUMBER>.jar

## Used libraries
* [Fuel](https://github.com/kittinunf/fuel)

## Minimal solution for happy path

    import com.github.kittinunf.fuel.httpGet
    import com.github.kittinunf.fuel.json.responseJson
    import org.json.JSONObject
    import java.io.File
    
    fun minimalSolution() =
        "https://jsonplaceholder.typicode.com/posts".httpGet().responseJson().third.get().array().forEach {
            if (it is JSONObject) File("${(it)["id"]}.json").writeText(it.toString())
        }
