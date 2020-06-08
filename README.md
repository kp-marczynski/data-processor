# JSONPlaceholder Processor
This script allows user to download data from provided source and save it with selected writer.

By default it downloads jsonarray from [JSONPlaceholder site](https://jsonplaceholder.typicode.com/) and save json object to separate json files.

## How to run with default values
Use gradle to build and run project:

    gradlew run
    
Alternatively you can use precompiled jar:

    java -jar json-placeholder-processor.jar


## Available run options

    -r, --request-type TEXT  Request type protocol. Supported options: http, https. Default: https
    -p, --parser-type TEXT   Data type for parser. Supported options: jsonarray. Default: jsonarray
    -o, --output-type TEXT   Output type. Supported options: file. Default: file
    -s, --source TEXT        Source from data should be downloaded. Default: https://jsonplaceholder.typicode.com/posts
    --parser-config VALUE    Collection of configuration pairs for parser
    --writer-config VALUE    Collection of configuration pairs for writer
    -h, --help               Show this message and exit



### Config values
User can define parser and writer configuration as dynamic map of key-value pairs. 
Following are keys currently supported:
* for parser type "jsonarray":
    * indentation - Indentation of parsed data. Default: "0"
    * identifierKey - Custom identifier key in parsed data. Default: "id"
* for output type "file"
    * path - Directory in which files should be written. Default: "posts"
    * extension - Extension of written files. Default: ".json"
    

### Example script invocation with parameters
    
    gradlew run --args="-r https --source https://test.com --parser-config indentation=5 --parser-config identifierKey=name --writer-config extension=xml --writer-config path=sample"
    

## How to build
To build fat jar run:
    
    gradlew clean build

## Used libraries
* [Fuel](https://fuel.gitbook.io/documentation/) - HTTP networking library for Kotlin
* [Clikt](https://ajalt.github.io/clikt/) - Command Line Interface for Kotlin library 
* [Mockk](https://mockk.io/) - Mocking library for Kotlin
* [Kotlin logging](https://github.com/MicroUtils/kotlin-logging) - Lightweight logging framework for Kotlin
* [org.JSON](https://www.json.org/) - Library for parsing json

## Bonus
Following code could be used as minimal happy-path solution for the considered problem:

    import com.github.kittinunf.fuel.httpGet
    import com.github.kittinunf.fuel.json.responseJson
    import org.json.JSONObject
    import java.io.File
    
    fun minimalSolution() =
        "https://jsonplaceholder.typicode.com/posts".httpGet().responseJson().third.get().array().forEach {
            if (it is JSONObject) File("${(it)["id"]}.json").writeText(it.toString())
        }
