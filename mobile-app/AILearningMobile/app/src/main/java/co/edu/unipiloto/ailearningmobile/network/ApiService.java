package co.edu.unipiloto.ailearningmobile.network;

import co.edu.unipiloto.ailearningmobile.dto.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

//retrofit implementa las operaciones  para inicio de sesión
public interface ApiService {

    @POST("api/auth/login") //cuando alguien ejecuta el login, hace una peticion hacia la ruta
    Call<LoginResponse> login(@Body LoginRequest request); //el request se convierte en el JSON de la peticion HTTP

    @POST("api/auth/register")
    Call<ResponseBody> register(@Body RegisterRequest request);

    @PUT("api/usuarios/{id}/rol")
    Call<ResponseBody> cambiarRol(@Path("id") Long id, @Body CambiarRolRequest request);

    @GET("api/usuarios/buscar")
    Call<List<UsuarioResponse>> buscarUsuarios(
            @Query("termino") String termino
    );

}

