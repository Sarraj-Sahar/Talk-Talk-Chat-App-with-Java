package com.example.talktalk.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talktalk.Models.MessageModel;
import com.example.talktalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    //Our Vars
    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;


    //Vars for getItemViewType Method
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    //Making a constrcutor with recieverId
    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    // GO TO 1.CREATING VIEWS FIRST

    // 3. Creating our view , wether it's senderView or Recieverview...
    // we have binded views for two different types of messgaes
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE)
             {
            View view= LayoutInflater.from (context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
             }
        else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
                return new RecieverViewHolder(view);
            }
    }


    // 2. We need to get the id's of which is the senderMsg
    // and which is the recieverMsg
    //we will override our onBindViewHolder method to get it
    @Override
    public int getItemViewType(int position) {

        //2.checking which is the reciever and which is the sender
        //we extract the id of the user that is logged in
        //and check whether he is the sender or not...
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance ().getUid()))
            return SENDER_VIEW_TYPE;
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }


    //4.We use this onBindViewHolder to know which "text format" (senderText or recieverText)
    // we will use on the message in question
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModels.get(position);

        // 2. Adding the delete option
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom= FirebaseAuth.getInstance ().getUid() + recId ;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);

                            }
                        }).show();
                return false;
            }
        });

        // end delete feature

        //1.
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMessage());
        } else {
            ((RecieverViewHolder) holder).receiverMsg.setText(messageModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    //1.Extends a recyclerView to make the diffrence between the sender and reciever views


    //A. Class for Reciever view
    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;

        //constructor
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            //creating our variables
            receiverMsg = itemView.findViewById(R.id.receiverText); //this is in sample_reciever.xml
            receiverTime = itemView.findViewById (R.id.receiverTime);

        }
    }


    //B. Class for Sender view
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            //creating our variables
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById (R.id.senderTime);
        }
    }

}
