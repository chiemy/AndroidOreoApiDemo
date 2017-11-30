package com.chiemy.androidoreoapidemo;

import android.app.NotificationChannel;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if (NotificationUtil.supportNotificationChannel()) {
            findViewById(R.id.btn_send).setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ChannelAdapter(
                    NotificationUtil.getNotificationManager(this).getNotificationChannels()
            ));
        }
    }

    public void onClickSend(View v) {
        NotificationUtil.sendTestNotification(v.getContext(), id++, null);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static class ChannelAdapter extends RecyclerView.Adapter<ChannelHolder> {
        private List<NotificationChannel> channels;
        ChannelAdapter(List<NotificationChannel> channels) {
            this.channels = channels;
        }

        @Override
        public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ChannelHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(ChannelHolder holder, int position) {
            holder.bind(channels.get(position));
        }

        @Override
        public int getItemCount() {
            return channels != null ? channels.size() : 0;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static class ChannelHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        public static ChannelHolder create(ViewGroup parent) {
            return new ChannelHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_channel, parent, false)
            );
        }

        private TextView nameTv;
        private NotificationChannel channel;

        ChannelHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_channel_name);
            itemView.findViewById(R.id.btn_send).setOnClickListener(this);
            itemView.findViewById(R.id.btn_setting).setOnClickListener(this);
        }

        void bind(NotificationChannel channel) {
            this.channel = channel;
            nameTv.setText(channel.getName());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:
                    NotificationUtil.sendTestNotification(v.getContext(), id++, channel.getId());
                    break;

                case R.id.btn_setting:
                    NotificationChannelSettingActivity.start(v.getContext(), channel.getId());
                    break;
                default:
            }
        }

    }

}
