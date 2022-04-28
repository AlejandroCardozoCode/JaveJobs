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
import com.example.pk2.model.ChatElementoList;

import java.util.List;

public class AdaptadorListaChatsDueno extends RecyclerView.Adapter<AdaptadorListaChatsDueno.ViewHolder>
{
    private List<ChatElementoList> datos;
    private LayoutInflater inflater;
    private Context context;
    final AdaptadorListaChatsDueno.OnItemClickListener listener;
    public  interface  OnItemClickListener{
        void onItemClick(ChatElementoList elementos);
    }
    public AdaptadorListaChatsDueno(List<ChatElementoList> datos, Context context, AdaptadorListaChatsDueno.OnItemClickListener listener) {
        this.datos = datos;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return datos.size();
    }

    @NonNull
    @Override
    public AdaptadorListaChatsDueno.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.activity_chat_listchats, null);
        return new AdaptadorListaChatsDueno.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaChatsDueno.ViewHolder holder, int position) {
        holder.bindData(datos.get(position));
    }
    public void setItems(List<ChatElementoList> items)
    {
        datos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView NombreApellido;

        ViewHolder(View itemView){
            super(itemView);
            NombreApellido = itemView.findViewById(R.id.name_chat_d);
        }

        void bindData(final ChatElementoList lista){
            String nameLastName = lista.getName_Reciever() + " " + lista.getLastName_Reciever();
            NombreApellido.setText(nameLastName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(lista);
                }
            });
        }


    }


}
