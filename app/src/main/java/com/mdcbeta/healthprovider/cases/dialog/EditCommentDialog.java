package com.mdcbeta.healthprovider.cases.dialog;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;
import com.mdcbeta.healthprovider.cases.CaseDetailFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class EditCommentDialog extends BaseDialogFragment {

    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.cancel_btn)
    ImageButton btnClose;

    @Inject
    ApiFactory apiFactory;

    private static String mCaseId, mComment, mCommentById, mCommentId;

    public static EditCommentDialog newInstance(String caseId, String comment, String commentById, String commentId) {
        EditCommentDialog frag = new EditCommentDialog();
        mCaseId = caseId;
        mComment = comment;
        mCommentById = commentById;
        mCommentId = commentId;
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etComment.setText(mComment.trim());

        btnUpdate.setOnClickListener(view12 -> {
            if (etComment.getText().toString().equals(mComment))
                Toast.makeText(getContext(), getContext().getString(R.string.comment_not_changed), Toast.LENGTH_SHORT).show();

            else if (!etComment.getText().toString().isEmpty()) {
                showProgress(getContext().getString(R.string.updating_comment));

                apiFactory.updateComment(mCaseId, etComment.getText().toString().trim(),
                        mCommentById, Utils.localToUTCDateTime(Utils.getDatetime()), mCommentId)
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {
                            if (model.status) {

                                showSuccess(getContext().getString(R.string.comment_updated));
                                if (getTargetFragment() instanceof CaseDetailFragment) {
                                    CaseDetailFragment frag = (CaseDetailFragment) getTargetFragment();
                                    frag.getData();
                                }
                                if (getTargetFragment() instanceof Appointment1DetailFragment) {
                                    Appointment1DetailFragment frag = (Appointment1DetailFragment) getTargetFragment();
                                    frag.getData();
                                }
                                dismiss();

                            } else {
                                showError(model.getMessage());
                                dismiss();
                            }

                        }, throwable -> {
                            showError(throwable.getMessage());
                            dismiss();
                        });

            } else
                Toast.makeText(getContext(), getContext().getString(R.string.comment_required), Toast.LENGTH_SHORT).show();
        });

        btnClose.setOnClickListener(view1 -> dismiss());

    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_edit_comment;
    }

    @Override
    public void onValidationSucceeded() {

    }

}

