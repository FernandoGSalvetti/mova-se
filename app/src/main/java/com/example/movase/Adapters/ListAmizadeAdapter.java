package com.example.movase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movase.Models.AmizadeViewModel;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListAmizadeAdapter extends ArrayAdapter<AmizadeViewModel> {

    private Context context;
    private List<AmizadeViewModel> usuarios = null;

    public ListAmizadeAdapter(Context context,  List<AmizadeViewModel> usuarios) {
        super(context, 0, usuarios);
        this.usuarios = usuarios;

        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        AmizadeViewModel usuario = usuarios.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_list_amizades, null);
        ImageView eventoImage = (ImageView) view.findViewById(R.id.item_list_amizade_img);
        if(usuario.getFotoPerfil().isEmpty() == false){
            Picasso.with(getContext()).load(usuario.getFotoPerfil()).transform(new CropCircleTransformation()).into(eventoImage);
        }
        TextView nomeUsuario = (TextView) view.findViewById(R.id.item_list_amizade_nome);
        nomeUsuario.setText(usuario.getNomeCompleto());

        TextView apelido = (TextView) view.findViewById(R.id.item_list_amizade_apelido);
        apelido.setText(usuario.getApelido());

        TextView modalidade = (TextView) view.findViewById(R.id.item_list_amizade_modalidade);
        modalidade.setText(usuario.getEsportePreferido());
        ImageView amizadeStatus = (ImageView) view.findViewById(R.id.item_list_amizade_status_amizade);
        if(usuario.isAmigo()){
            amizadeStatus.setImageResource(R.drawable.ic_baseline_connect_without_contact_24);
        }
        return view;
    }
}
