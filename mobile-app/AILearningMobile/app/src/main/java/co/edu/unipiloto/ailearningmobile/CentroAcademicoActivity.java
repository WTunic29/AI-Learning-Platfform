package co.edu.unipiloto.ailearningmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.*;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentroAcademicoActivity extends AppCompatActivity {

    private static final int PICK_FILE = 2001;
    private ApiService api;
    private Long usuarioId;
    private String rol;
    private Uri archivoSeleccionado;

    private EditText editCursoId, editActividadId, editMensajeForo, editMotivoReclamo;
    private TextView textResultado, textUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro_academico);

        api = RetrofitClient.getApiService();
        usuarioId = getIntent().getLongExtra("usuarioId", -1L);
        rol = getIntent().getStringExtra("rol");
        if (rol == null) rol = "ESTUDIANTE";

        editCursoId = findViewById(R.id.editCursoId);
        editActividadId = findViewById(R.id.editActividadId);
        editMensajeForo = findViewById(R.id.editMensajeForo);
        editMotivoReclamo = findViewById(R.id.editMotivoReclamo);
        textResultado = findViewById(R.id.textResultado);
        textUsuario = findViewById(R.id.textUsuarioCentro);
        textUsuario.setText("Usuario ID: " + usuarioId + " | Rol: " + rol);

        findViewById(R.id.btnNotificaciones).setOnClickListener(v -> cargarNotificaciones());
        findViewById(R.id.btnActividades).setOnClickListener(v -> cargarActividades());
        findViewById(R.id.btnSeleccionarArchivo).setOnClickListener(v -> seleccionarArchivo());
        findViewById(R.id.btnEntregar).setOnClickListener(v -> entregarArchivo());
        findViewById(R.id.btnCrearReclamo).setOnClickListener(v -> crearReclamo());
        findViewById(R.id.btnVerReclamos).setOnClickListener(v -> cargarReclamos());
        findViewById(R.id.btnEnviarForo).setOnClickListener(v -> enviarForo());
        findViewById(R.id.btnVerForo).setOnClickListener(v -> cargarForo());
        findViewById(R.id.btnReporte).setOnClickListener(v -> cargarReporte());
        findViewById(R.id.btnRecursos).setOnClickListener(v -> cargarRecursos());
    }

    private Long valorLong(EditText edit, String nombre) {
        try { return Long.parseLong(edit.getText().toString().trim()); }
        catch (Exception e) { Toast.makeText(this, "Ingresa " + nombre + " válido", Toast.LENGTH_SHORT).show(); return null; }
    }

    private void cargarNotificaciones() {
        api.obtenerNotificaciones(usuarioId).enqueue(new Callback<List<NotificacionResponse>>() {
            @Override public void onResponse(Call<List<NotificacionResponse>> c, Response<List<NotificacionResponse>> r) {
                if (!r.isSuccessful() || r.body()==null) { mostrar("Error notificaciones: " + r.code()); return; }
                StringBuilder sb=new StringBuilder("NOTIFICACIONES\n");
                for(NotificacionResponse n:r.body()) sb.append("#").append(n.getId()).append(" | ").append(n.getTipo()).append(" | ").append(n.isLeida()?"Leída":"Nueva").append("\n").append(n.getMensaje()).append("\n\n");
                mostrar(sb.toString());
                for(NotificacionResponse n:r.body()) if(!n.isLeida()){ api.marcarNotificacionLeida(n.getId()).enqueue(vacioNotificacion()); break; }
            }
            @Override public void onFailure(Call<List<NotificacionResponse>> c, Throwable t){ mostrar("Sin conexión: "+t.getMessage()); }
        });
    }

    private Callback<NotificacionResponse> vacioNotificacion(){ return new Callback<NotificacionResponse>(){
        @Override public void onResponse(Call<NotificacionResponse> c,Response<NotificacionResponse> r){}
        @Override public void onFailure(Call<NotificacionResponse> c,Throwable t){}
    };}

    private void cargarActividades() {
        Long cursoId=valorLong(editCursoId,"curso ID"); if(cursoId==null)return;
        api.obtenerActividadesCurso(cursoId).enqueue(new Callback<List<ActividadResponse>>() {
            @Override public void onResponse(Call<List<ActividadResponse>> c, Response<List<ActividadResponse>> r){
                if(!r.isSuccessful()||r.body()==null){mostrar("Error actividades: "+r.code());return;}
                StringBuilder sb=new StringBuilder("ACTIVIDADES DEL CURSO "+cursoId+"\n");
                for(ActividadResponse a:r.body()) sb.append("#").append(a.getId()).append(" | ").append(a.getTipo()).append(" | ").append(a.getDescripcion()).append(" | ").append(a.getPonderacion()).append("%\nApertura: ").append(a.getFechaApertura()).append(" | Cierre: ").append(a.getFechaCierre()).append("\n\n");
                mostrar(sb.toString());
            }
            @Override public void onFailure(Call<List<ActividadResponse>> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void seleccionarArchivo(){ Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT); i.setType("*/*"); i.addCategory(Intent.CATEGORY_OPENABLE); startActivityForResult(i,PICK_FILE); }

    @Override protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_FILE && resultCode==RESULT_OK && data!=null){ archivoSeleccionado=data.getData(); mostrar("Archivo seleccionado: "+nombreArchivo(archivoSeleccionado)); }
    }

    private String nombreArchivo(Uri uri){
        String n="entrega"; Cursor c=getContentResolver().query(uri,null,null,null,null);
        if(c!=null){ int idx=c.getColumnIndex(OpenableColumns.DISPLAY_NAME); if(c.moveToFirst()&&idx>=0)n=c.getString(idx); c.close(); }
        return n;
    }

    private void entregarArchivo(){
        Long actividadId=valorLong(editActividadId,"actividad ID"); if(actividadId==null)return;
        if(archivoSeleccionado==null){Toast.makeText(this,"Selecciona un archivo",Toast.LENGTH_SHORT).show();return;}
        try{
            InputStream in=getContentResolver().openInputStream(archivoSeleccionado); ByteArrayOutputStream out=new ByteArrayOutputStream(); byte[] b=new byte[8192]; int n; while((n=in.read(b))>0)out.write(b,0,n); in.close();
            String mime=getContentResolver().getType(archivoSeleccionado); if(mime==null)mime="application/octet-stream";
            RequestBody rb=RequestBody.create(out.toByteArray(), MediaType.parse(mime));
            MultipartBody.Part part=MultipartBody.Part.createFormData("archivo",nombreArchivo(archivoSeleccionado),rb);
            api.entregarActividad(actividadId,usuarioId,part).enqueue(new Callback<EntregaActividadResponse>(){
                @Override public void onResponse(Call<EntregaActividadResponse> c,Response<EntregaActividadResponse> r){ if(r.isSuccessful()&&r.body()!=null)mostrar("Entrega registrada #"+r.body().getId()+"\nArchivo: "+r.body().getNombreArchivo()+"\nFecha: "+r.body().getFechaEntrega()); else mostrar("Error entrega: "+r.code()); }
                @Override public void onFailure(Call<EntregaActividadResponse> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
            });
        }catch(Exception e){mostrar("No se pudo leer el archivo: "+e.getMessage());}
    }

    private void crearReclamo(){
        Long actividadId=valorLong(editActividadId,"actividad ID"); if(actividadId==null)return;
        String motivo=editMotivoReclamo.getText().toString().trim(); if(motivo.isEmpty()){Toast.makeText(this,"Escribe el motivo",Toast.LENGTH_SHORT).show();return;}
        api.crearReclamo(new ReclamoRequest(usuarioId,actividadId,motivo)).enqueue(new Callback<ReclamoResponse>(){
            @Override public void onResponse(Call<ReclamoResponse> c,Response<ReclamoResponse> r){if(r.isSuccessful()&&r.body()!=null)mostrar("Reclamo #"+r.body().getId()+" creado. Estado: "+r.body().getEstado());else mostrar("Error reclamo: "+r.code());}
            @Override public void onFailure(Call<ReclamoResponse> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void cargarReclamos(){
        api.obtenerReclamosEstudiante(usuarioId).enqueue(new Callback<List<ReclamoResponse>>(){
            @Override public void onResponse(Call<List<ReclamoResponse>> c,Response<List<ReclamoResponse>> r){if(!r.isSuccessful()||r.body()==null){mostrar("Error reclamos: "+r.code());return;}StringBuilder sb=new StringBuilder("RECLAMOS\n");for(ReclamoResponse x:r.body())sb.append("#").append(x.getId()).append(" | ").append(x.getEstado()).append("\n").append(x.getMotivo()).append("\nRespuesta: ").append(x.getRespuesta()).append("\n\n");mostrar(sb.toString());}
            @Override public void onFailure(Call<List<ReclamoResponse>> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void enviarForo(){
        Long cursoId=valorLong(editCursoId,"curso ID"); if(cursoId==null)return; String m=editMensajeForo.getText().toString().trim(); if(m.isEmpty()){Toast.makeText(this,"Escribe el mensaje",Toast.LENGTH_SHORT).show();return;}
        api.publicarMensajeForo(cursoId,new MensajeForoRequest(usuarioId,m)).enqueue(new Callback<MensajeForoResponse>(){
            @Override public void onResponse(Call<MensajeForoResponse> c,Response<MensajeForoResponse> r){if(r.isSuccessful()&&r.body()!=null){mostrar("Mensaje publicado por "+r.body().getUsuarioNombre());editMensajeForo.setText("");}else mostrar("Error foro: "+r.code());}
            @Override public void onFailure(Call<MensajeForoResponse> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void cargarForo(){
        Long cursoId=valorLong(editCursoId,"curso ID"); if(cursoId==null)return;
        api.obtenerForoCurso(cursoId).enqueue(new Callback<List<MensajeForoResponse>>(){
            @Override public void onResponse(Call<List<MensajeForoResponse>> c,Response<List<MensajeForoResponse>> r){if(!r.isSuccessful()||r.body()==null){mostrar("Error foro: "+r.code());return;}StringBuilder sb=new StringBuilder("FORO CURSO "+cursoId+"\n");for(MensajeForoResponse x:r.body())sb.append(x.getUsuarioNombre()).append(": ").append(x.getMensaje()).append("\n").append(x.getFechaCreacion()).append("\n\n");mostrar(sb.toString());}
            @Override public void onFailure(Call<List<MensajeForoResponse>> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void cargarReporte(){
        api.obtenerReporteResumen().enqueue(new Callback<ReporteResumenResponse>(){
            @Override public void onResponse(Call<ReporteResumenResponse> c,Response<ReporteResumenResponse> r){if(r.isSuccessful()&&r.body()!=null){ReporteResumenResponse x=r.body();mostrar("REPORTE GENERAL\nUsuarios: "+x.getUsuarios()+"\nCursos: "+x.getCursos()+"\nInscripciones: "+x.getInscripciones()+"\nActividades: "+x.getActividades()+"\nCertificados: "+x.getCertificados());}else mostrar("Error reporte: "+r.code());}
            @Override public void onFailure(Call<ReporteResumenResponse> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void cargarRecursos(){
        api.obtenerRecursosNexa().enqueue(new Callback<List<RecursoNexaResponse>>(){
            @Override public void onResponse(Call<List<RecursoNexaResponse>> c,Response<List<RecursoNexaResponse>> r){if(!r.isSuccessful()||r.body()==null){mostrar("Error recursos: "+r.code());return;}StringBuilder sb=new StringBuilder("RECURSOS\n");for(RecursoNexaResponse x:r.body())sb.append("#").append(x.getId()).append(" | ").append(x.getTipo()).append(" | ").append(x.getTitulo()).append("\n").append(x.getUrl()).append("\n\n");mostrar(sb.toString());if(!r.body().isEmpty()){RecursoNexaResponse x=r.body().get(0);try{startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(x.getUrl())));}catch(Exception ignored){}}}
            @Override public void onFailure(Call<List<RecursoNexaResponse>> c,Throwable t){mostrar("Sin conexión: "+t.getMessage());}
        });
    }

    private void mostrar(String texto){ textResultado.setText(texto); }
}
