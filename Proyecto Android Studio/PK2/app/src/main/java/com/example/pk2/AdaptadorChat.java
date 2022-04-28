package com.example.pk2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pk2.model.Mensaje_Texto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AdaptadorChat extends RecyclerView.Adapter
{
    private static final int M_VISTA_TIPO_ENVIADO = 1;
    private static final int M_VISTA_TIPO_RECIBIDO = 2;

    private Context c;
    private List<Mensaje_Texto> m_messageList;
    static final String PATH_USERS = "users/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    public AdaptadorChat(Context context, List<Mensaje_Texto> messageList) {
        c = context;
        m_messageList = messageList;

    }
    @Override
    public int getItemCount() {
        return m_messageList.size();
    }
    //Determina que tipo de "ViewType" es dependiendo del usuario que envio el mensaje.
    @Override
    public int getItemViewType(int position)
    {
        mAuth = FirebaseAuth.getInstance();
        Mensaje_Texto message =  m_messageList.get(position);
        FirebaseUser currUser = mAuth.getCurrentUser();

        if (message.getDueno().equals(currUser.getUid())) {
            // If the current user is the sender of the message
            return M_VISTA_TIPO_ENVIADO;
        } else {
            // If some other user sent the message
            return M_VISTA_TIPO_RECIBIDO;
        }
    }
    void addMessage(Mensaje_Texto message)
    {
        m_messageList.add(message);
        notifyDataSetChanged();
    }
    void setUpdatedList(List<Mensaje_Texto> newList)
    {
        this.m_messageList.clear();
        this.m_messageList = newList;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == M_VISTA_TIPO_ENVIADO) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_chat_s_text_message, parent, false);
            return new EnviadorMensajeHolder(view);
        } else if (viewType == M_VISTA_TIPO_RECIBIDO) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_chat_r_text_message, parent, false);
            return new RecibidorMensajeHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Mensaje_Texto message = m_messageList.get(position);

        switch (holder.getItemViewType()) {
            case M_VISTA_TIPO_ENVIADO:
                ((EnviadorMensajeHolder) holder).bind(message);
                break;
            case M_VISTA_TIPO_RECIBIDO:
                ((RecibidorMensajeHolder) holder).bind(message);
        }
    }

    private class RecibidorMensajeHolder extends RecyclerView.ViewHolder
    {
        TextView messageText, timeText, nameText, dateText;
        ImageView profileImage;
        RecibidorMensajeHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.name_chat_d);
            timeText = (TextView) itemView.findViewById(R.id.text_message_hour_r);
            nameText = (TextView) itemView.findViewById(R.id.text_uname_r);
            profileImage = (ImageView) itemView.findViewById(R.id.image_profile_r);
            dateText = (TextView) itemView.findViewById(R.id.text_date_r);
        }
        void bind(Mensaje_Texto message) {
            messageText.setText(message.getContenidoMensaje());

            long newTime = message.getCreatedAt();
            newTime = newTime + TimeZone.getTimeZone("America/Bogota").getRawOffset();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
            calendar.setTimeInMillis(newTime);

            int mHour = calendar.get(Calendar.HOUR);
            int mMinute = calendar.get(Calendar.MINUTE);
            int mSecond = calendar.get(Calendar.SECOND);
            String mHourS, mMinuteS, mSecondsS;
            if (mHour < 9)
            {
                mHourS = "0"+String.valueOf(mHour);
            }
            else
            {
                mHourS = String.valueOf(mHour);
            }
            if (mMinute < 9)
            {
                mMinuteS = "0"+String.valueOf(mMinute);
            }
            else
            {
                mMinuteS = String.valueOf(mMinute);
            }
            if (mSecond < 9)
            {
                mSecondsS = "0"+String.valueOf(mSecond);
            }
            else
            {
                mSecondsS = String.valueOf(mSecond);
            }
            timeText.setText(mHourS + ":" + mMinuteS + ":" +mSecondsS);
            String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)-1];
            dateText.setText(month + " " + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            nameText.setText(message.getNombreDueno());
        }
    }
    private class EnviadorMensajeHolder extends RecyclerView.ViewHolder
    {
        TextView messageText, timeText, dateText;
        EnviadorMensajeHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message);
            timeText = (TextView) itemView.findViewById(R.id.text_time);
            dateText = (TextView) itemView.findViewById(R.id.textView2);
        }
        void bind(Mensaje_Texto message) {

            messageText.setText(message.getContenidoMensaje());

            //Transformar la hora guardada (En Milisegundos ) a una hora formateada.

            long newTime = message.getCreatedAt();
            newTime = newTime + TimeZone.getTimeZone("America/Bogota").getRawOffset();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
            calendar.setTimeInMillis(newTime);

            int mHour = calendar.get(Calendar.HOUR);
            int mMinute = calendar.get(Calendar.MINUTE);
            int mSecond = calendar.get(Calendar.SECOND);
            String mHourS, mMinuteS, mSecondsS;
            if (mHour < 9)
            {
                mHourS = "0"+String.valueOf(mHour);
            }
            else
            {
                 mHourS = String.valueOf(mHour);
            }
            if (mMinute < 9)
            {
                 mMinuteS = "0"+String.valueOf(mMinute);
            }
            else
            {
                 mMinuteS = String.valueOf(mMinute);
            }
            if (mSecond < 9)
            {
                 mSecondsS = "0"+String.valueOf(mSecond);
            }
            else
            {
                 mSecondsS = String.valueOf(mSecond);
            }
            timeText.setText(mHourS + ":" + mMinuteS + ":" +mSecondsS);
            String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)-1];
            dateText.setText(month + " " + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
    }

}
