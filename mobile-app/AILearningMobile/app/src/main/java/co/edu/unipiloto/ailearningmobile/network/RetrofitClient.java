package co.edu.unipiloto.ailearningmobile.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    private static final String BASE_URL = "http://127.0.0.1:8080/";  //hay que mantener activo el adb reverse, ya que es el puente entre la ip del pixel8 y el server
    //& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" devices
    //& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
    private static Retrofit retrofit;
    private static Retrofit getRetrofitInstance(){

        if (retrofit == null){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // muestra en logcat la peticion y respuesta

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder() // cliente interno http usado por retrofit de manera interna
                    .proxy(java.net.Proxy.NO_PROXY)
                    .addInterceptor(logging)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // AQUÍ ESTABA EL ERROR
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;
    }

    //esta seccion le pide a retrofit que cree una implementacion de ApiService
    public static ApiService getApiService(){

        return getRetrofitInstance()
                .create(ApiService.class); //genera una implementacion que entiende las anotaciones @POST @Body

    }

}
