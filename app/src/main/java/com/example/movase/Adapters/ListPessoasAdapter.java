package com.example.movase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListPessoasAdapter extends ArrayAdapter<UsuarioViewModel> {

    private Context context;
    private List<UsuarioViewModel> usuarios = null;

    public ListPessoasAdapter(Context context,  List<UsuarioViewModel> usuarios) {
        super(context, 0, usuarios);
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        UsuarioViewModel usuario = usuarios.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_list_pessoas, null);
        ImageView eventoImage = (ImageView) view.findViewById(R.id.item_list_pessoa_img);
        if(usuario.getFotoPerfil().isEmpty() == false){
            Picasso.with(getContext()).load(usuario.getFotoPerfil()).transform(new CropCircleTransformation()).into(eventoImage);
        }
        TextView nomeUsuario = (TextView) view.findViewById(R.id.item_list_pessoa_nome);
        nomeUsuario.setText(usuario.getNomeCompleto());

        TextView apelido = (TextView) view.findViewById(R.id.item_list_pessoa_apelido);
        apelido.setText(usuario.getApelido());

        TextView modalidade = (TextView) view.findViewById(R.id.item_list_pessoa_modalidade);
        modalidade.setText(usuario.getEsportePreferido());

        return view;
    }
}
