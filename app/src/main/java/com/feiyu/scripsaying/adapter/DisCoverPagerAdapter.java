package com.feiyu.scripsaying.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.DiscoverScrip;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.view.GlideCircleTransform;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import io.rong.imkit.RongIM;

/**
 * Created by HONGDA on 2017/1/1.
 */
public class DisCoverPagerAdapter extends PagerAdapter {
    private Context context;
    private List<DiscoverScrip> discoverScrips;

    //根据user表查询
    private String strsendUserIcon;          //发送者头像
    private String userType;              //发送者类型
    //根据Scrip表查询
    private String sendUserType;       //发送者类型萝莉御姐少年绅士等
    private String sendUserId;          //发送者ID
    private String sendUserGender;     //发送者性别
    private BmobFile ScripType;          //纸片类型的图片描述 足球篮球运动健身
    private String ScripTypeText;          //纸片类型的文字描述 足球篮球运动健身
    private String ScripImg;         //纸片图片
    private String ScripAudio;      //纸片语音
    private String Scriptext;   //纸片内容

    public DisCoverPagerAdapter(Context context, List<DiscoverScrip> discoverScrips) {
        this.context = context;
        this.discoverScrips = discoverScrips;
    }

    @Override
    public int getCount() {
        return discoverScrips.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_discover_scrip, null);
        container.addView(view);
        HD.LOG("=====   instantiateItem   ======");
        ImageView sendUserIcon = (ImageView) view.findViewById(R.id.img_send_user_icon);
        ImageView sendScripTag = (ImageView) view.findViewById(R.id.img_send_scrip_tag);
        ImageView sendUserType = (ImageView) view.findViewById(R.id.img_send_user_type);
        ImageView sendImgContent = (ImageView) view.findViewById(R.id.scrip_img);
        ImageView sendUserAudio = (ImageView) view.findViewById(R.id.img_send_user_audio);
        TextView sendAudioTime = (TextView) view.findViewById(R.id.tv_audio_time);
        TextView sendTextContent = (TextView) view.findViewById(R.id.tv_scrip_content);
        TextView sendText1 = (TextView) view.findViewById(R.id.scrip_text_1);
        Button btnReply = (Button) view.findViewById(R.id.btn_reply);

        DiscoverScrip discoverScrip = discoverScrips.get(position);


        String struserType = discoverScrip.getUserType();
        String strsendUserType = discoverScrip.getUserType();
        final String strsendUserId = discoverScrip.getSendUserId();
        final String strsendUserName = discoverScrip.getSendUserName();
        String strsendUserGender = discoverScrip.getSendUserGender();
        String strScripTypeText = discoverScrip.getScripTypeText();
        String strScriptext = discoverScrip.getScriptext();

        //显示头像
        if (discoverScrip.getSendUserIcon()!=null){
            strsendUserIcon = discoverScrip.getSendUserIcon();
            if (strsendUserIcon.equals(GlobalConstant.DEFAULT_USER_ICON_URL)){
                Glide.with(context).load(R.mipmap.default_head)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(context))
                        .into(sendUserIcon);
            }else{
                Glide.with(context).load(strsendUserIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(context))
                        .into(sendUserIcon);
            }
        }else{
            //没有就显示默认头像
            Glide.with(context).load(R.mipmap.default_head)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(context))
                    .into(sendUserIcon);
        }

        //显示tag
        int ScripType;
        if (discoverScrip.getScripType() != 0) {
            ScripType = discoverScrip.getScripType();
            sendScripTag.setVisibility(View.VISIBLE);
            int resId = context.getResources().getIdentifier("tag" + ScripType,"mipmap",
                    context.getPackageName());
            Glide.with(context).load(resId)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(context))
                    .into(sendScripTag);

//            if (ScripType == 1) {
//                Glide.with(context).load(R.mipmap.tag1)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .transform(new GlideCircleTransform(context))
//                        .into(sendScripTag);
//            } else if (ScripType == 2) {
//                Glide.with(context).load(R.mipmap.tag2)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .transform(new GlideCircleTransform(context))
//                        .into(sendScripTag);
//            } else if (ScripType == 3) {
//                Glide.with(context).load(R.mipmap.tag3)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .transform(new GlideCircleTransform(context))
//                        .into(sendScripTag);
//            } else if (ScripType == 4) {
//                Glide.with(context).load(R.mipmap.tag4)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .transform(new GlideCircleTransform(context))
//                        .into(sendScripTag);
//            }
        }else{//没有tag就隐藏
            sendScripTag.setVisibility(View.INVISIBLE);
        }

        //显示声音
        if (discoverScrip.getScripAudio() != null) {
            sendUserAudio.setVisibility(View.VISIBLE);
            ScripAudio = discoverScrip.getScripAudio().getFileUrl();
            //todo 下载语音并播放
//            Glide.with(context).load(ScripAudio)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .transform(new GlideCircleTransform(context))
//                    .into(sendUserAudio);
        }
        //显示图片
        if (discoverScrip.getScripImg() != null) {
            sendImgContent.setVisibility(View.VISIBLE);
            sendUserAudio.setVisibility(View.VISIBLE);
            sendAudioTime.setVisibility(View.VISIBLE);
            sendTextContent.setVisibility(View.VISIBLE);
            sendText1.setVisibility(View.INVISIBLE);
            ScripImg = discoverScrip.getScripImg().getFileUrl();
            //todo 图片纸片
            Glide.with(context).load(ScripImg)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(context))
                    .into(sendImgContent);
            HD.LOG("加载图片：  " + ScripImg);
            if (!strScriptext.isEmpty()) {
                sendTextContent.setText(strScriptext);
            }
        } else {
            //显示文字
            sendImgContent.setVisibility(View.INVISIBLE);
            sendUserAudio.setVisibility(View.INVISIBLE);
            sendAudioTime.setVisibility(View.INVISIBLE);
            sendTextContent.setVisibility(View.INVISIBLE);
            sendText1.setVisibility(View.VISIBLE);
            HD.LOG("加载文字：  " + strScriptext);
            sendText1.setText(strScriptext);
        }
        //回复
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RongIM.getInstance() != null) {
                    HD.LOG("开启私聊页面  " + strsendUserId);
                    RongIM.getInstance().startPrivateChat(context, strsendUserId, strsendUserName);
                    //todo 接收预置消息
                }
            }
        });

        return view;
    }

}
