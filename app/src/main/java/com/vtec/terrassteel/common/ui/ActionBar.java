package com.vtec.terrassteel.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ActionBarListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionBar extends LinearLayout {

    ActionBarListener listener;

    @BindView(R.id.titleTextView)
    FontTextView titleTextView;

    @BindView(R.id.actionButton)
    ImageView actionButton;

    @BindView(R.id.back_arrow)
    View backArrow;

    @OnClick(R.id.back_arrow)
    public void onClickOnBackArrow(){
        if(listener != null){
            listener.onBackArrowClick();
        }
    }

    @OnClick(R.id.actionButton)
    public void onClickOnActionButton(){
        if(listener != null) {
            listener.onActionButtonClick();
        }
    }

    public ActionBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ActionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public ActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);

    }

    public void setListener(ActionBarListener listener){
        this.listener = listener;
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        View view = inflate(getContext(), R.layout.action_bar_view, this);
        ButterKnife.bind(this, view);

        TypedArray attributes = context
                .obtainStyledAttributes(attrs, R.styleable.ActionBarView, defStyleAttr, defStyleRes);

        titleTextView.setText(attributes.getString(R.styleable.ActionBarView_title));

        if(attributes.getBoolean(R.styleable.ActionBarView_showActionButton, false)){
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setImageDrawable(attributes.getDrawable(R.styleable.ActionBarView_actionButtonDrawable));
        }else{
            actionButton.setVisibility(View.INVISIBLE);
        }

        if(attributes.getBoolean(R.styleable.ActionBarView_showBackArrow, true)){
            backArrow.setVisibility(View.VISIBLE);
        }else{
            backArrow.setVisibility(View.INVISIBLE);
        }

    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void showBackArrow(boolean shouldShowBackArrow) {
        if(shouldShowBackArrow){
            backArrow.setVisibility(View.VISIBLE);
        }else{
            backArrow.setVisibility(View.INVISIBLE);
        }
    }
}
