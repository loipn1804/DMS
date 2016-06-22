package com.dms.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.activity.CaseDetailActivity;
import com.dms.daocontroller.CaseController;
import com.dms.daocontroller.CategoryController;
import com.dms.daocontroller.DormitoryController;
import com.dms.daocontroller.StaffController;
import com.dms.daocontroller.StatusController;
import com.dms.daocontroller.UserController;
import com.dms.interfaces.ItemListCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.view.PopupUpdateCase;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.callback.CaseCallback;

import java.util.ArrayList;
import java.util.List;

import greendao.CaseObj;
import greendao.Category;
import greendao.Dormitory;
import greendao.Staff;
import greendao.Status;
import greendao.User;

/**
 * Created by USER on 1/14/2016.
 */
public class CaseAdapter extends BaseAdapter implements PopupUpdateCase.PopupUpdateCaseCallback {

    public interface ScrollCallback {
        void scrollUp(int scrollHeight);
    }

    private List<CaseObj> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private int color_live;
    private int color_progress;
    private int color_approval;
    private int color_closed;

    private int screenHeight;
    private int popupHeight;

    private PopupUpdateCase popupUpdateCase;
    private ScrollCallback scrollCallback;

    public CaseAdapter(Activity activity, List<CaseObj> listData, ScrollCallback scrollCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.scrollCallback = scrollCallback;

        color_live = activity.getResources().getColor(R.color.case_live_bg);
        color_progress = activity.getResources().getColor(R.color.case_progress_bg);
        color_approval = activity.getResources().getColor(R.color.case_approval_bg);
        color_closed = activity.getResources().getColor(R.color.case_closed_bg);

        screenHeight = StaticFunction.getScreenHeight(activity);
        popupHeight = ((int) activity.getResources().getDimension(R.dimen.dm_50dp)) * 3;
    }

