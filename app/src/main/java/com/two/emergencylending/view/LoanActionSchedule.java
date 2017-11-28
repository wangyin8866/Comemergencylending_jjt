package com.two.emergencylending.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

public class LoanActionSchedule extends LinearLayout implements View.OnClickListener {

    private ImageView iv_unit_infor, wait_review_arrow, iv_unit_infor_next, iv_contact_icon, iv_personal_arrow,
            iv_contact_to_author, iv_anthor_infor, iv_anthor_to_end, iv_end_process, iv_contact_to_next;//,by_lending_arrow;
    private TextView tv_unit_info_text, tv_contact_text, tv_end, tv_anthor_info;
    public static final int EDIT_PERSONAL_STATUS = 0;
    public static final int EDIT_JOB_STATUS = 1;
    public static final int EDIT_CONTACT_STATUS = 2;
    public static final int EDIT_AUTHOR_STATUS = 3;
    public static final int END_STATUS = 4;
    private int status = EDIT_PERSONAL_STATUS;
    private onClickItemListener listener;


    private LinearLayout ll_pesonal, ll_job, ll_contact, ll_info_author, ll_end;

    public LoanActionSchedule(Context context) {
        this(context,null);
    }

    public LoanActionSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setGravity(Gravity.CENTER);
        super.setOrientation(HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_loan_action_schedule, this);
        iv_personal_arrow = (ImageView) view.findViewById(R.id.iv_personal_arrow);


        iv_unit_infor = (ImageView) view.findViewById(R.id.iv_unit_infor);
        tv_unit_info_text = (TextView) view.findViewById(R.id.tv_unit_info_text);
        iv_unit_infor_next = (ImageView) view.findViewById(R.id.iv_unit_infor_next);

        iv_contact_icon = (ImageView) view.findViewById(R.id.iv_contact_icon);
        tv_contact_text = (TextView) view.findViewById(R.id.tv_contact_text);
        iv_contact_to_next = (ImageView) view.findViewById(R.id.iv_contact_to_next);


        iv_anthor_infor = (ImageView) view.findViewById(R.id.iv_anthor_infor);
        tv_anthor_info = (TextView) view.findViewById(R.id.tv_anthor_info);
        iv_anthor_to_end = (ImageView) view.findViewById(R.id.iv_anthor_to_end);

        iv_end_process = (ImageView) view.findViewById(R.id.iv_end_process);
        tv_end = (TextView) view.findViewById(R.id.tv_end);

        ll_pesonal = (LinearLayout) view.findViewById(R.id.ll_pesonal);
        ll_job = (LinearLayout) view.findViewById(R.id.ll_job);
        ll_contact = (LinearLayout) view.findViewById(R.id.ll_contact);
        ll_info_author = (LinearLayout) view.findViewById(R.id.ll_info_author);
        ll_end = (LinearLayout) view.findViewById(R.id.ll_end);
        
