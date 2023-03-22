package com.lemon.smartcampus.utils

import com.orhanobut.logger.Logger
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object UnifiedExceptionHandler {
    private const val TAG = "UnifiedException"

    suspend fun <T> handleSuspend(function: suspend () -> ResponseData<T>): NetworkState<T> {
        return try {
            val result = function.invoke()
            when (result.code) {
                200 -> result.data?.let { NetworkState.Success(result.data) }
                    ?: NetworkState.Success(msg = result.message)
                else ->
                    NetworkState.Error(result.message ?: "未知错误，请联系管理员")
            }
        } catch (e: SocketTimeoutException) {
            Logger.e(TAG, "链接超时")
            return NetworkState.Error("网络好像被UFO捉走了QAQ", SocketTimeoutException("网络好像被UFO捉走了QAQ"))
        } catch (e: ConnectException) {
            Logger.e(TAG, "无法连接到服务器")
            return NetworkState.Error("服务器离家出走了QAQ", ConnectException("无法连接到服务器"))
        }catch (e: HttpException){
            if (e.code() == 401) {
                Logger.e(TAG, "token失效")
                return NetworkState.Error("登录过期啦!", LoginException("token失效"))
            }else return NetworkState.Error("未知错误，请联系管理员", e)
        } catch (e: Exception) {
            e.message?.let { Logger.e(it) }?:Logger.e(e::class.toString())
            return NetworkState.Error("未知错误，请联系管理员", e)
        }
    }
}

class LoginException(message: String): Exception(message)