    public void setListData(List<CaseObj> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    private PopupUpdateCase getPopupUpdateCase() {
        if (popupUpdateCase == null) {
            popupUpdateCase = new PopupUpdateCase(activity, this);
        }
        return popupUpdateCase;
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_case, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltBackground = (RelativeLayout) convertView.findViewById(R.id.rltBackground);
            holder.imvBackground = (ImageView) convertView.findViewById(R.id.imvBackground);
            holder.viewBottom = convertView.findViewById(R.id.viewBottom);

            holder.txtDormitory = (TextView) convertView.findViewById(R.id.txtDormitory);
            holder.txtAssignName = (TextView) convertView.findViewById(R.id.txtAssignName);
            holder.txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
            holder.txtID = (TextView) convertView.findViewById(R.id.txtID);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.imvMenu = (ImageView) convertView.findViewById(R.id.imvMenu);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == getCount() - 1) {
            holder.viewBottom.setVisibility(View.VISIBLE);
        } else {
            holder.viewBottom.setVisibility(View.GONE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CaseDetailActivity.class);
                intent.putExtra("case_id", listData.get(position).getId());
                activity.startActivity(intent);
            }
        });

        if (listData.get(position).getCase_status_id() == 1) {
            holder.rltBackground.setBackgroundResource(R.drawable.bg_case_live);
            holder.txtAssignName.setTextColor(color_live);
            holder.imvBackground.setImageResource(R.drawable.case_live_hint);
        } else if (listData.get(position).getCase_status_id() == 2) {
            holder.rltBackground.setBackgroundResource(R.drawable.bg_case_progress);
            holder.txtAssignName.setTextColor(color_progress);
            holder.imvBackground.setImageResource(R.drawable.case_progress_hint);
        } else if (listData.get(position).getCase_status_id() == 3) {
            holder.rltBackground.setBackgroundResource(R.drawable.bg_case_approval);
            holder.txtAssignName.setTextColor(color_approval);
            holder.imvBackground.setImageResource(R.drawable.case_approval_hint);
        } else {
            holder.rltBackground.setBackgroundResource(R.drawable.bg_case_closed);
            holder.txtAssignName.setTextColor(color_closed);
            holder.imvBackground.setImageResource(R.drawable.case_closed_hint);
        }

        final ImageView imvMenu = holder.imvMenu;
        holder.imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                imvMenu.getLocationOnScreen(location);
                int scrollHeight = location[1] + popupHeight - screenHeight;
                if (scrollHeight > 0) {
                    scrollCallback.scrollUp(scrollHeight);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPopupUpdateCase().dismiss();
                            getPopupUpdateCase().setPosition(position);
                            getPopupUpdateCase().setAnchor(imvMenu);
                        }
                    }, 300);
                } else {
                    getPopupUpdateCase().dismiss();
                    getPopupUpdateCase().setPosition(position);
                    getPopupUpdateCase().setAnchor(imvMenu);
                }
            }
        });

        String dormitory_name = "";
        Dormitory dormitory = DormitoryController.getById(activity, listData.get(position).getDormitory_id());
        if (dormitory != null) {
            dormitory_name = dormitory.getName();
        }
        holder.txtDormitory.setText(dormitory_name);

        String category_name = "";
        Category category = CategoryController.getById(activity, listData.get(position).getCategory_id());
        if (category != null) {
            category_name = category.getName();
        }
        holder.txtCategory.setText(category_name);

        holder.txtID.setText("#" + listData.get(position).getId());
        holder.txtDesc.setText(listData.get(position).getContent());
        String assign_to_name = listData.get(position).getAssign_to_name();
        if (assign_to_name.length() == 0) {
            holder.txtAssignName.setText("Not Assigned");
        } else {
            holder.txtAssignName.setText(assign_to_name);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        RelativeLayout rltBackground;
        ImageView imvBackground;
        View viewBottom;

        TextView txtDormitory;
        TextView txtAssignName;
        TextView txtCategory;
        TextView txtID;
        TextView txtDesc;
        ImageView imvMenu;
    }

    @Override
    public void onChangeStatus(int position) {
//        ((BaseActivity) activity).showToastInfo("change status " + position);
        showPopupListStatus(listData.get(position));
    }

    @Override
    public void onAssign(int position) {
//        ((BaseActivity) activity).showToastInfo("assign " + position);
        getListStaff(listData.get(position));
    }

    public void showPopupListStatus(final CaseObj case_obj) {
        // custom dialog
        final Dialog dialog = new Dialog(activity);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Status");

        User user = UserController.getCurrentUser(activity);
        List<String> list = new ArrayList<>();
        final List<Status> listStatus = StatusController.getAll(activity);
        for (Status status : listStatus) {
            if (user.getRole_id() == 3 && status.getId() == 4) {
                listStatus.remove(status);
            } else {
                list.add(status.getName());
            }
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                changeStatus(case_obj, listStatus.get(position).getId());
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(activity, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listStatus.size() == 0) {
            ((BaseActivity) activity).showToastError("There is no status");
            dialog.dismiss();
        }
    }

    private void changeStatus(final CaseObj case_obj, final long case_status_id) {
        ((BaseActivity) activity).showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(activity);
        caseVolley.changeStatus(new CaseCallback.ChangeStatusCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setCase_status_id(case_status_id);
                    CaseController.insertOrUpdate(activity, case_obj);

                    StaticFunction.sendBroadCast(activity, BroadcastValue.CASE_NOTIFY_LIST);

                    getPopupUpdateCase().dismiss();

//                    showToastOk(result);
                } else {
                    ((BaseActivity) activity).showToastError(result);
                }
                ((BaseActivity) activity).hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) activity).showToastError(error);
                ((BaseActivity) activity).hideProgressDialog();
            }
        }, case_obj.getId(), case_status_id);
    }

    private void getListStaff(final CaseObj case_obj) {
        ((BaseActivity) activity).showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(activity);
        caseVolley.getListStaff(new CaseCallback.GetListStaffCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    showPopupListStaff(case_obj);
                } else {
                    ((BaseActivity) activity).showToastError(result);
                }
                ((BaseActivity) activity).hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) activity).showToastError(error);
                ((BaseActivity) activity).hideProgressDialog();
            }
        });
    }

    public void showPopupListStaff(final CaseObj case_obj) {
        // custom dialog
        final Dialog dialog = new Dialog(activity);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Status");

        List<String> list = new ArrayList<>();
        final List<Staff> listStaff = StaffController.getAll(activity);
        for (Staff staff : listStaff) {
            list.add(staff.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                assign(case_obj, listStaff.get(position));
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(activity, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listStaff.size() == 0) {
            ((BaseActivity) activity).showToastError("There is no staff");
            dialog.dismiss();
        }
    }

    private void assign(final CaseObj case_obj, final Staff assign_to) {
        ((BaseActivity) activity).showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(activity);
        caseVolley.assign(new CaseCallback.AssignCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setAssign_by(UserController.getCurrentUser(activity).getId());
                    case_obj.setAssign_to(assign_to.getId());
                    case_obj.setAssign_to_name(assign_to.getName());
                    CaseController.insertOrUpdate(activity, case_obj);

                    StaticFunction.sendBroadCast(activity, BroadcastValue.CASE_NOTIFY_LIST);

                    getPopupUpdateCase().dismiss();

//                    showToastOk(result);
                } else {
                    ((BaseActivity) activity).showToastError(result);
                }
                ((BaseActivity) activity).hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) activity).showToastError(error);
                ((BaseActivity) activity).hideProgressDialog();
            }
        }, case_obj.getId(), UserController.getCurrentUser(activity).getId(), assign_to.getId());
    }
}
