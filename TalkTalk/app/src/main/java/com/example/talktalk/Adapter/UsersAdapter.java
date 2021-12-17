package com.example.talktalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talktalk.ChatDetailActivity;
import com.example.talktalk.Models.Users;
import com.example.talktalk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    //Making a users List
    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creating a View
        //the inflater takes the xml file : sample_user.xml --> and turns it into a view
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }


    //Getting username, profilepic from firebase (using Picasso)
    //Fetching the data --> to set them inside our View (viewHolder)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = list.get(position);
        Picasso.get().load (users.getProfilePic ()).placeholder (R.drawable.avatar3).into(holder.image);
        holder.userName.setText(users.getUserName());

        //When item/chat is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatDetailActivity.class);

                //get user id and show Chat_Detailed according to it
                intent.putExtra(  "userId", users.getUserId());  // the getters and setters are from Users Model
                intent.putExtra(  "profilePic",users.getProfilePic());
                intent.putExtra( "userName", users.getUserName());

                context.startActivity(intent);
            }
        });
    }


    //Returns Numbers of items in our List
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image ;
        TextView userName,lastMessage;

        //Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //R gives you the root element of a view, without having
            // to know its actual path :name/type/ID
            image = itemView.findViewById (R.id.profile_image); //profile_image from sample_user.xml
            userName = itemView.findViewById(R.id.userNameList);
            lastMessage = itemView.findViewById (R.id.lastMessage);
        }
    }
}
