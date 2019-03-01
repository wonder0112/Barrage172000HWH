package cn.edu.gdpt.xxgcx.barrage172000hwh;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView mVdoViMainVideo;
    private DanmakuView mKuViewMainBarrage;
    private EditText mEdtTxtMainInput;
    private Button mBtnMainSend;
    private LinearLayout mLinLayMainBottom;
    //1、声明或定义3个变量
    private boolean showDanmaku;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser=new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        playVideo();
        initDanmaku();
    }
    private  void initDanmaku(){//初始化弹幕视图控件
        mKuViewMainBarrage.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku=true;
                mKuViewMainBarrage.start();
                generateDanmaku();
            }
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }
            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }
            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext=DanmakuContext.create();//创建弹幕上下文对象
        mKuViewMainBarrage.enableDanmakuDrawingCache(true);
        mKuViewMainBarrage.prepare(parser,danmakuContext);
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
        addDanmaku(input,true);
    }
    /**
     * 单条弹幕
     */
    private  void addDanmaku(String context,boolean border){
        //定义一条弹幕
        BaseDanmaku danmaku=danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text=context;//文本
        danmaku.textSize=30;//字体大小
        danmaku.padding=6;//内边距
        danmaku.textColor=Color.WHITE;//字体颜色
        danmaku.setTime(mKuViewMainBarrage.getCurrentTime());//显示时间
        if(border){
            danmaku.borderColor=Color.RED;//边框颜色
        }
        mKuViewMainBarrage.addDanmaku(danmaku);//将弹幕加到弹幕视图中显示
    }

    /**
     * 随机数多弹幕
     */
    private  void generateDanmaku(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(showDanmaku){  //死循环
                    int num=new Random().nextInt(300);//产生一个整数
                    String context=""+num;//转换成字符串
                    addDanmaku(context,false);//调用弹幕函数
                    try{
                        Thread.sleep(num);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
