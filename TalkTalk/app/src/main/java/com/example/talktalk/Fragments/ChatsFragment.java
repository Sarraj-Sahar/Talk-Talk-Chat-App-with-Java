package com.example.talktalk.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talktalk.Adapter.UsersAdapter;
import com.example.talktalk.Models.Users;
import com.example.talktalk.R;
import com.example.talktalk.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    // Required empty public constructor
    public ChatsFragment() {

    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container,  false);
        database = FirebaseDatabase.getInstance();
        UsersAdapter adapter = new UsersAdapter(list,getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        //Adding a LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager (getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        //Getting Users' Ids from our firebase RealTime DataBase
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //On dataChange
                //1.We should First Clear the List
                list.clear();

                //Displaying Users :

                //2.Get user id and add them to list
                //Will be displayed in Chats Tab
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue (Users.class);
                    users.setUserId(dataSnapshot.getKey());

                    //a user shouldn't appear in his own Chat's list
                    //Adding all users except current user to the list
                    if(!users.getUserId().equals (FirebaseAuth.getInstance ().getUid())){
                        list.add(users);}
                }
                adapter.notifyDataSetChanged ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

   return binding.getRoot();
    }

}