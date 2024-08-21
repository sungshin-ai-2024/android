package com.example.savewith_android

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class WebSocketListener(private val chartUpdateCallback: (List<Float>, List<Float>, List<Float>, Int, Int) -> Unit) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("Socket", "WebSocket opened: $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("Socket","Receiving : $text")
        handleIncomingData(text)

    }

    private fun onDataReceived() {
        val ppgDataString = WebSocketDataManager.ppgData
        Log.d("DataCheck", "ppgDataString: $ppgDataString")

        if (ppgDataString != null) {
            try {
                val ppgDataArray = JSONArray(ppgDataString)

                if (ppgDataArray.length() > 0) {
                    val innerJsonArrayString = ppgDataArray.getString(0)
                    val actualPpgDataArray = JSONArray(innerJsonArrayString)

                    val ppgDataList = mutableListOf<Float>()
                    for (i in 0 until actualPpgDataArray.length()) {
                        ppgDataList.add(actualPpgDataArray.getDouble(i).toFloat())
                    }

                    Log.d("DataSuccess", "Parsed ppgData: $ppgDataList")
                }
            } catch (e: JSONException) {
                Log.e("DataError", "Failed to parse ppgData as JSONArray", e)
            }
        } else {
            Log.e("DataError", "ppgData is null or not a valid string")
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("Socket", "Receiving bytes : $bytes")
    }


    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Socket","Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    private fun handleIncomingData(data: String) {
        try {
            val jsonObject = JSONObject(data)

            // 전체 JSON 데이터 로그 출력
            Log.d("DataProcessing", "Received JSON data: $jsonObject")

            val ppgDataArray = jsonObject.getJSONArray("ppg_data")
            val ppgPredictDataArray = jsonObject.getJSONArray("predictions")
            val accPredictArray = jsonObject.getJSONArray("acc_predictions")
            val bpm: Int = jsonObject.getInt("bpm_data")
            val step : Int = jsonObject.getInt("step_count")
            Log.d("DataProcessing", "ppg_data array length: ${ppgDataArray.length()}")

            if (ppgDataArray.length() > 0) {
                val ppgDataList = mutableListOf<Float>()
                for (i in 0 until ppgDataArray.length()) {
                    ppgDataList.add(ppgDataArray.getDouble(i).toFloat())
                }

                val predictDataList = mutableListOf<Float>()
                for (i in 0 until ppgPredictDataArray.length()) {
                    predictDataList.add(ppgPredictDataArray.getDouble(i).toFloat())
                }

                val accPredictList = mutableListOf<Float>()
                for (i in 0 until accPredictArray.length()) {
                    accPredictList.add(accPredictArray.getDouble(i).toFloat())
                }

                // 데이터가 올바르게 파싱되었는지 확인
                Log.d("DataSuccess", "Parsed ppgData: $ppgDataList")
                Log.d("DataSuccess", "Parsed ppgDataArray: $ppgDataArray")

                // 데이터 매니저에 업데이트
                WebSocketDataManager.ppgData = ppgDataList
                WebSocketDataManager.predictions = predictDataList
                WebSocketDataManager.accPredictions = accPredictList
                WebSocketDataManager.bpm = bpm
                WebSocketDataManager.step = step


                chartUpdateCallback(ppgDataList, predictDataList, accPredictList, bpm, step)
            } else {
                Log.e("DataError", "ppgDataArray is empty")
            }

        } catch (e: JSONException) {
            Log.e("DataError", "Failed to parse JSON: ${e.message}")
        } catch (e: Exception) {
            Log.e("DataError", "General error: ${e.message}")
        }
    }

    private fun JSONArray.toList(): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until this.length()) {
            list.add(this.get(i))
        }
        return list
    }
}

