package com.demos.vedioplaybygsyplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<String> list = new ArrayList<>();
        list.add("单屏");
        list.add("多屏");
        list.add("gsy-详情");

        RecyclerView recyclerView = findViewById(R.id.rv_urls);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.simple_item, parent, false);
                return new SimpleViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                ((SimpleViewHolder) holder).textView.setText(list.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipTo(position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });
    }

    private void skipTo(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, DetailPlayerActivity.class);
                break;
            case 1:
                intent = new Intent(this, ListDetailPlayerActivity.class);
                break;
            case 2:
            default:
                intent = new Intent(this, PlayerActivity.class);
                break;
        }
        startActivity(intent);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_simple);
        }
    }

}
