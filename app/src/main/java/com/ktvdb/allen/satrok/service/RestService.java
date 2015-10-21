package com.ktvdb.allen.satrok.service;


import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.AlbumQueryCondition;
import com.ktvdb.allen.satrok.model.AppVersion;
import com.ktvdb.allen.satrok.model.FullSearchResult;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.model.Marquee;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.MovieQueryCondition;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.PlayActionResult;
import com.ktvdb.allen.satrok.model.PlayRecord;
import com.ktvdb.allen.satrok.model.RoomInfo;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.SingerQueryCondition;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.socket.RoomStatusResponses;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import rx.Observable;

/**
 * Created by Allen on 15/7/11.
 */
public interface RestService
{
    @GET("/api/live/types")
    Observable<List<SysDictionary>> getLpTypes();

    @GET("/api/live/all")
    Observable<List<LiveProgram>> getLives();

    @GET("/api/live/type/{typeCode}")
    Observable<List<LiveProgram>> getLiveByType(@Path("typeCode") String typeCode);

    @GET("/api/movie/types")
    Observable<List<SysDictionary>> getMovieTypes();

    @POST("/api/movie/movies")
    Observable<PageResponse<Movie>> getMovies(@Body MovieQueryCondition condition);

    @GET("/marquee/all")
    Observable<List<Marquee>> getMarquees();

    @GET("/api/singer/types")
    Observable<List<SysDictionary>> getSingerType();

    @POST("/api/singer/singers")
    Observable<PageResponse<Singer>> getSingers(@Body SingerQueryCondition condition);

    @GET("/api/singer/{singerId}")
    Observable<Singer> getSinger(@Path("singerId") String singerId);

    @POST("/api/singer/albums")
    Observable<PageResponse<Album>> getAlbums(@Body AlbumQueryCondition condition);

    @GET("/api/singer/album/{albumID}")
    Observable<Album> getAlbum(@Path("albumID") String albumID);

    @POST("/api/song/singer")
    Observable<PageResponse<Song>> getSongBySinger(@Body SongQueryCondition condition);

    @POST("/api/song/songs")
    Observable<PageResponse<Song>> getAllSongs(@Body SongQueryCondition condition);

    @GET("/api/song/languages")
    Observable<List<SysDictionary>> getLanguages();

    @GET("/api/song/category/{id}")
    Observable<List<SysDictionary>> getSongCategory(@Path("id") String id);

    @GET("/api/config/appversion")
    Observable<AppVersion> getAppversion();

    @GET("/api/config/roominfo/{device}")
    Observable<RoomInfo> getRoomInfo(@Path("device") String device);

    @GET("/api/singer/album/recommend")
    Observable<PageResponse<Album>> getRecommendAlbums();


    @GET(("/api/search/{searchText}"))
    Observable<FullSearchResult> fullSearch(@Path("searchText") String searchText);

    /**
     * 获取一体机文字广告
     *
     * @param roomCode 房间号
     * @return 广告列表
     */
    @GET("/api/ad/stb_texts/{roomCode}")
    Observable<List<Advertisement>> getMainTextAds(@Path("roomCode") String roomCode);

    /**
     * 获取电视文字广告
     *
     * @param roomCode 房间号
     * @return 广告列表
     */
    @GET("/api/ad/tv_texts/{roomCode}")
    Observable<List<Advertisement>> getTVTextAds(@Path("roomCode") String roomCode);

    /**
     * 获取首页滚动图片广告
     *
     * @param roomCode 房间号
     * @return 图片广告
     */
    @GET("/api/ad/home_images/{roomCode}")
    Observable<List<Advertisement>> getHomeImageAds(@Path("roomCode") String roomCode);

    /**
     * 获取歌曲详细信息（含专辑）
     *
     * @param songId 歌曲ID
     * @return song
     */
    @GET("/api/song/detail/{id}")
    Observable<Song> getSongInfo(@Path("id") String songId);

    /**
     * 获取左侧滚动图片广告
     *
     * @param roomCode 房间号
     * @return 图片广告
     */
    @GET("/api/ad/left_images/{roomCode}")
    Observable<List<Advertisement>> geLeftImageAds(@Path("roomCode") String roomCode);

    /**
     * 获取电视屏幕广告
     *
     * @param roomCode 房间号
     * @return 图片广告
     */
    @GET("/api/ad/tv_left_images/{roomCode}")
    Observable<List<Advertisement>> getTVImageAds(@Path("roomCode") String roomCode);

    /**
     * 行为数据
     *
     * @param playRecord 行为
     * @return 返回
     */
    @POST("/api/playback/add")
    Observable<PlayActionResult> addPlayAction(@Body PlayRecord playRecord);

    @Multipart
    @POST("/api/playback/upload")
    Observable<PlayActionResult> uploadImage(@Part("roomStatus") RoomStatusResponses responses,
                                             @Part("file")
                                             TypedFile file);
}
