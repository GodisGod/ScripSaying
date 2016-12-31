package com.feiyu.scripsaying.util.recorder;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.feiyu.scripsaying.R;

/**
 * Created by ${鸿达} on 2016/8/30.
 */
public class RecordButton extends Button implements AudioManger.AudioStateListener {

    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurSate = STATE_NORMAL;
    private boolean isRecording = false;
    private DialogManager mdialogManager;
    private AudioManger mAudioManger;

    //计算录音时间
    private float mTime;
    //是否触发longclick
    private boolean mReady;
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    mdialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    //更新音量
                    mdialogManager.updateVoiceLevel(mAudioManger.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    mdialogManager.dismissDialog();
                    break;
            }
        }
    };

    public RecordButton(Context context) {
        this(context, null);
    }

    //录音完成后的回调
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener mFinishListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mFinishListener = listener;
    }


    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mdialogManager = new DialogManager(getContext());
        String dir = Environment.getExternalStorageDirectory() + "/scrip_audios";
        mAudioManger = AudioManger.getmInstance(dir);
        mAudioManger.setOnAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true;
                mAudioManger.prepareAudio();
                return false;
            }
        });
    }

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                //已经开始录音
                if (isRecording) {
                    //根据x,y的坐标判断是否想要取消
                    if (wantToCancle(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || mTime < 0.6) {
                    mdialogManager.tooShort();
                    mAudioManger.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                } else if (mCurSate == STATE_RECORDING) {//正常录制结束
                    //release
                    mdialogManager.dismissDialog();
                    //callbackToAct
                    mAudioManger.release();
                    if (mFinishListener != null) {
                        float b = (float) (Math.round(mTime * 100)) / 100;//保留两位小数
                        mFinishListener.onFinish(b, mAudioManger.getCurrentFilePath());
                    }
                } else if (mCurSate == STATE_WANT_TO_CANCEL) {
                    //cancle
                    mdialogManager.dismissDialog();
                    mAudioManger.cancel();
                }
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    //恢复一些标志位
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancle(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    private void changeState(int stateRecording) {
        if (mCurSate != stateRecording) {
            mCurSate = stateRecording;
            switch (stateRecording) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mdialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_want_cancel);
                    mdialogManager.wantToCancel();
                    break;
            }
        }
    }


}
