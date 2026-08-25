package co.edu.unipiloto.ailearningmobile.network;

import co.edu.unipiloto.ailearningmobile.dto.LoginRequest;
import co.edu.unipiloto.ailearningmobile.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//retrofit implementa las operaciones  para inicio de sesión
public interface ApiService {

    @POST("api/auth/login") //cuando alguien ejecuta el login, hace una peticion hacia la esa ruta
    Call<LoginResponse> login(@Body LoginRequest request); //el request se convierte en el JSON de la peticion HTTP

}
