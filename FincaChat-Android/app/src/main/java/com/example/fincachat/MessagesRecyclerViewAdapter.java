package com.example.fincachat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fincachat.Models.Message;
import com.example.fincachat.Models.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.myViewHolder> {
    List<Message> lstMessages;
    MessagesRecyclerViewAdapter(ArrayList<Message> lstMessages){
        this.lstMessages=lstMessages;
    }

    public MessagesRecyclerViewAdapter() {
        lstMessages=new ArrayList<>();
        lstMessages.add(new Message(1, "Hello! How are you?", "12-12-2018","Muhammad Umair Tahir"));
        lstMessages.add(new Message(2, "I am Fine", "12-12-2018","Aqeel Ahmad"));
    }

    public MessagesRecyclerViewAdapter(List<Message> lstMessages) {
        this.lstMessages = lstMessages;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_message_row,parent,false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Message message=lstMessages.get(position);
        holder.txtDateTime.setText(message.getDateTime().toString());
        holder.txtMessage.setText(message.getMessage());
        holder.txtSenderName.setText(message.getUser());
    }

    @Override
    public int getItemCount() {
        return lstMessages.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtSenderName;
        public final TextView txtMessage;
        public final TextView txtDateTime;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSenderName=itemView.findViewById(R.id.txtSenderName);
            txtMessage=itemView.findViewById(R.id.txtMessage);
            txtDateTime=itemView.findViewById(R.id.txtDateTime);
        }
    }

}
