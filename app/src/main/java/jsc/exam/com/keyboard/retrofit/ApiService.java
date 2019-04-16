package jsc.exam.com.keyboard.retrofit;

import io.reactivex.Observable;
import jsc.exam.com.keyboard.BuildConfig;
import retrofit2.http.GET;

public interface ApiService {

    @GET(BuildConfig.VERSION_URL)
    Observable<String> getVersionInfo();

}
