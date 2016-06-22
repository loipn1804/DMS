package com.dms.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.R;
import com.dms.adapter.CaseAdapter;
import com.dms.adapter.ListAdapter;
import com.dms.adapter.MultichoiceAdapter;
import com.dms.daocontroller.BlockController;
import com.dms.daocontroller.CaseController;
import com.dms.daocontroller.CaseImageController;
import com.dms.daocontroller.CaseSubCategoryController;
import com.dms.daocontroller.CategoryController;
import com.dms.daocontroller.DormitoryCategoryController;
import com.dms.daocontroller.DormitoryController;
import com.dms.daocontroller.StaffController;
import com.dms.daocontroller.StatusController;
import com.dms.daocontroller.SubCategoryController;
import com.dms.daocontroller.UserController;
import com.dms.interfaces.ItemListCallback;
import com.dms.interfaces.MultichoiceCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.volleycontroller.CaseVolley;
import com.dms.volleycontroller.DormitoryVolley;
import com.dms.volleycontroller.callback.CaseCallback;
import com.dms.volleycontroller.callback.DormitoryCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import greendao.Block;
import greendao.CaseImage;
import greendao.CaseObj;
import greendao.CaseSubCategory;
import greendao.Category;
import greendao.Dormitory;
import greendao.DormitoryCategory;
import greendao.Staff;
import greendao.Status;
import greendao.SubCategory;
import greendao.User;

/**
 * Created by USER on 1/15/2016.
 */
public class CaseDetailActivity extends BaseActivity implements View.OnClickListener {

    private int CAMERA_REQUEST = 123;
    private int PICK_IMAGE_REQUEST = 124;

    private String FOLDER = "DMS";
    private String FILENAME = "";
    private String FILETYPE = ".png";

    private RelativeLayout rltBack;
    private TextView txtSave;

    private RelativeLayout rltDormitory;
    private RelativeLayout rltBlock;
    private RelativeLayout rltCategory;
    private RelativeLayout rltStatus;
    private RelativeLayout rltAssignTo;
    private TextView txtDormitory;
    private TextView txtBlock;
    private TextView txtCategory;
    private TextView txtCaseNo;
    private TextView txtStatus;
    private View imvStatus;
    private TextView txtCreatedBy;
    private TextView txtAssignTo;
    private TextView txtContent;
    private LinearLayout lnlSubCategory;

    private HorizontalScrollView crollViewImage;
    private LinearLayout lnlImage;

    private List<Uri> listImageLocal;

