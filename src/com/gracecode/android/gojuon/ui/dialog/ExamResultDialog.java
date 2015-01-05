package com.gracecode.android.gojuon.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.helper.ExamHelper;
import com.gracecode.android.gojuon.ui.activity.ExamActivity;
import com.gracecode.android.gojuon.ui.activity.PrefActivity;

public class ExamResultDialog extends BaseDialog {
    private ExamActivity mExamActivity;
    private ExamHelper mExamHelper;
    private TextView mExamPercent;
    private Button mExamButtonTryAgain;
    private Button mExamButtonRedoWrong;
    private Button mExamButtonConfigure;
    private Button mExamButtonExit;

    public ExamResultDialog() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof ExamActivity) {
            mExamActivity = (ExamActivity) activity;
            mExamHelper = mExamActivity.getExamHelper();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_result, null);
        if (view != null) {
            mExamPercent = (TextView) view.findViewById(R.id.exam_percent);
            mExamButtonTryAgain = (Button) view.findViewById(R.id.exam_try_again);
            mExamButtonRedoWrong = (Button) view.findViewById(R.id.exam_redo_wrong);
            mExamButtonConfigure = (Button) view.findViewById(R.id.exam_configure);
            mExamButtonExit = (Button) view.findViewById(R.id.exam_exit);
        }

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.exam_redo_wrong:
                    mExamActivity.redoWrongTopic();
                    break;

                case R.id.exam_try_again:
                    mExamActivity.startExam();
                    break;

                case R.id.exam_configure:
                    startActivity(new Intent(mExamActivity, PrefActivity.class));
                    break;

                case R.id.exam_exit:
                    mExamActivity.finish();
                    break;
            }

            dismiss();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);

        for (View view : new View[]{mExamButtonTryAgain, mExamButtonRedoWrong, mExamButtonConfigure, mExamButtonExit}) {
            view.setOnClickListener(mOnClickListener);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mExamPercent.setText(String.format("%.0f%%",
                (1 - (mExamHelper.getWrongCount() * 1f / mExamHelper.getTotalCount())) * 100));

        mExamButtonRedoWrong.setEnabled((mExamHelper.getWrongCount() > 0));
    }
}
