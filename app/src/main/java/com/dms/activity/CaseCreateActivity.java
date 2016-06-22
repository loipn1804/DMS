package com.dms.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.adapter.ListAdapter;
import com.dms.adapter.MultichoiceAdapter;
import com.dms.daocontroller.BlockController;
import com.dms.daocontroller.CaseController;
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

import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import greendao.Block;
import greendao.CaseObj;
import greendao.Category;
import greendao.Dormitory;
import greendao.DormitoryCategory;
import greendao.Staff;
import greendao.Status;
import greendao.SubCategory;

/**
 * Created by USER on 2/23/2016.
 */
public class CaseCreateActivity extends BaseActivity implements View.OnClickListener {

    private int CAMERA_REQUEST = 123;
    private int PICK_IMAGE_REQUEST = 124;

    private String FOLDER = "DMS";
    private String FILENAME = "";
    private String FILETYPE = ".png";

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

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

    private List<Uri> listImageLocalUri;
    private List<SubCategory> listSubCategory;

    private CaseObj case_obj;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        case_obj = new CaseObj();
        listSubCategory = new ArrayList<>();

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
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
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

        txtNameActionBar.setText("Add Case");

        findViewById(R.id.lnlCaseNo).setVisibility(View.GONE);
        findViewById(R.id.lineCaseNo).setVisibility(View.GONE);
        findViewById(R.id.lnlStatus).setVisibility(View.GONE);
        findViewById(R.id.lineStatus).setVisibility(View.GONE);
        findViewById(R.id.lnlCreatedBy).setVisibility(View.GONE);
        findViewById(R.id.lineCreatedBy).setVisibility(View.GONE);
        findViewById(R.id.lnlAssignTo).setVisibility(View.GONE);
        findViewById(R.id.lineAssignTo).setVisibility(View.GONE);
    }

    private void initData() {
        listImageLocalUri = new ArrayList<>();
        setListImageLocal(true);

        getDormitoryCategory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                prepareCreateCase();
                break;
            case R.id.rltDormitory:
                showPopupListDormitory();
                break;
            case R.id.rltBlock:
                if (case_obj.getDormitory_id() != null) {
                    Dormitory dormitory = DormitoryController.getById(this, case_obj.getDormitory_id());
                    if (dormitory != null) {
                        showPopupListBlock(dormitory);
                    }
                } else {
                    showToastError(getString(R.string.choose_dormitory));
                }
                break;
            case R.id.rltCategory:
                if (case_obj.getDormitory_id() != null) {
                    Dormitory dormitory2 = DormitoryController.getById(this, case_obj.getDormitory_id());
                    if (dormitory2 != null) {
                        Block block2 = BlockController.getById(this, case_obj.getBlock_id());
                        if (block2 != null) {
                            showPopupListCategory(dormitory2, block2);
                        }
                    }
                } else {
                    showToastError(getString(R.string.choose_dormitory));
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

    private void prepareCreateCase() {
        if (case_obj.getDormitory_id() == null) {
            showToastError(getString(R.string.choose_dormitory));
        } else {
            String content = txtContent.getText().toString().trim();
            if (content.length() == 0) {
                showToastError(getString(R.string.blank_content));
            } else {
                case_obj.setContent(content);
                createCase();
            }
        }
    }

    private void createCase() {
        showProgressDialog(false);
        CaseVolley caseVolley = new CaseVolley(this);
        caseVolley.createCase(new CaseCallback.CreateCaseCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    StaticFunction.sendBroadCast(CaseCreateActivity.this, BroadcastValue.CASE_REFRESH_LIST);
                    finish();

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
        }, case_obj, listImageLocalUri, listSubCategory, UserController.getCurrentUser(this).getId() + "");
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
        final List<SubCategory> listSubCategoryLocal = SubCategoryController.getByCategoryId(this, category.getId() + "");
        for (SubCategory subCategory : listSubCategoryLocal) {
            list.add(subCategory.getName());
        }

        final List<SubCategory> listChecked = new ArrayList<>();

        MultichoiceCallback multichoiceCallback = new MultichoiceCallback() {
            @Override
            public void checkedChange(int position, boolean isChecked) {
                if (isChecked) {
                    listChecked.add(listSubCategoryLocal.get(position));
                } else {
                    for (int i = 0; i < listChecked.size(); i++) {
                        if (listChecked.get(i).equals(listSubCategoryLocal.get(position))) {
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
                    listSubCategory.clear();
                    listSubCategory.addAll(listChecked);
                    case_obj.setDormitory_id(dormitory.getId());
                    case_obj.setBlock_id(block.getId());
                    case_obj.setCategory_id(category.getId());
                    CaseController.insertOrUpdate(CaseCreateActivity.this, case_obj);

                    String dormitory_name = "";
                    Dormitory dormitory = DormitoryController.getById(CaseCreateActivity.this, case_obj.getDormitory_id());
                    if (dormitory != null) {
                        dormitory_name = dormitory.getName();
                    }
                    txtDormitory.setText(dormitory_name);

                    String block_name = "";
                    Block block = BlockController.getById(CaseCreateActivity.this, case_obj.getBlock_id());
                    if (block != null) {
                        block_name = block.getName();
                    }
                    txtBlock.setText(block_name);

                    String category_name = "";
                    Category category = CategoryController.getById(CaseCreateActivity.this, case_obj.getCategory_id());
                    if (category != null) {
                        category_name = category.getName();
                    }
                    txtCategory.setText(category_name);
                    setListSubCategoryLocal(listSubCategory);

                    dialog.dismiss();
                } else {
                    showToastInfo("Please choose sub category");
                }
            }
        });

        dialog.show();

        if (listSubCategoryLocal.size() == 0) {
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

        List<String> list = new ArrayList<>();
        final List<Status> listStatus = StatusController.getAll(this);
        for (Status status : listStatus) {
            list.add(status.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                case_obj.setCase_status_id(listStatus.get(position).getId());

                String status_name = "";
                Status status = StatusController.getById(CaseCreateActivity.this, case_obj.getCase_status_id());
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
                case_obj.setAssign_to(listStaff.get(position).getId());
                case_obj.setAssign_to_name(listStaff.get(position).getName());
                txtAssignTo.setText(listStaff.get(position).getName());
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

    private void setListImageLocal(boolean isScroll) {
        int size = listImageLocalUri.size();

        lnlImage.removeAllViews();

        for (int i = 0; i < size; i++) {
            final int j = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_image, null);
            ImageView imv = (ImageView) viewRow.findViewById(R.id.imv);
            ImageView imvDelete = (ImageView) viewRow.findViewById(R.id.imvDelete);

            imageLoader.displayImage(listImageLocalUri.get(j).toString(), imv, options);

            imvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listImageLocalUri.remove(j);
                    setListImageLocal(false);
                }
            });

            lnlImage.addView(viewRow);
        }

        LayoutInflater inflaterAdd = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewAdd = inflaterAdd.inflate(R.layout.view_add_image, null);
        RelativeLayout rltAdd = (RelativeLayout) viewAdd.findViewById(R.id.rltAdd);
        TextView txtLabelAdd = (TextView) viewAdd.findViewById(R.id.txtLabelAdd);
        if (listImageLocalUri.size() == 0) {
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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        parcelFileDescriptor.close();
        return image;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
//                listImageLocal.add(getFileFromURI(uri));
//                listImageLocal.add(uri);
//                setListImageLocal(true);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    listImageLocalUri.add(uri);
                    setListImageLocal(true);
                } catch (Exception e) {
                    e.toString();
                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = getFile();
            if (uri != null) {
//                listImageLocal.add(uri);
//                setListImageLocal(true);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    listImageLocalUri.add(uri);
                    setListImageLocal(true);
                } catch (Exception e) {
                    e.toString();
                }
            }
        }
    }

    public Uri getFileFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = MediaStore.Images.Media.query(this.getContentResolver(), contentUri, proj);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        File file = new File(cursor.getString(column_index));
        Uri uri = Uri.fromFile(file);
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
}
