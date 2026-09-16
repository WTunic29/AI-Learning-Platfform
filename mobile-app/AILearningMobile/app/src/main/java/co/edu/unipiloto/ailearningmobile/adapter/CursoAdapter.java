package co.edu.unipiloto.ailearningmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.R;
import co.edu.unipiloto.ailearningmobile.dto.CursoResponse;

/*
* onCreateViewHolder() crea visualmente cada tarjeta usando item_curso.xml.
* onBindViewHolder() coloca los datos del curso en los TextView.
* getItemCount() indica cuántos cursos deben mostrarse.
* CursoViewHolder conecta los componentes XML con Java.
*/

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private final List<CursoResponse> listaCursos;

    public CursoAdapter(List<CursoResponse> listaCursos) {
        this.listaCursos = listaCursos;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso, parent, false);

        return new CursoViewHolder(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {

        CursoResponse curso = listaCursos.get(position);

        holder.tvNombreCurso.setText(curso.getNombre());
        holder.tvCategoriaCurso.setText(curso.getCategoria());
        holder.tvDescripcionCurso.setText(curso.getDescripcion());

    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public static class CursoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreCurso;
        TextView tvCategoriaCurso;
        TextView tvDescripcionCurso;

        public CursoViewHolder(@NonNull View itemView) {

            super(itemView);

            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvCategoriaCurso = itemView.findViewById(R.id.tvCategoriaCurso);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCurso);

        }

    }

}