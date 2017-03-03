package com.demo.berosy;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.berosy.R;
import com.demo.ezrecyclerview.EzRecyclerView;

public class MainActivity extends AppCompatActivity implements EzRecyclerView.OnLoadingListener {
    EzRecyclerView recyclerView;
    Adapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (EzRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManage = new LinearLayoutManager(this);
        linearLayoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManage);
        recyclerView.setOnLoadingListener(this);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onLoading() {
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setCount(adapter.getItemCount() + 10);
                adapter.notifyDataSetChanged();
                recyclerView.stopLoading();
            }
        }, 3000);
    }

    @Override
    public void onStopLoading() {
        progressDialog.dismiss();
    }

    class Adapter extends RecyclerView.Adapter {
        int count = 10;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder) holder).textView.setText(String.valueOf(position));
        }

        void setCount(int count) {
            this.count = count;
        }

        @Override
        public int getItemCount() {
            return count;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView textView;

            Holder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textview_name);
            }
        }
    }
}
