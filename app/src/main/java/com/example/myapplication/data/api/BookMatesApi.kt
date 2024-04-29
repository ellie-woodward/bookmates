package com.example.myapplication.data.api

import com.example.myapplication.Player
import com.example.myapplication.data.model.JsonResponse
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.Token
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface BookMatesApi {

    //path('get_player_list/<str:account_id>/'),
    @GET(
        "get_player_list/<str:account_id>/"
    )
    suspend fun fetchPlayerListById(): List<Player>

    //path('get_player_account/name/<str:account_id>/<str:player_name>/')
    @GET(
        "get_player_account/name/<str:account_id>/<str:player_name>/"
    )
    suspend fun fetchPlayerAccountByIdAndName(): User

    //path('search_account/username/<str:account_username>/')
    @GET(
        "search_account/username/<str:account_username>/"
    )
    suspend fun fetchUserByName(name: String): User

    @GET(
        "get_csrf/"
    )
    suspend fun getToken(): Token

    @Headers("Content-Type: application/json")
    @POST(
        "account_login/"
    )
    fun loginData(@Body body: RequestBody): Call<LoginResponse>

//    suspend fun loginData(@Header("Authorization") token: String, @Body body: JSONObject): LoginResponse

    //path('create_account/',
    @Headers("Content-Type: application/json")
    @POST(
        "create_account/"
    )
    suspend fun createAccount(@Body body: JSONObject?): JsonResponse

//    //path('get_player_account/id/<str:account_id>/<str:player_id>/',               views.get_player_account_by_id,                           name='get_player_account_by_id'),
//    @GET(
//        "get_player_account/id/<str:account_id>/<str:player_id>/"
//    )
//
//    //path('get_template_list/<str:account_id>/',                                   views.get_template_list,                                  name='get_template_list'),
//    @GET(
//        "get_template_list/<str:account_id>/"
//    )
//
//    //path('get_template_account/name/<str:account_id>/<str:template_name>/',       views.get_template_account_by_template_name,              name='get_template_account_by_template_name'),
//    @GET(
//        "get_template_account/name/<str:account_id>/<str:template_name>/"
//    )
//
//    //path('get_template_account/id/<str:account_id>/<str:template_id>/',           views.get_template_account_by_template_id,                name='get_template_account_by_template_id'),
//        @GET(
//        "get_template_account/id/<str:account_id>/<str:template_id>/"
//    )
//
//    //path('get_boardgame_list/<str:account_id>/',                                  views.get_boardgame_list,                                 name='get_boardgame_list'),
//    @GET(
//        "get_boardgame_list/<str:account_id>/"
//    )
//
//    //path('get_boardgame_account/name/<str:account_id>/<str:boardgame_name>/',     views.get_boardgame_account_by_boardgame_name,            name='get_boardgame_account_by_boardgame_name'),
//    @GET(
//        "get_boardgame_account/name/<str:account_id>/<str:boardgame_name>/'"
//    )
//
//    //path('get_boardgame_account/id/<str:account_id>/<str:boardgame_id>/',         views.get_boardgame_account_by_boardgame_id,              name='get_boardgame_account_by_boardgame_id'),
//    @GET(
//        "get_boardgame_account/id/<str:account_id>/<str:boardgame_id>/"
//    )
//
//    //path('search_player/name/<str:player_name>/',                                 views.search_player_by_full_name,                         name='search_player_by_full_name'),
//    @GET(
//        "search_player/name/<str:player_name>/"
//    )
//
//    //path('search_player/id/<str:player_id>/',                                     views.search_player_by_id,                                name='search_player_by_id'),
//    @GET(
//        "search_player/od/<str:player_id>/"
//    )
//
//    //path('search_account/id/<str:account_id>/',                                   views.search_account_by_id,                               name='search_account_by_id'),
//    @GET(
//        "search_account/id/<str:account_id>/"
//    )
//
//
//    //path('assign_to_account/<str:account_id>/<str:assignable_id>/',                views.assign_to_account,                                 name='assign_to_account'),
//    @POST(
//        "assign_to_account/<str:account_id>/<str:assignable_id>/"
//    )
//
//    //path('create_boardgame/<str:account_id>/',                                    views.create_boardgame,                                   name='create_boardgame'),
//    @POST(
//        "create_boardgame/<str:account_id>/"
//    )
//
//    //path('create_template/<str:account_id>/',                                     views.create_template,                                    name='create_template'),
//    @POST(
//        "create_template/<str:account_id>/"
//    )
//
//    //path('create_player/<str:account_id>/',                                       views.create_player_by_account_id,                        name='create_player_by_account_id'),
//    @POST(
//        "create_player/<str:account_id>/"
//    )
//
//    //path('update_player/<str:account_id>/<str:player_id>/',                       views.update_player_by_id, name="update_player_by_id"),
//    @POST(
//        "update_player/<str:account_id>/<str:player_id>/"
//    )
//
//    //path('delete_template_account/<str:account_id>/<str:template_id>/',           views.delete_template_from_account,                       name='delete_template_from_account'),
//    @DELETE(
//        "delete_template_account/<str:account_id>/<str:template_id>/"
//    )
//
//    //path('delete_player_inside_account/<str:account_id>/<str:player_id>/' ,       views.delete_p_i_c,                                       name='delete_p_i_i_c' ),
//    @DELETE(
//        "delete_template_account/<str:account_id>/<str:template_id>/"
//    )
//
//    //path('delete_boardgame_account/<str:account_id>/<str:boardgame_id>/',         views.delete_boardgame_from_account,                      name='delete_boardgame_from_account'),
//    @DELETE(
//        "delete_boardgame_account/<str:account_id>/<str:boardgame_id>/"
//    )
}

//path('admin/', admin.site.urls),

    //# Unimplemented Methods
    //path('delete_player_database/name/<str:account_id>/<str:player_name>/',       views.delete_player_from_database_by_full_name,           name='delete_player_from_database_by_full_name'),
    //path('delete_player_database/id/<int:player_id>/',                            views.delete_player_from_database_by_id,                  name='delete_player_from_database_by_id'),
