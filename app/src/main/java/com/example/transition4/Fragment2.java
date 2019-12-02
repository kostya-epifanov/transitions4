package com.example.transition4;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class Fragment2 extends Fragment {

  public static Fragment2 instance() {
    return new Fragment2();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    postponeEnterTransition();
    View root = inflater.inflate(R.layout.fragment_2, container, false);

    final View item = root.findViewById(R.id.texture_container);
    item.setTag(new ItemView.Item(1, "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));

    return root;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    System.out.println("2 onViewCreated: " + view.findViewById(R.id.texture).getTransitionName());

    new Handler().postDelayed(() -> {
      view.getRootView().invalidate();
      startPostponedEnterTransition();
    }, 100);

  }


  static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
  static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
  static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
  static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);

  static final int ANIM_DUR = 300 /*1000*/;

  static AnimationSet makeOpenCloseAnimation(float startScale, float endScale, float startAlpha, float endAlpha) {
    AnimationSet set = new AnimationSet(false);
    ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale,
                                              Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
    scale.setInterpolator(DECELERATE_QUINT);
    scale.setDuration(ANIM_DUR);
    set.addAnimation(scale);
    AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
    alpha.setInterpolator(DECELERATE_CUBIC);
    alpha.setDuration(ANIM_DUR);
    set.addAnimation(alpha);
    return set;
  }

  static AlphaAnimation makeFadeAnimation(float start, float end) {
    AlphaAnimation anim = new AlphaAnimation(start, end);
    anim.setInterpolator(DECELERATE_CUBIC);
    anim.setDuration(ANIM_DUR);
    return anim;
  }

  public static final int ANIM_STYLE_OPEN_ENTER_ = 1;
  public static final int ANIM_STYLE_OPEN_EXIT_ = 2;
  public static final int ANIM_STYLE_CLOSE_ENTER_ = 3;
  public static final int ANIM_STYLE_CLOSE_EXIT_ = 4;
  public static final int ANIM_STYLE_FADE_ENTER_ = 5;
  public static final int ANIM_STYLE_FADE_EXIT_ = 6;

  public static int transitToStyleIndex(int transit, boolean enter) {
    int animAttr = -1;
    switch (transit) {
      case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
        animAttr = enter ? ANIM_STYLE_OPEN_ENTER_ : ANIM_STYLE_OPEN_EXIT_;
        break;
      case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
        animAttr = enter ? ANIM_STYLE_CLOSE_ENTER_ : ANIM_STYLE_CLOSE_EXIT_;
        break;
      case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
        animAttr = enter ? ANIM_STYLE_FADE_ENTER_ : ANIM_STYLE_FADE_EXIT_;
        break;
    }
    return animAttr;
  }

  @Override
  public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
    final int styleIndex = transitToStyleIndex(transit, enter);
    switch (styleIndex) {
      case ANIM_STYLE_OPEN_ENTER_:
        return makeOpenCloseAnimation(1.125f, 1.0f, 0, 1);
      case ANIM_STYLE_OPEN_EXIT_:
        return makeOpenCloseAnimation(1.0f, .975f, 1, 0);
      case ANIM_STYLE_CLOSE_ENTER_:
        return makeOpenCloseAnimation(.975f, 1.0f, 0, 1);
      case ANIM_STYLE_CLOSE_EXIT_:
        return makeOpenCloseAnimation(1.0f, 1.075f, 1, 0);
      case ANIM_STYLE_FADE_ENTER_:
        return makeFadeAnimation(0, 1);
      case ANIM_STYLE_FADE_EXIT_:
        return makeFadeAnimation(1, 0);
      default: return null;
    }
  }

}