    private long case_id;
    private CaseObj case_obj;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        if (getIntent().hasExtra("case_id")) {
            case_id = getIntent().getLongExtra("case_id", 0);
            case_obj = CaseController.getById(this, case_id);
            if (case_obj == null) {
                finish();
            }
        } else {
            finish();
        }

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.color.white)
                .showImageOnLoading(R.color.white)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtSave = (TextView) findViewById(R.id.txtSave);
        rltDormitory = (RelativeLayout) findViewById(R.id.rltDormitory);
        rltBlock = (RelativeLayout) findViewById(R.id.rltBlock);
        rltCategory = (RelativeLayout) findViewById(R.id.rltCategory);
        rltStatus = (RelativeLayout) findViewById(R.id.rltStatus);
        rltAssignTo = (RelativeLayout) findViewById(R.id.rltAssignTo);
        txtDormitory = (TextView) findViewById(R.id.txtDormitory);
        txtBlock = (TextView) findViewById(R.id.txtBlock);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtCaseNo = (TextView) findViewById(R.id.txtCaseNo);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        imvStatus = findViewById(R.id.imvStatus);
        txtCreatedBy = (TextView) findViewById(R.id.txtCreatedBy);
        txtAssignTo = (TextView) findViewById(R.id.txtAssignTo);
        txtContent = (TextView) findViewById(R.id.txtContent);
        crollViewImage = (HorizontalScrollView) findViewById(R.id.crollViewImage);
        lnlImage = (LinearLayout) findViewById(R.id.lnlImage);
        lnlSubCategory = (LinearLayout) findViewById(R.id.lnlSubCategory);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        rltDormitory.setOnClickListener(this);
        rltBlock.setOnClickListener(this);
        rltCategory.setOnClickListener(this);
        rltStatus.setOnClickListener(this);
        rltAssignTo.setOnClickListener(this);
    }

    private void initData() {
        listImageLocal = new ArrayList<>();

        getCaseDetail();

        String dormitory_name = "";
        Dormitory dormitory = DormitoryController.getById(this, case_obj.getDormitory_id());
        if (dormitory != null) {
            dormitory_name = dormitory.getName();
        }
        txtDormitory.setText(dormitory_name);

        String block_name = "";
        Block block = BlockController.getById(this, case_obj.getBlock_id());
        if (block != null) {
            block_name = block.getName();
        }
        txtBlock.setText(block_name);

        String category_name = "";
        Category category = CategoryController.getById(this, case_obj.getCategory_id());
        if (category != null) {
            category_name = category.getName();
        }
        txtCategory.setText(category_name);

        txtCaseNo.setText("#" + case_obj.getId());

        String status_name = "";
        Status status = StatusController.getById(this, case_obj.getCase_status_id());
        if (status != null) {
            status_name = status.getName();
        }
        txtStatus.setText(status_name);

        if (case_obj.getCase_status_id() == 1) {
            imvStatus.setBackgroundResource(R.drawable.circle_status_live);
        } else if (case_obj.getCase_status_id() == 2) {
            imvStatus.setBackgroundResource(R.drawable.circle_status_progress);
        } else if (case_obj.getCase_status_id() == 3) {
            imvStatus.setBackgroundResource(R.drawable.circle_status_approval);
        } else if (case_obj.getCase_status_id() == 4) {
            imvStatus.setBackgroundResource(R.drawable.circle_status_closed);
        }

        txtCreatedBy.setText(case_obj.getCreate_by_name());

        String assign_to_name = case_obj.getAssign_to_name();
        if (assign_to_name.length() == 0) {
            txtAssignTo.setText("Not Assigned");
        } else {
            txtAssignTo.setText(assign_to_name);
        }

        txtContent.setText(case_obj.getContent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                updateContent(txtContent.getText().toString().trim());
                break;
            case R.id.rltDormitory:
                showPopupListDormitory();
                break;
            case R.id.rltBlock:
                Dormitory dormitory = DormitoryController.getById(this, case_obj.getDormitory_id());
                if (dormitory != null) {
                    showPopupListBlock(dormitory);
                }
                break;
            case R.id.rltCategory:
                Dormitory dormitory2 = DormitoryController.getById(this, case_obj.getDormitory_id());
                if (dormitory2 != null) {
                    Block block2 = BlockController.getById(this, case_obj.getBlock_id());
                    if (block2 != null) {
                        showPopupListCategory(dormitory2, block2);
                    }
                }
                break;
            case R.id.rltStatus:
                showPopupListStatus();
                break;
            case R.id.rltAssignTo:
                getListStaff();
                break;
        }
    }

    private void getCaseDetail() {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.getCaseDetail(new CaseCallback.GetCaseDetailCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    setListImageLive(false);
                    setListSubCategory();

                    getDormitoryCategory();

//                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id + "");
    }

    private void getDormitoryCategory() {
        DormitoryVolley dormitoryVolley = new DormitoryVolley(this);
        dormitoryVolley.getDormitoryCategory(new DormitoryCallback.GetDormitoryCategoryCallback() {
            @Override
            public void onSuccess(boolean success, String result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void updateCase(final Dormitory dormitory, final Block block, final Category category, List<SubCategory> listSubCategory) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.updateCase(new CaseCallback.UpdateCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setDormitory_id(dormitory.getId());
                    case_obj.setBlock_id(block.getId());
                    case_obj.setCategory_id(category.getId());
                    CaseController.insertOrUpdate(CaseDetailActivity.this, case_obj);

                    String dormitory_name = "";
                    Dormitory dormitory = DormitoryController.getById(CaseDetailActivity.this, case_obj.getDormitory_id());
                    if (dormitory != null) {
                        dormitory_name = dormitory.getName();
                    }
                    txtDormitory.setText(dormitory_name);

                    String block_name = "";
                    Block block = BlockController.getById(CaseDetailActivity.this, case_obj.getBlock_id());
                    if (block != null) {
                        block_name = block.getName();
                    }
                    txtBlock.setText(block_name);

                    String category_name = "";
                    Category category = CategoryController.getById(CaseDetailActivity.this, case_obj.getCategory_id());
                    if (category != null) {
                        category_name = category.getName();
                    }
                    txtCategory.setText(category_name);
                    setListSubCategory();

                    StaticFunction.sendBroadCast(CaseDetailActivity.this, BroadcastValue.CASE_NOTIFY_LIST);

//                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_obj, dormitory, block, category, listSubCategory);
    }

    private void changeStatus(final long case_status_id) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.changeStatus(new CaseCallback.ChangeStatusCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setCase_status_id(case_status_id);
                    CaseController.insertOrUpdate(CaseDetailActivity.this, case_obj);

                    String status_name = "";
                    Status status = StatusController.getById(CaseDetailActivity.this, case_obj.getCase_status_id());
                    if (status != null) {
                        status_name = status.getName();
                    }
                    txtStatus.setText(status_name);

                    if (case_obj.getCase_status_id() == 1) {
                        imvStatus.setBackgroundResource(R.drawable.circle_status_live);
                    } else if (case_obj.getCase_status_id() == 2) {
                        imvStatus.setBackgroundResource(R.drawable.circle_status_progress);
                    } else if (case_obj.getCase_status_id() == 3) {
                        imvStatus.setBackgroundResource(R.drawable.circle_status_approval);
                    } else if (case_obj.getCase_status_id() == 4) {
                        imvStatus.setBackgroundResource(R.drawable.circle_status_closed);
                    }

                    StaticFunction.sendBroadCast(CaseDetailActivity.this, BroadcastValue.CASE_NOTIFY_LIST);

//                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id, case_status_id);
    }

    private void assign(final Staff assign_to) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.assign(new CaseCallback.AssignCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setAssign_by(UserController.getCurrentUser(CaseDetailActivity.this).getId());
                    case_obj.setAssign_to(assign_to.getId());
                    case_obj.setAssign_to_name(assign_to.getName());
                    CaseController.insertOrUpdate(CaseDetailActivity.this, case_obj);

                    String assign_to_name = case_obj.getAssign_to_name();
                    if (assign_to_name.length() == 0) {
                        txtAssignTo.setText("Not Assigned");
                    } else {
                        txtAssignTo.setText(assign_to_name);
                    }

                    StaticFunction.sendBroadCast(CaseDetailActivity.this, BroadcastValue.CASE_NOTIFY_LIST);

//                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id, UserController.getCurrentUser(this).getId(), assign_to.getId());
    }

    private void updateContent(final String content) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.updateContent(new CaseCallback.UpdateContentCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    case_obj.setContent(content);
                    CaseController.insertOrUpdate(CaseDetailActivity.this, case_obj);

                    txtContent.setText(case_obj.getContent());

                    StaticFunction.sendBroadCast(CaseDetailActivity.this, BroadcastValue.CASE_NOTIFY_LIST);

                    txtContent.clearFocus();

//                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id, content);
    }

    private void getListStaff() {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.getListStaff(new CaseCallback.GetListStaffCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    showPopupListStaff();
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        });
    }

    public void showPopupListDormitory() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Dormitory");

        List<String> list = new ArrayList<>();
        final List<Dormitory> listDormitory = DormitoryController.getAll(this);
        for (Dormitory dormitory : listDormitory) {
            list.add(dormitory.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
//                txtDormitory.setText(listDormitory.get(position).getName());
                showPopupListBlock(listDormitory.get(position));
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listDormitory.size() == 0) {
            showToastError("There is no dormitory");
            dialog.dismiss();
        }
    }

    public void showPopupListBlock(final Dormitory dormitory) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Block");

        List<String> list = new ArrayList<>();
        final List<Block> listBlock = BlockController.getByDormitoryId(this, dormitory.getId() + "");
        for (Block block : listBlock) {
            list.add(block.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
//                txtBlock.setText(listBlock.get(position).getName());
                showPopupListCategory(dormitory, listBlock.get(position));
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listBlock.size() == 0) {
            showToastError("There is no block of " + dormitory.getName());
            dialog.dismiss();
        }
    }

    public void showPopupListCategory(final Dormitory dormitory, final Block block) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Category");

        List<String> list = new ArrayList<>();
        final List<Category> listCategory = new ArrayList<>();
        List<DormitoryCategory> listDormitoryCategory = DormitoryCategoryController.getByDormitoryId(this, dormitory.getId() + "");
        for (DormitoryCategory dormitoryCategory : listDormitoryCategory) {
            Category category = CategoryController.getById(this, dormitoryCategory.getCategory_id());
            if (category != null) {
                listCategory.add(category);
                list.add(category.getName());
            }
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
//                txtCategory.setText(listCategory.get(position).getName());
                showPopupListSubCategory(dormitory, block, listCategory.get(position));
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listCategory.size() == 0) {
            showToastError("There is no category of " + dormitory.getName());
            dialog.dismiss();
        }
    }

    public void showPopupListSubCategory(final Dormitory dormitory, final Block block, final Category category) {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_multichoice_list);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        ListView lvData = (ListView) dialog.findViewById(R.id.lvData);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtTitle.setText("SubCategory");

        List<String> list = new ArrayList<>();
        final List<SubCategory> listSubCategory = SubCategoryController.getByCategoryId(this, category.getId() + "");
        for (SubCategory subCategory : listSubCategory) {
            list.add(subCategory.getName());
        }

        final List<SubCategory> listChecked = new ArrayList<>();

        MultichoiceCallback multichoiceCallback = new MultichoiceCallback() {
            @Override
            public void checkedChange(int position, boolean isChecked) {
                if (isChecked) {
                    listChecked.add(listSubCategory.get(position));
                } else {
                    for (int i = 0; i < listChecked.size(); i++) {
                        if (listChecked.get(i).equals(listSubCategory.get(position))) {
                            listChecked.remove(i);
                        }
                    }
                }
            }
        };

        MultichoiceAdapter listAdapter = new MultichoiceAdapter(this, list, multichoiceCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listChecked.size() > 0) {
                    updateCase(dormitory, block, category, listChecked);
                    dialog.dismiss();
                } else {
                    showToastInfo("Please choose sub category");
                }
            }
        });

        dialog.show();

        if (listSubCategory.size() == 0) {
            showToastError("There is no sub category of " + category.getName());
            dialog.dismiss();
        }
    }

    public void showPopupListStatus() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

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

        User user = UserController.getCurrentUser(this);
        List<String> list = new ArrayList<>();
        final List<Status> listStatus = StatusController.getAll(this);
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
                changeStatus(listStatus.get(position).getId());
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listStatus.size() == 0) {
            showToastError("There is no status");
            dialog.dismiss();
        }
    }

    public void showPopupListStaff() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

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
        final List<Staff> listStaff = StaffController.getAll(this);
        for (Staff staff : listStaff) {
            list.add(staff.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                assign(listStaff.get(position));
                dialog.dismiss();
            }
        };

        ListAdapter listAdapter = new ListAdapter(this, list, itemListCallback);
        lvData.setAdapter(listAdapter);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        if (listStaff.size() == 0) {
            showToastError("There is no staff");
            dialog.dismiss();
        }
    }

    private void setListSubCategory() {
        final List<CaseSubCategory> list = CaseSubCategoryController.getAll(this);
        int size = list.size();

        lnlSubCategory.removeAllViews();

        for (int i = 0; i < size; i++) {
            final int j = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_subcategory, null);
            TextView txtSubCategory = (TextView) viewRow.findViewById(R.id.txtSubCategory);

            txtSubCategory.setText(list.get(j).getName());

            lnlSubCategory.addView(viewRow);
        }
    }

    private void setListSubCategoryLocal(List<SubCategory> list) {
        int size = list.size();

        lnlSubCategory.removeAllViews();

        for (int i = 0; i < size; i++) {
            final int j = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_subcategory, null);
            TextView txtSubCategory = (TextView) viewRow.findViewById(R.id.txtSubCategory);

            txtSubCategory.setText(list.get(j).getName());

            lnlSubCategory.addView(viewRow);
        }
    }

    private void setListImageLive(boolean isScroll) {
        final List<CaseImage> list = CaseImageController.getAll(this);
        int size = list.size();

        lnlImage.removeAllViews();

        for (int i = 0; i < size; i++) {
            final int j = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_image, null);
            ImageView imv = (ImageView) viewRow.findViewById(R.id.imv);
            ImageView imvDelete = (ImageView) viewRow.findViewById(R.id.imvDelete);

            imageLoader.displayImage(StaticFunction.BASE_URL + list.get(j).getPath(), imv, options);

            imvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeImage(list.get(j).getId());
                }
            });

            lnlImage.addView(viewRow);
        }

        LayoutInflater inflaterAdd = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewAdd = inflaterAdd.inflate(R.layout.view_add_image, null);
        RelativeLayout rltAdd = (RelativeLayout) viewAdd.findViewById(R.id.rltAdd);
        TextView txtLabelAdd = (TextView) viewAdd.findViewById(R.id.txtLabelAdd);
        if (listImageLocal.size() == 0) {
            txtLabelAdd.setText("Add image");
        } else {
            txtLabelAdd.setText("Add more");
        }
        rltAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupPickImage();
            }
        });

        lnlImage.addView(viewAdd);

        if (isScroll) {
            crollViewImage.postDelayed(new Runnable() {
                public void run() {
                    crollViewImage.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100);
        }
    }

    public void showPopupPickImage() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_pick_image);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        LinearLayout lnlCamera = (LinearLayout) dialog.findViewById(R.id.lnlCamera);
        LinearLayout lnlGallery = (LinearLayout) dialog.findViewById(R.id.lnlGallery);

        lnlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });

        lnlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                dialog.dismiss();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCamera() {
        FILENAME = FOLDER + "_" + Settings.Secure.ANDROID_ID + "_" + System.currentTimeMillis() + FILETYPE;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createUriForCamera());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {

        }
    }

    private Uri createUriForCamera() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    private Uri getFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);
        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
//                listImage.add(getFileFromURI(uri));
//                listImageLocal.add(uri);
//                setListImageLocal(true);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    addImage(bitmap);
                } catch (Exception e) {

                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = getFile();
            if (uri != null) {
//                listImageLocal.add(uri);
//                setListImageLocal(true);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    addImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    }

    private void removeImage(long image_id) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.removeImage(new CaseCallback.RemoveImageCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    setListImageLive(false);
                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id + "", image_id);
    }

    private void addImage(Bitmap bitmap) {
        showProgressDialog(true);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.addImage(new CaseCallback.AddImageCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    setListImageLive(false);
                    showToastOk(result);
                } else {
                    showToastError(result);
                }
                hideProgressDialog();
            }

            @Override
            public void onError(String error) {
                showToastError(error);
                hideProgressDialog();
            }
        }, case_id, bitmap);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("onTrimMemory", level + "");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("onLowMemory", "onLowMemory");
    }
}