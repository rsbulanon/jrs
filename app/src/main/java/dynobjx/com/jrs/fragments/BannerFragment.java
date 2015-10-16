package dynobjx.com.jrs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import dynobjx.com.jrs.R;


/**
 * Created by rsbulanon on 7/8/15.
 */
public class BannerFragment extends Fragment {

    private int image;

    public static BannerFragment newInstance(int image) {
        BannerFragment fragment = new BannerFragment();
        fragment.image = image;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, null, false);
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageLoader.displayImage("drawable://" + image, imageView);
        return view;
    }
}