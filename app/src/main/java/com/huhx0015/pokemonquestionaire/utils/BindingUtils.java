package com.huhx0015.pokemonquestionaire.utils;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.huhx0015.pokemonquestionaire.R;

/**
 * Created by Michael Yoon Huh on 6/16/2017.
 */

public class BindingUtils {

    /** BINDING METHODS ________________________________________________________________________ **/

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(AppCompatImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }
}