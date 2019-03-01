package cn.edu.gdpt.xxgcx.barrage172000hwh;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import master.flame.danmaku.ui.widget.DanmakuView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView mVdoViMainVideo;
    private DanmakuView mKuViewMainBarrage;
    private EditText mEdtTxtMainInput;
    private Button mBtnMainSend;
    private LinearLayout mLinLayMainBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        playVideo();
    }
    /**
     * 播放视频
     */
    private  void playVideo(){
        String uri="android.resource://"+getPackageName()+"/"+R.raw.sun;//视频路径
        if(uri!=null){
            mVdoViMainVideo.setVideoURI(Uri.parse(uri));//视频赋给VideoView控件
            mVdoViMainVideo.start();//播放
            //循环播放
            mVdoViMainVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
        }else {
            mVdoViMainVideo.getBackground().setAlpha(0);//设置背景透明
        }

    }

    private void initView() {
        mVdoViMainVideo = (VideoView) findViewById(R.id.vdoVi_main_video);
        mKuViewMainBarrage = (DanmakuView) findViewById(R.id.kuView_main_barrage);
        mEdtTxtMainInput = (EditText) findViewById(R.id.edtTxt_main_input);
        mBtnMainSend = (Button) findViewById(R.id.btn_main_send);
        mLinLayMainBottom = (LinearLayout) findViewById(R.id.linLay_main_bottom);
        mLinLayMainBottom.setVisibility(View.GONE);
        mBtnMainSend.setOnClickListener(this);
        mKuViewMainBarrage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_send:
                submit();
                break;
            case R.id.kuView_main_barrage:  //点击屏幕处理器
                if(mLinLayMainBottom.getVisibility()==View.GONE){ //输入栏是否隐藏？
                    mLinLayMainBottom.setVisibility(View.VISIBLE);//是时，设为显示
                }else{
                    mLinLayMainBottom.setVisibility(View.GONE);//否则，设为隐藏
                }
                break;
        }
    }

    private void submit() {
        // validate
        String input = mEdtTxtMainInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "input不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
