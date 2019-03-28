package com.lkl.ansuote.hdqlibrary.util.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class ImageLoader {

    /**
     * 加载网络资源
     * @param context
     * @param url
     * @param iv
     */
    public static void load(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(context).load(url).into(iv);
    }

    /**
     * 加载本地资源
     * @param context
     * @param resId
     * @param iv
     */
    public static void load(Context context, @DrawableRes int resId, ImageView iv) {
        //使用缓存会导致图片不能及时更新
        //Glide.with(context).load(resId).into(iv);
        if (null != iv) {
            iv.setImageResource(resId);
        }
    }



    /**
     * 加载圆形
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleCrop(Context context,
                                      String url,
                                      ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载网络资源 -- 由外界定义属性
     * @param context
     * @param url
     * @param iv
     */
    public static void load(Context context, String url, ImageView iv, RequestOptions options) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void load(Context context, Uri uri, ImageView iv, RequestOptions options) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(iv);
    }
}
