package com.example.pk2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pk2.model.HabitacionElementoList;
import com.example.pk2.model.MotelElementoList;

import java.util.List;

public class adaptadorHabi extends RecyclerView.Adapter<adaptadorHabi.ViewHolder>{
    private List<HabitacionElementoList> datos;
    private LayoutInflater inflater;
    private Context context;
    final adaptadorHabi.OnItemClickListener listener;

    public  interface  OnItemClickListener{
        void onItemClick(HabitacionElementoList elementos);
    }



    public adaptadorHabi(List<HabitacionElementoList> lista, Context context, adaptadorHabi.OnItemClickListener listener ){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = lista;
        this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @NonNull
    @Override
    public adaptadorHabi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cards_hab, null);
        return new adaptadorHabi.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorHabi.ViewHolder holder, int position) {
        holder.bindData(datos.get(position));
    }

    public void setItems(List<HabitacionElementoList> items)
    {
        datos = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre, descripcion;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.imagenHab);
            nombre = itemView.findViewById(R.id.nombreHab);
            descripcion = itemView.findViewById(R.id.descripcionHab);
        }

        void bindData(final HabitacionElementoList lista){
            String urlImage = lista.getImagen1();
            Glide.with(context)
                    .load(urlImage)
                    .into(iconImage);
            nombre.setText(lista.getNombre());
            descripcion.setText(lista.getDescripcion());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(lista);
                }
            });

        }


    }
}
