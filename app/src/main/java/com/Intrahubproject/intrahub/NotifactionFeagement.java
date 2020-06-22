package com.Intrahubproject.intrahub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifactionFeagement extends Fragment {

   private RecyclerView notifactionview;
   private DatabaseReference MmessageDatabase;
   private RecyclerView recyclerView;
   private RelativeLayout aleartlayout;


    public NotifactionFeagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifaction_feagement, container, false);

        aleartlayout = view.findViewById(R.id.AleartLayoutID);
        recyclerView = view.findViewById(R.id.NotifactionViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MmessageDatabase = FirebaseDatabase.getInstance().getReference().child("AdminMessage");
        MmessageDatabase.keepSynced(true);

        notifactionview = view.findViewById(R.id.NotifactionViewID);
        notifactionview.setHasFixedSize(true);
        notifactionview.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }


    @Override
    public void onStart() {



        FirebaseRecyclerAdapter<AdminMessagePogo, NotifactionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminMessagePogo, NotifactionViewHolder>(
                AdminMessagePogo.class,
                R.layout.notifaction_update_layout,
                NotifactionViewHolder.class,
                MmessageDatabase
        ) {
            @Override
            protected void populateViewHolder(final NotifactionViewHolder notifactionViewHolder, final AdminMessagePogo adminMessagePogo, int i) {

                String UID = getRef(i).getKey();
                MmessageDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    aleartlayout.setVisibility(View.INVISIBLE);
                                    if(dataSnapshot.hasChild("AdminMessage")){
                                        String AdminMessageget = dataSnapshot.child("AdminMessage").getValue().toString();
                                        notifactionViewHolder.setMessageset(AdminMessageget);
                                    }
                                    if(dataSnapshot.hasChild("current_time")){
                                        String current_timeget = dataSnapshot.child("current_time").getValue().toString();
                                        notifactionViewHolder.setimeset(current_timeget);
                                    }
                                    if(dataSnapshot.hasChild("current_date")){
                                        String current_dateget = dataSnapshot.child("current_date").getValue().toString();
                                        notifactionViewHolder.setDateset(current_dateget);
                                    }
                                }
                                else {

                                    aleartlayout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

   public static class  NotifactionViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView time, date, message;
        private Context context;
        private View Mview;

        public NotifactionViewHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            time = Mview.findViewById(R.id.NotifactionTimerID);
            date = Mview.findViewById(R.id.NotifactionDateID);
            message = Mview.findViewById(R.id.MessagePostTextID);
        }

        public void  setimeset(String tim){
            time.setText(tim);
        }
        public void setDateset(String dat){
            date.setText(dat);
        }
        public void setMessageset(String mess){
            message.setText(mess);
        }
    }
}
