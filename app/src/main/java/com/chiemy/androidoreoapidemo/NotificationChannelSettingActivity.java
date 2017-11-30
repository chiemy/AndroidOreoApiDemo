package com.chiemy.androidoreoapidemo;

import android.app.NotificationChannel;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

@RequiresApi(Build.VERSION_CODES.O)
public class NotificationChannelSettingActivity extends AppCompatActivity implements View
        .OnClickListener {
    private static final String CHANNEL_ID = "channel_id";

    public static void start(Context context, String channelId) {
        Intent intent = new Intent(context, NotificationChannelSettingActivity.class);
        intent.putExtra(CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    private String channelId;
    private TextView textView;
    private View settingView;
    private View playSound;

    private NotificationChannel notificationChannel;

    MediaPlayer mediaPlayer;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_channel_setting);
        channelId = getIntent().getStringExtra(CHANNEL_ID);

        textView = findViewById(R.id.textView);
        settingView = findViewById(R.id.button5);
        settingView.setOnClickListener(this);

        playSound = findViewById(R.id.button6);
        playSound.setOnClickListener(this);

        findViewById(R.id.button7).setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button5:
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                break;

            case R.id.button6:
                if (notificationChannel != null) {
                    Uri uri = notificationChannel.getSound();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(this, uri);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.button7:
                if (notificationChannel != null
                        && notificationChannel.shouldVibrate()) {
                    long[] vibrationPattern = notificationChannel.getVibrationPattern();
                    if (vibrationPattern != null) {
                        VibrationEffect effect = VibrationEffect.createWaveform(vibrationPattern, -1);
                        vibrator.vibrate(effect);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationChannel = getNotificationChannel(channelId);
        String settings = null;
        if (notificationChannel != null) {
            settings = getChannelSettingsString(notificationChannel);
        }
        if (settings == null) {
            textView.setText("无法获取");
            settingView.setVisibility(View.GONE);
            playSound.setVisibility(View.GONE);
        } else {
            textView.setText(settings);
        }
    }

    private String getChannelSettingsString(NotificationChannel channel) {
        String settings = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            StringBuilder stringBuilder = new StringBuilder();
            if (channel != null) {
                stringBuilder
                        .append(format("Name", channel.getName()))
                        .append(format("\nID", channel.getId()))
                        .append(format("\nDescription", channel.getDescription()))
                        .append(format("\nImportance", NotificationUtil.convertImportance(channel.getImportance())))
                        .append(format("\ncanShowBadge", channel.canShowBadge()))
                        .append(format("\nshouldShowLights", channel.shouldShowLights()))
                        .append(format("\nshouldVibrate", channel.shouldVibrate()))
                        .append(format("\ncanBypassDnd", channel.canBypassDnd()))
                        .append(format("\nLock screen visibility", NotificationUtil.convertVisibility(channel.getLockscreenVisibility())));

            }
            settings = stringBuilder.toString();
        }
        return settings;
    }

    private NotificationChannel getNotificationChannel(String channelId) {
        return NotificationUtil.getNotificationChannel(this, channelId);
    }

    private String format(String key, Object value) {
        return String.format("%s : %s", key, value.toString());
    }

}
