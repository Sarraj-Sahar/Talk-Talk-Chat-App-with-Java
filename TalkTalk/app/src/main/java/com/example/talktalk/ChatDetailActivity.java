package com.example.talktalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.talktalk.Adapter.ChatAdapter;
import com.example.talktalk.Models.MessageModel;
import com.example.talktalk.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //Getting Chatter's data
        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra( "userId");
        String userName = getIntent().getStringExtra( "userName");
        String profilePic = getIntent().getStringExtra(  "profilePic");

        //Setting Chatter's name & pic
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder (R.drawable.avatar).into(binding.profileImage);

        //Go back Arrow when clicked
      binding.backArrow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent( ChatDetailActivity.this, MainActivity.class);
              startActivity(intent);
          }
      });


      //II.Sending & Recieving Messages
        // Create two vars
        final ArrayList<MessageModel> messageModels= new ArrayList<>();
        final ChatAdapter chatAdapter= new ChatAdapter(messageModels,  this, recieveId);

        //Binding our Views using chatAdapter to know if it's receiver or sender view
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(  this);
        //Bind the layoutManager with the recyclerView
        //A RecyclerView.LayoutManager : implementation which provides similar
        // functionality to android.widget.ListView.
        binding.chatRecyclerView.setLayoutManager (layoutManager);


        //create two vars : for senderRoom & RecieverRoom :
        // we created two unique ids as a sum of other ids
        //Used to identify which user is the sender and which is the reciever
        final String senderRoom =senderId + recieveId;
        final String receiverRoom = recieveId + senderId;


        //Sending the msg when we click on the send button
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Store the entered msg
                String message = binding.enterMessage.getText().toString();

                //create a msgModel with the stored msg
                final MessageModel model = new MessageModel(senderId, message);

                //set the timeStamp
                model.setTimestamp(new Date().getTime());

                //Clear the EditText
                binding.enterMessage.setText("");

                //1.if a sender sends a message
                database.getReference ().child("chats")
                        .child(senderRoom)
                        .push()         //2. it needs to be stored for the sender
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // 3 .That same message needs to be stored at the reciever's end also
                        database.getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                    }
                });


            }
        });

        //////////////////
        //Fetching the messages from our firebase and displaying them !!
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //on changed , clear the messageModel
                        messageModels.clear();
                        //A DataSnapshot is an immutable copy of the data at a Database location (firebase)
                        // It cannot be modified,(to modify data, you always call the set() method on a Reference directly)
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            //creating a message Model and giving it the val of the snapshot
                            //and setting it's id as the snapshot key
                            MessageModel model = snapshot1.getValue (MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model); //adding the model to the list of messages of this chat
                        }
                            //After that , we need to inform our ChatAdapter that the data is changed
                            chatAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}