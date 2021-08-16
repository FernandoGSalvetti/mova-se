package com.example.movase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.movase.Models.EventoViewModel;
import com.example.movase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListEventosAdapter extends ArrayAdapter<EventoViewModel> {

    private Context context;
    private List<EventoViewModel> eventos = null;
    private boolean fromPesquisaFrag;

    public ListEventosAdapter(Context context,  List<EventoViewModel> eventos, boolean fromPesquisaFrag) {
        super(context, 0, eventos);
        this.eventos = eventos;
        this.context = context;
        this.fromPesquisaFrag = fromPesquisaFrag;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EventoViewModel evento = eventos.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_list_eventos, null);

        ImageView eventoImage = (ImageView) view.findViewById(R.id.item_list_eventos_img);
        switch (evento.getModalidade()){
            case "Basquete":
                eventoImage.setImageResource(R.drawable.ic_outline_sports_basketball_24);
                break;
            case "Futebol":
                eventoImage.setImageResource(R.drawable.ic_baseline_sports_soccer_24);
                break;
            case "Fut7":
                eventoImage.setImageResource(R.drawable.ic_baseline_sports_soccer_24);
                break;
            case "Futsal":
                eventoImage.setImageResource(R.drawable.ic_baseline_sports_soccer_24);
                break;
            case "Corrida":
                eventoImage.setImageResource(R.drawable.ic_baseline_directions_run_24);
                break;
            case "Caminhada":
                eventoImage.setImageResource(R.drawable.ic_baseline_directions_walk_24);
                break;
            case "Calistenia":
                eventoImage.setImageResource(R.drawable.ic_workout__1_);
                break;
            case "Vôlei":
                eventoImage.setImageResource(R.drawable.ic_outline_sports_volleyball_24);
                break;
            case "Musculação":
                eventoImage.setImageResource(R.drawable.ic_dumbbell__3_);
                break;
            default:
                eventoImage.setImageResource(R.drawable.ic_dumbbell__3_);
                break;
        }
        if(fromPesquisaFrag){
            TextView nomeDono = (TextView) view.findViewById(R.id.item_list_eventos_dono_evento);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios").document(evento.getIdCriadorDoEvento()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    nomeDono.setVisibility(View.VISIBLE);
                    nomeDono.setText("Dono do evento: " + task.getResult().get("nomeCompleto").toString());
                }
            });
        }
        TextView nomeEvento = (TextView) view.findViewById(R.id.item_list_eventos_nome);
        nomeEvento.setText(evento.getNome());
        TextView dataEvento = (TextView) view.findViewById(R.id.item_list_eventos_data_hora);
        dataEvento.setText(evento.getData() + " " + evento.getHorario());
        TextView modalidade = (TextView) view.findViewById(R.id.item_list_eventos_modalidade);
        modalidade.setText(evento.getModalidade());
        TextView privacidade = (TextView) view.findViewById(R.id.item_list_eventos_privacidade);
        privacidade.setText(evento.getTipoDeEvento());
        TextView endereco = (TextView) view.findViewById(R.id.item_list_eventos_endereco);
        endereco.setText(evento.getEndereco());
        return view;
    }
}