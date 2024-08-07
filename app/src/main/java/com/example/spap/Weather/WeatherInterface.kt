package com.example.spap.Weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {
    // getVilageFcst : 동네 예보 조회
    @GET("getVilageFcst?serviceKey=EcOMAmwYy5vFpStPlSEREmS7QXOvnz24mjdnYtJBzOZCGfLKZ0ToMHpZnDipbG0T4II5aXACQ4%2BrJk3%2FuDXrxA%3D%3D")
    fun getWeather(
        @Query("numOfRows") num_of_rows: String,   // 한 페이지 경과 수
        @Query("pageNo") page_no: String,          // 페이지 번호
        @Query("dataType") data_type: String,   // 응답 자료 형식
        @Query("base_date") base_date: String,  // 발표 일자
        @Query("base_time") base_time: String,  // 발표 시각
        @Query("nx") nx: String,                // 예보지점 X 좌표
        @Query("ny") ny: String                 // 예보지점 Y 좌표
    ): Call<WEATHER>
}