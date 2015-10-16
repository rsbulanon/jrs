package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class BranchByKeywordDialogFragment extends DialogFragment {

    private View view;
    private OnEnteredKeywordListener onEnteredKeywordListener;
    @Bind(R.id.etKeyword) EditText etKeyword;

    public static BranchByKeywordDialogFragment newInstance() {
        BranchByKeywordDialogFragment frag = new BranchByKeywordDialogFragment();
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.modal_branch_by_keyword, null);
        ButterKnife.bind(this, view);

        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }
    @OnClick(R.id.tvSearch)
    public void searchBranchByKeyword() {
        String keyword = etKeyword.getText().toString();
        if (keyword.isEmpty()) {
            etKeyword.setError("This is a required field!");
            etKeyword.requestFocus();
        } else {
            onEnteredKeywordListener.onEnteredKeyword(keyword);
        }
    }
    public interface OnEnteredKeywordListener {
        void onEnteredKeyword(String keyword);
    }

    public void setOnEnteredKeywordListener(OnEnteredKeywordListener onEnteredKeywordListener) {
        this.onEnteredKeywordListener = onEnteredKeywordListener;
    }
}