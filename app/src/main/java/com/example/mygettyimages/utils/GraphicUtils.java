package com.example.mygettyimages.utils;


import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class GraphicUtils {

    public static void displayImage(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        ImageAware imageAware = new ImageViewAware(imageView, true);
        ImageLoader.getInstance().displayImage(url, imageAware);
    }

}
