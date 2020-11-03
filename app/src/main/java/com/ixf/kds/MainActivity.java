package com.ixf.kds;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ixf.kds.utils.MLog;
import com.ixf.kds.utils.NumberFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button plusButton, lessButton, startButton, passButton;
    private EditText kdsNumberEditText;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AssetManager assetManager;
    private AudioAttributes audioAttributes;
    private AudioFocusRequest audioFocusRequest;
    private int kdsNumber;
    private Map<String, AssetFileDescriptor> audioMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                init();
            }
        }
        ).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        plusButton = findViewById(R.id.kds_plus_button);
        lessButton = findViewById(R.id.kds_less_button);
        startButton = findViewById(R.id.kds_start_button);
        passButton = findViewById(R.id.kds_pass_button);

        assetManager = getAssets();
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        switch (focusChange) {
                            case AudioManager.AUDIOFOCUS_GAIN:
                                if (!mediaPlayer.isPlaying()) {
                                    mediaPlayer.start();
                                }
                                break;
                            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                                break;
                            case AudioManager.AUDIOFOCUS_LOSS:
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                }
                                break;
                            default:
                        }
                    }
                })
                .setAudioAttributes(audioAttributes)
                .setFocusGain(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).build();

        kdsNumberEditText = findViewById(R.id.kds_number_edit_text);
        kdsNumber = kdsNumberEditText.getText().toString().equals("") ? 0 : Integer.parseInt(kdsNumberEditText.getText().toString());
        MLog.debug("获取输入框文本", kdsNumber + "");


        audioMap = new HashMap<>();
        loadAudio();


        ButtonPlus buttonPlus = new ButtonPlus();
        plusButton.setOnClickListener(buttonPlus);

        ButtonLess buttonLess = new ButtonLess();
        lessButton.setOnClickListener(buttonLess);

        KdsStart kdsStart = new KdsStart();
        startButton.setOnClickListener(kdsStart);

        KdsPass kdsPass = new KdsPass();
        passButton.setOnClickListener(kdsPass);

    }

    private void loadAudio() {

        String[] audioNameList = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "q", "qc"};

        try {
            for (String s : audioNameList) {
                audioMap.put(s, assetManager.openFd("audio/" + s + ".mp3"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ButtonPlus implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            kdsNumber = kdsNumberEditText.getText().toString().equals("") ? 0 : Integer.parseInt(kdsNumberEditText.getText().toString());
            kdsNumber++;
            kdsNumberEditText.setText(NumberFormat.number2String4(kdsNumber));
        }
    }

    private class ButtonLess implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            kdsNumber = kdsNumberEditText.getText().toString().equals("") ? 0 : Integer.parseInt(kdsNumberEditText.getText().toString());
            if (kdsNumber <= 1) {
                kdsNumber = 1;
            } else {
                kdsNumber--;
            }
            kdsNumberEditText.setText(NumberFormat.number2String4(kdsNumber));
        }
    }

    private class KdsPass extends ButtonPlus {

        @Override
        public void onClick(View v) {
            super.onClick(v);
            mediaPlayer.stop();
        }
    }

    private class KdsStart implements View.OnClickListener {

        int audioNameNum;
        int kdsRepeatNum;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            audioNameNum = 0;
            kdsRepeatNum = 0;
            kdsNumber = kdsNumberEditText.getText().toString().equals("") ? 0 : Integer.parseInt(kdsNumberEditText.getText().toString());
            mediaPlayer.stop();
            String[] audioNameList = {"q", "0", "1", "0", "0", "qc"};
            String[] kdsNumberArray = NumberFormat.number2String4(kdsNumber).split("");
            System.arraycopy(kdsNumberArray, 0, audioNameList, 1, 4);

            audioManager.requestAudioFocus(audioFocusRequest);


            playAudio(audioNameList[0]);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioNameNum += 1;
                    if (audioNameNum < audioNameList.length) {
                        playAudio(audioNameList[audioNameNum]);
                    } else if (kdsRepeatNum == 0) {
                        kdsRepeatNum++;
                        audioNameNum = 0;
                        playAudio(audioNameList[0]);
                    } else {
                        audioManager.abandonAudioFocusRequest(audioFocusRequest);
                    }
                }
            });

        }

        private void playAudio(String key) {
            AssetFileDescriptor assetFileDescriptor = audioMap.get(key);
            mediaPlayer.reset();
            //设置媒体播放器的数据资源
            assert assetFileDescriptor != null;
            try {
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            MLog.debug("音频播放", "当前播放" + key);
        }
    }
}