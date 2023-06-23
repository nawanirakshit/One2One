package android.sleek.construction.io.networking.coroutineRequestAdapter

import com.rakshit.one.model.ErrorModel
import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}



typealias GenericResponse<S> = NetworkResponse<S, ErrorModel>

@Suppress("UNCHECKED_CAST")
fun <T> NetworkResponse<Any, Any>.responseChecker(
    onSuccess: (T) -> Unit,
    onFailure: ((String) -> Unit)? = null,
    always: (() -> Unit)? = null
) {
    when (this) {
        is NetworkResponse.Success -> {
            onSuccess(this.body as T); always?.invoke(); }
        is NetworkResponse.ApiError -> {
            onFailure?.invoke((body as? ErrorModel)?.message ?: ""); always?.invoke();
        }
        is NetworkResponse.NetworkError -> {
            onFailure?.invoke(error?.localizedMessage ?: ""); always?.invoke();
        }
        is NetworkResponse.UnknownError -> {
            onFailure?.invoke(error?.localizedMessage ?: ""); always?.invoke();
        }
    }

}