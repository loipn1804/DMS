package com.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.interfaces.MenuCallback;
import com.dms.volleycontroller.DashboardVolley;
import com.dms.volleycontroller.callback.DashboardCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 1/14/2016.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;

    private TextView txtDormitory;
    private TextView txtUser;
    private TextView txtUserAdmin;
    private TextView txtUserUser;
    private TextView txtNumCase;
    private TextView txtCaseLive;
    private TextView txtCaseProgress;
    private TextView txtCaseApproval;
    private TextView txtCaseClosed;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);

        txtDormitory = (TextView) view.findViewById(R.id.txtDormitory);
        txtUser = (TextView) view.findViewById(R.id.txtUser);
        txtUserAdmin = (TextView) view.findViewById(R.id.txtUserAdmin);
        txtUserUser = (TextView) view.findViewById(R.id.txtUserUser);
        txtNumCase = (TextView) view.findViewById(R.id.txtNumCase);
        txtCaseLive = (TextView) view.findViewById(R.id.txtCaseLive);
        txtCaseProgress = (TextView) view.findViewById(R.id.txtCaseProgress);
        txtCaseApproval = (TextView) view.findViewById(R.id.txtCaseApproval);
        txtCaseClosed = (TextView) view.findViewById(R.id.txtCaseClosed);

        rltMenu.setOnClickListener(this);
    }

    private void initData() {
        getAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                menuCallback.OpenMenu();
                break;
        }
    }

    private void getAll() {
        ((BaseActivity) getActivity()).showProgressDialog(true);
        DashboardVolley dashboardVolley = new DashboardVolley(getActivity());
        dashboardVolley.getAll(new DashboardCallback.GetAllCallback() {
            @Override
            public void onSuccess(boolean success, String result, JSONObject data) {
                if (success) {
                    try {
                        int dormitory = data.getInt("dormitory");
                        JSONObject user = data.getJSONObject("user");
                        int super_admin = user.getInt("super_admin");
                        int admin = user.getInt("admin");
                        int staff = user.getInt("staff");
                        JSONObject caseObj = data.getJSONObject("case");
                        int New = caseObj.getInt("New");
                        int In_Progress = caseObj.getInt("In Progress");
                        int Approval = caseObj.getInt("Approval");
                        int Closed = caseObj.getInt("Closed");

                        txtDormitory.setText(dormitory + "");
                        txtUser.setText((super_admin + admin + staff) + "");
                        txtUserAdmin.setText((super_admin + admin) + "");
                        txtUserUser.setText(staff + "");
                        txtNumCase.setText((New + In_Progress + Approval + Closed) + "");
                        txtCaseLive.setText(New + "");
                        txtCaseProgress.setText(In_Progress + "");
                        txtCaseApproval.setText(Approval + "");
                        txtCaseClosed.setText(Closed + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((BaseActivity) getActivity()).showToastError(result);
                }
                ((BaseActivity) getActivity()).hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
            }
        });
    }
}