        ll_pesonal.setOnClickListener(this);
        ll_job.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        ll_info_author.setOnClickListener(this);
        ll_end.setOnClickListener(this);
        
    }

    public LoanActionSchedule(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setStatus(int status) {
        this.status = status;
        refreshViews();
    }

    private void refreshViews() {
        switch (status) {
            case EDIT_PERSONAL_STATUS:
                iv_unit_infor.setImageResource(R.drawable.icon_company);
                tv_unit_info_text.setTextColor(Color.parseColor("#999999"));
                iv_unit_infor_next.setImageResource(R.drawable.icon_next);

                iv_contact_icon.setImageResource(R.drawable.icon_contacts);
                tv_contact_text.setTextColor(Color.parseColor("#999999"));
                iv_contact_to_next.setImageResource(R.drawable.icon_next);


                iv_anthor_infor.setImageResource(R.drawable.icon_verifications);
                tv_anthor_info.setTextColor(Color.parseColor("#999999"));
                iv_anthor_to_end.setImageResource(R.drawable.icon_next);

                iv_end_process.setImageResource(R.drawable.icon_over);
                tv_end.setTextColor(Color.parseColor("#999999"));
                break;
            case EDIT_JOB_STATUS:
                iv_unit_infor.setImageResource(R.drawable.icon_companyh);
                tv_unit_info_text.setTextColor(Color.parseColor("#333333"));
                iv_unit_infor_next.setImageResource(R.drawable.icon_nexthh);


                iv_contact_icon.setImageResource(R.drawable.icon_contacts);
                tv_contact_text.setTextColor(Color.parseColor("#999999"));
                iv_contact_to_next.setImageResource(R.drawable.icon_next);


                iv_anthor_infor.setImageResource(R.drawable.icon_verifications);
                tv_anthor_info.setTextColor(Color.parseColor("#999999"));
                iv_anthor_to_end.setImageResource(R.drawable.icon_next);

                iv_end_process.setImageResource(R.drawable.icon_over);
                tv_end.setTextColor(Color.parseColor("#999999"));

                break;
            case EDIT_CONTACT_STATUS:
                iv_unit_infor.setImageResource(R.drawable.icon_companyh);
                tv_unit_info_text.setTextColor(Color.parseColor("#333333"));
                iv_unit_infor_next.setImageResource(R.drawable.icon_nexthh);

                iv_contact_icon.setImageResource(R.drawable.icon_contactsh);
                tv_contact_text.setTextColor(Color.parseColor("#333333"));
                iv_contact_to_next.setImageResource(R.drawable.icon_nexthh);


                iv_anthor_infor.setImageResource(R.drawable.icon_verifications);
                tv_anthor_info.setTextColor(Color.parseColor("#999999"));
                iv_anthor_to_end.setImageResource(R.drawable.icon_next);

                iv_end_process.setImageResource(R.drawable.icon_over);
                tv_end.setTextColor(Color.parseColor("#999999"));

                break;
            case EDIT_AUTHOR_STATUS:

                iv_unit_infor.setImageResource(R.drawable.icon_companyh);
                tv_unit_info_text.setTextColor(Color.parseColor("#333333"));
                iv_unit_infor_next.setImageResource(R.drawable.icon_nexthh);

                iv_contact_icon.setImageResource(R.drawable.icon_contactsh);
                tv_contact_text.setTextColor(Color.parseColor("#333333"));
                iv_contact_to_next.setImageResource(R.drawable.icon_nexthh);


                iv_anthor_infor.setImageResource(R.drawable.icon_verifivationh);
                tv_anthor_info.setTextColor(Color.parseColor("#333333"));
                iv_anthor_to_end.setImageResource(R.drawable.icon_nexthh);

                iv_end_process.setImageResource(R.drawable.icon_over);
                tv_end.setTextColor(Color.parseColor("#999999"));

                break;
            case END_STATUS:

                iv_unit_infor.setImageResource(R.drawable.icon_companyh);
                tv_unit_info_text.setTextColor(Color.parseColor("#333333"));
                iv_unit_infor_next.setImageResource(R.drawable.icon_nexthh);

                iv_contact_icon.setImageResource(R.drawable.icon_contactsh);
                tv_contact_text.setTextColor(Color.parseColor("#333333"));
                iv_contact_to_next.setImageResource(R.drawable.icon_nexthh);


                iv_anthor_infor.setImageResource(R.drawable.icon_verifivationh);
                tv_anthor_info.setTextColor(Color.parseColor("#333333"));
                iv_anthor_to_end.setImageResource(R.drawable.icon_nexthh);

                iv_end_process.setImageResource(R.drawable.icon_overh);
                tv_end.setTextColor(Color.parseColor("#333333"));
                break;
            default:
                break;

        }
    }

    public void setArrow(int v) {
        iv_personal_arrow.setVisibility(v);
        iv_unit_infor_next.setVisibility(v);
        iv_contact_to_next.setVisibility(v);
        iv_anthor_to_end.setVisibility(v);
        ll_info_author.setVisibility(v);
        ll_end.setVisibility(v);
    }

    @Override
    public void onClick(View view) {
        int item=-1;
        switch (view.getId()){
            case R.id.ll_pesonal:
                item = EDIT_PERSONAL_STATUS;
                break;
            case R.id.ll_job:
                item = EDIT_JOB_STATUS;
                break;
            case R.id.ll_contact:
                item = EDIT_CONTACT_STATUS;
                break;
            case R.id.ll_info_author:
                item = EDIT_AUTHOR_STATUS;
                break;
            case R.id.ll_end:
                item = END_STATUS;
                break;
            
        }
        if (item !=-1&&listener!=null){
            listener.onItemsClick(item);
        }
    }

    public void setOnClikItemListener(onClickItemListener listener){
        this.listener = listener;
    }
    
    
    public interface onClickItemListener{
        void onItemsClick(int item);
    }
}
