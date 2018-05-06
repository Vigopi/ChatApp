package com.example.vinothgopigraj.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText editMessage;
    private DatabaseReference databaseReference;
    private RecyclerView mMessageList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editMessage = (EditText) findViewById(R.id.editMsg);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Messages");
        mMessageList = (RecyclerView) findViewById(R.id.messageRec);
        mMessageList.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            }
        };
    }

    public void sendButtonClicked(View view) {
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        final String messageValue = editMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(messageValue)){
            final DatabaseReference newPost = databaseReference.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("content").setValue(messageValue);
                    newPost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                    Date date = new Date();
                    //System.out.println(date);
                    newPost.child("time").setValue(formatter.format(date).toString());
                    mMessageList.smoothScrollToPosition(mMessageList.getAdapter().getItemCount());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mMessageList.smoothScrollToPosition(mMessageList.getAdapter().getItemCount());
            editMessage.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT);
        FirebaseRecyclerAdapter <Message,MessageViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,R.layout.singlemessagelayout,
                MessageViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());
                mMessageList.smoothScrollToPosition(position);
            }
        };
        mMessageList.setAdapter(FBRA);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setContent(String content){
            TextView messageContent = (TextView) mView.findViewById(R.id.messageText);
            messageContent.setText(content);
        }
        public void setUsername(String username){
            TextView username_content = (TextView) mView.findViewById(R.id.usernameText);
            username_content.setText(username);
        }
        public void setTime(String time){
            TextView time_content = (TextView) mView.findViewById(R.id.timeText);
            time_content.setText(time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu)
        {
            logout();
        }
        return true;
    }

    public void logout(){
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),"Logout successfull",Toast.LENGTH_LONG);
        startActivity(new Intent(MainActivity.this,LoginActivity.class));

    }
}
