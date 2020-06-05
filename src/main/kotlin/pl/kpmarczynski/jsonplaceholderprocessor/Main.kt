package pl.kpmarczynski.jsonplaceholderprocessor

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

private const val DEFAULT_INDENTATION = 4
private const val JSON_FILE_EXTENSION = "json"
fun main() {
    getJsonArray("https://jsonplaceholder.typicode.com/posts")?.forEach { json ->
        savePostToFile(json as JSONObject, "posts")
    }
}

fun getJsonArray(url: String): JSONArray? =
    when (val result = url.httpGet().responseJson().third) {
        is Result.Success -> {
            try {
                result.get().array()
            } catch (ex: JSONException) {
                System.err.println("Can't parse result string to array")
                null
            }
        }
        is Result.Failure -> {
            System.err.println("Can't retrieve result")
            null
        }
    }


fun savePostToFile(post: JSONObject, path: String = ""): Unit =
    if (post.has("id")) {
        prepareDirectory(path)
        val postId = post["id"]
        try {
            val filePath = (if (path.isNotBlank()) "$path/" else "") + "$postId.$JSON_FILE_EXTENSION"
            File(filePath).writeText(post.toString(DEFAULT_INDENTATION))
            println("Post with id ${post["id"]} saved!")
        } catch (ex: IOException) {
            System.err.println("Post with id ${post["id"]} not saved")
        }
    } else {
        System.err.println("Can't save post without id")
    }

fun prepareDirectory(path: String) {
    if (path.isNotBlank() && !File(path).exists()) File(path).mkdir()
}
