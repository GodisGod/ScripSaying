package com.feiyu.scripsaying.util.recorder;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyu.scripsaying.R;

/**
 * Created by ${鸿达} on 2016/8/30.
 */
//不可使用单例模式，因为单例模式的引用是application级别的，当取消dialog的时候并不能释放
// 就会造成下次使用dialog的异常或者内存泄露
public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;
    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("手指上滑,取消发送");
        }
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.id_record_dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.id_record_dialog_voice);
        mLable = (TextView) mDialog.findViewById(R.id.id_record_dialog_label);

        mDialog.show();
    }

    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指,取消发送");
        }
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("录音时间过短");
        }
    }

    //更新音量.通过level去更新voice上的图片
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("v" + level, "drawable",
                    mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
