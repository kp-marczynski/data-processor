package pl.kpmarczynski.jsonplaceholderprocessor

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

fun main() {
    val path = "posts"
    File(path).mkdir()
    getPosts()?.forEach { json ->
        savePostToFile(json as JSONObject, path)
    }
}

fun getPosts(): JSONArray? {
    return try {
        "https://jsonplaceholder.typicode.com/posts".httpGet().responseJson().third.get().array()
    } catch (ex: Exception) {
        null
    }
}

fun savePostToFile(post: JSONObject, path: String) {
    File("$path/${(post)["id"]}.json").writeText(post.toString(4))
}
