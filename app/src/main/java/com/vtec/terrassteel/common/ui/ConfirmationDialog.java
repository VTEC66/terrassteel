package com.vtec.terrassteel.common.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.vtec.terrassteel.R;
import com.vtec.terrassteel.common.listener.ConfirmationDialogCallback;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmationDialog  extends DialogFragment {

    private ConfirmationDialogCallback callback;
    private String confirmationMessage;

    @BindView(R.id.confirmation_message_tv)
    TextView confirmationMessageTextView;

    @OnClick(R.id.yes_button)
    public void onClickYesButton(){
        callback.onConfirm();
        dismiss();
    }

    @OnClick(R.id.no_button)
    public void onClickNoButton(){
        dismiss();
    }

    public ConfirmationDialog setCallBack(ConfirmationDialogCallback callBack) {
        this.callback = callBack;
        return this;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        View thisView = inflater.inflate(R.layout.confirmation_dialog_fragment, parent, false);

        ButterKnife.bind(this, thisView);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        confirmationMessageTextView.setText(confirmationMessage);

        return thisView;

    }

    public ConfirmationDialog setConfirmationMessage(String message) {
        this.confirmationMessage = message;
        return this;
    }
}
