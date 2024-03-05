package com.chunxia.chatgpt.fragment;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE_ALL;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE_SINGLE;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_TOPIC;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.ActivityIntentKeys;
import com.chunxia.chatgpt.activity.AddReviewCardActivity;
import com.chunxia.chatgpt.activity.BottomNavigationLightActivity;
import com.chunxia.chatgpt.activity.ReviewCardActivity;
import com.chunxia.chatgpt.base.AppFragment;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.model.review.AllReviewData;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.TopicReviewSets;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.ui.ReviewCardListItemView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class ChatGptReviewFragment extends AppFragment<BottomNavigationLightActivity> {

    private MaterialButton startReviewButton;
    private MaterialButton addNewCardListButton;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    private View bottom_sheet;

    public ChatGptReviewFragment() {
    }


    public static ChatGptReviewFragment newInstance() {
        return new ChatGptReviewFragment();
    }



    private void initStatusBar() {
        Tools.setSystemBarColor(getActivity(), R.color.grey_10);
        Tools.setSystemBarLight(getActivity());
    }


    private void initStartReviewButton() {
        startReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicReviewSets topicReviewSets = ReviewCardManager.getInstance().getAllTopicReviewSets();
                if (topicReviewSets.size() == 0) {
                    Toast.makeText(getContext(), R.string.review_fragment_empty_card_list_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                ReviewCardManager.getInstance().setCurrentTopicReviewSets(topicReviewSets);
                Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class)
                        .putString(ACTIVITY_REVIEW_CARD_MODE, ACTIVITY_REVIEW_CARD_MODE_ALL);
                ActivityUtils.startActivity(intent);
            }
        });
    }

    private void initAddYourOwnCardButton() {
        addNewCardListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewTopicCardListDialog();
            }
        });
    }

    @Override
    public void initView() {
        startReviewButton = findViewById(R.id.exerciseButton);
        initStartReviewButton();
        addNewCardListButton = findViewById(R.id.autoplayButton);
        initAddYourOwnCardButton();
        initReviewListViews();
        initBottomSheetList();

        initStatusBar();

    }

    @Override
    protected void initData() {

    }

    private void initBottomSheetList() {
        bottom_sheet = findViewById(R.id.review_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
    }

    @Override
    public void onResume() {
        super.onResume();
        // todo 优化 全部刷新太耗时，只应该刷新修改的部分
        initReviewListViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_review;
    }

    private void initReviewListViews() {
        AllReviewData allReviewData = ReviewCardManager.getInstance().getAllReviewData();
        if(allReviewData == null) return;
        int size = allReviewData.getSize();
        if (size == 0) return;

        Activity activity = getActivity();

        LinearLayout container = findViewById(R.id.fragment_review_cards_container);
        container.removeAllViews();

        for (int i = 0; i < size; i++) {
            TopicReviewSets tempTopicReviewSets = allReviewData.getTopicReviewSetsList().get(i);
            TopicReviewSets.ReviewData reviewData = tempTopicReviewSets.getReviewNumber();
            String topic = tempTopicReviewSets.getTopic();

            ReviewCardListItemView reviewCardListItemView = new ReviewCardListItemView(activity);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                    LinearLayout.LayoutParams.WRAP_CONTENT // 高度
            );

            int margin = Tools.dip2px(activity, 10); // 将 dp 转换为像素
            layoutParams.setMargins(margin, margin, margin, 0);
//            reviewCardListItemView.setBackground(getResources().getDrawable(R.drawable.pay_view_blue, null));
            reviewCardListItemView.setTopic(topic);
            reviewCardListItemView.setUnReviewCount(reviewData.unReviewedNumber);
            reviewCardListItemView.setReviewingCount(reviewData.reviewingNumber);
            reviewCardListItemView.setReviewedCount(reviewData.reviewedNumber);
            reviewCardListItemView.setLatestReviewTime(tempTopicReviewSets.getLatestReviewTime());
            reviewCardListItemView.setMenuIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog(topic);
                }
            });

            container.addView(reviewCardListItemView, layoutParams);

            reviewCardListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TopicReviewSets topicReviewSets = ReviewCardManager.getInstance()
                            .getTopicReviewSetsByTopic(topic);

                    if (topicReviewSets.size() == 0) {
                        Toast.makeText(activity, R.string.review_fragment_empty_card_list_hint, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ReviewCardManager.getInstance().setCurrentTopicReviewSets(topicReviewSets);
                    Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class)
                            .putString(ACTIVITY_REVIEW_CARD_MODE, ACTIVITY_REVIEW_CARD_MODE_SINGLE)
                            .putString(ACTIVITY_REVIEW_CARD_TOPIC, topic);

                    ActivityUtils.startActivity(intent);
                }
            });
        }
    }


    private void showNewTopicCardListDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.review_dialog_rename);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView textView = dialog.findViewById(R.id.review_dialog_title);
        String title = getResources().getString(R.string.title_new_card_list_input_dialog_title);
        textView.setText(title);

        EditText editView = dialog.findViewById(R.id.review_dialog_rename_input);

        ((AppCompatButton) dialog.findViewById(R.id.review_dialog_rename_cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.review_dialog_rename_ok_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(editView.getText());
                if (text.isEmpty()) {
                    String toast = getResources().getString(R.string.title_new_card_list_empty_input_toast);
                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                } else {
                    TopicReviewSets topicReviewSets = new TopicReviewSets(text);
                    try {
                        ReviewCardManager.getInstance().addOneTopicReviewSets(topicReviewSets);
                        initReviewListViews();
                        dialog.dismiss();
                    } catch (AllReviewData.TopicExistedException e) {
                        String toast = getResources().getString(R.string.title_new_card_list_input_already_toast);
                        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showRenameDialog(String topic) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.review_dialog_rename);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView textView = dialog.findViewById(R.id.review_dialog_title);
        String title = getResources().getString(R.string.title_preset_name_input_dialog);
        textView.setText(title);

        EditText editView = dialog.findViewById(R.id.review_dialog_rename_input);
        editView.setText(topic);
        ((AppCompatButton) dialog.findViewById(R.id.review_dialog_rename_cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.review_dialog_rename_ok_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTopic = String.valueOf(editView.getText());
                try {
                    ReviewCardManager.getInstance().renameTopicReviewSetsByTopic(topic, newTopic);
                    initReviewListViews();
                    dialog.dismiss();
                } catch (AllReviewData.TopicExistedException e) {
                    String toast = getResources().getString(R.string.title_new_card_list_rename_already_exist_toast);
                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showBottomSheetDialog(String topic) {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.review_bottom_sheet_list, null);

        ((View) view.findViewById(R.id.change_quizcard_list_title)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出修改标题的对话框
                mBottomSheetDialog.dismiss();
                showRenameDialog(topic);
            }
        });

        ((View) view.findViewById(R.id.delete_quizcard_list)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 删除该卡片组
                ReviewCardManager.getInstance().deleteTopicReviewSetsByTopic(topic);
                initReviewListViews();
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.add_new_quizcard)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 进入新增卡片界面
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), AddReviewCardActivity.class)
                .putString(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC, topic);
                ActivityUtils.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.add_to_favorite_list)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 收藏
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

}
