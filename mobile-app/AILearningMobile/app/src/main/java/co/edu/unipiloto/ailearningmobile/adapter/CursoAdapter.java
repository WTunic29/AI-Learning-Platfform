package co.edu.unipiloto.ailearningmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.R;
import co.edu.unipiloto.ailearningmobile.dto.CursoResponse;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private final List<CursoResponse> listaCursos;
    private final OnInscripcionClickListener inscripcionListener;
    private final OnContenidoClickListener contenidoListener;

    public interface OnInscripcionClickListener {
        void onInscripcionClick(CursoResponse curso);
    }

    public interface OnContenidoClickListener {
        void onContenidoClick(CursoResponse curso);
    }

    public CursoAdapter(
            List<CursoResponse> listaCursos,
            OnInscripcionClickListener inscripcionListener,
            OnContenidoClickListener contenidoListener) {

        this.listaCursos = listaCursos;
        this.inscripcionListener = inscripcionListener;
        this.contenidoListener = contenidoListener;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso, parent, false);

        return new CursoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CursoViewHolder holder,
            int position) {

        CursoResponse curso = listaCursos.get(position);

        holder.tvNombreCurso.setText(curso.getNombre());
        holder.tvCategoriaCurso.setText(curso.getCategoria());
        holder.tvDescripcionCurso.setText(curso.getDescripcion());

        holder.btnInscribirse.setOnClickListener(
                v -> inscripcionListener.onInscripcionClick(curso)
        );

        holder.btnVerContenido.setOnClickListener(
                v -> contenidoListener.onContenidoClick(curso)
        );
    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public static class CursoViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNombreCurso;
        TextView tvCategoriaCurso;
        TextView tvDescripcionCurso;

        Button btnInscribirse;
        Button btnVerContenido;

        public CursoViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvNombreCurso =
                    itemView.findViewById(R.id.tvNombreCurso);

            tvCategoriaCurso =
                    itemView.findViewById(R.id.tvCategoriaCurso);

            tvDescripcionCurso =
                    itemView.findViewById(R.id.tvDescripcionCurso);

            btnInscribirse =
                    itemView.findViewById(R.id.btnInscribirse);

            btnVerContenido =
                    itemView.findViewById(R.id.btnVerContenido);
        }
    }
}