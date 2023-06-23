package android.sleek.construction.io.networking.coroutineRequestAdapter

import java.io.IOException

class NetworkException(error: String): IOException(error)