package com.company.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.company.myapplication.view.JumpUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;


/**
 * 单独的视频播放页面
 * Created by shuyu on 2016/11/11.
 */
public class PlayActivity extends AppCompatActivity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    private SampleVideo videoPlayer;

    OrientationUtils orientationUtils;

    private boolean isTransition;

    private TextView shot;
    private TextView recode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
//        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        videoPlayer = findViewById(R.id.video_player);
        init();
    }

    private boolean isStarting = true;

    public void setTextColor(int color, String message, boolean isStarting) {
        recode.setText(message);
        recode.setTextColor(color);
        this.isStarting = isStarting;
    }

    private void init() {
        String url = "https://res.exexm.com/cw_145225549855002";

        //String url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
        //需要路径的
        //videoPlayer.setUp(url, true, new File(FileUtils.getPath()), "");

        //借用了jjdxm_ijkplayer的URL
        String source1 = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        String name2 = "清晰";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);

        videoPlayer.setUp(list, true, "测试视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //videoPlayer.setShowPauseCover(false);

        //videoPlayer.setSpeed(2f);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        shot = (TextView) videoPlayer.findViewById(R.id.shot);
        recode = (TextView) videoPlayer.findViewById(R.id.record);
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hotImage(v);
//                PlayActivityPermissionsDispatcher.shotImageWithPermissionCheck(PlayActivity.this, v);
PlayActivityPermissionsDispatcher.shotImageWithPermissionCheck(PlayActivity.this,v);
//                Toast.makeText(PlayActivity.this, "截图..", Toast.LENGTH_SHORT).show();
            }
        });
        recode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarting) {
                    setTextColor(getResources().getColor(R.color.colorAccent), "录屏中...", false);
                    Toast.makeText(PlayActivity.this, "开始..录屏", Toast.LENGTH_SHORT).show();

                } else {
                    setTextColor(getResources().getColor(R.color.white), "录屏", true);
                    Toast.makeText(PlayActivity.this, "录屏..结束", Toast.LENGTH_SHORT).show();


                }
            }
        });
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        //videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
        //videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
        //videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
        //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
        //videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
//
//    private void hotImage(View v) {
//
//        //获取截图
//        videoPlayer.taskShotPic(new GSYVideoShotListener() {
//            @Override
//            public void getBitmap(Bitmap bitmap) {
//                if (bitmap != null) {
//                    try {
//                        CommonUtil.saveBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        showToast("save fail ");
//                        e.printStackTrace();
//                        return;
//                    }
//                    showToast("save success ");
//                } else {
//                    showToast("get bitmap fail ");
//                }
//            }
//        });
//    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        super.onBackPressed();

    }
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("快给我权限")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }



    /**
     * 视频截图
     * 这里没有做读写本地sd卡的权限处理，记得实际使用要加上
     */
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void shotImage(final View v) {
        //获取截图
        videoPlayer.taskShotPic(new GSYVideoShotListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                if (bitmap != null) {
                    try {
                        CommonUtil.saveBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        showToast("截图失败 ");
                        e.printStackTrace();
                        return;
                    }
                    showToast("截图成功 ");
                } else {
                    showToast("截图存储失败 ");
                }
            }
        });

    }
    private void showToast(final String tip) {
        videoPlayer.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayActivity.this, tip, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(this, "没有权限啊", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        PlayActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }





























}
