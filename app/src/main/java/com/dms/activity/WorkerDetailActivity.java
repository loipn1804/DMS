package com.dms.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.adapter.ListAdapter;
import com.dms.daocontroller.BlockController;
import com.dms.daocontroller.DormitoryController;
import com.dms.daocontroller.LevelController;
import com.dms.daocontroller.NationalityController;
import com.dms.daocontroller.RoomController;
import com.dms.daocontroller.WorkerController;
import com.dms.interfaces.ItemListCallback;
import com.dms.staticfunction.BroadcastValue;
import com.dms.staticfunction.StaticFunction;
import com.dms.view.SignatureView;
import com.dms.volleycontroller.WorkerVolley;
import com.dms.volleycontroller.callback.WorkerCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import greendao.Block;
import greendao.Dormitory;
import greendao.Level;
import greendao.Nationality;
import greendao.Room;
import greendao.Worker;

/**
 * Created by USER on 2/26/2016.
 */
public class WorkerDetailActivity extends BaseActivity implements View.OnClickListener {

    private int CAMERA_REQUEST = 127;
    private int PICK_IMAGE_REQUEST = 128;

    private String FOLDER = "DMS";
    private String FILENAME = "";
    private String FILETYPE = ".png";

    private RelativeLayout rltBack;
    private TextView txtSave;
    private TextView txtNameActionBar;

    private EditText edtBarcode;
    private EditText edtName;
    private RelativeLayout rltDormitory;
    private TextView txtDormitory;
    private RelativeLayout rltBlock;
    private TextView txtBlock;
    private RelativeLayout rltLevel;
    private TextView txtLevel;
    private RelativeLayout rltRoom;
    private TextView txtRoom;
    private EditText edtUnitNo;
    private EditText edtCompany;
    private EditText edtWorkPermit;
    private EditText edtPassport;
    private RelativeLayout rltNationality;
    private TextView txtNationality;
    private RelativeLayout rltExpiry;
    private TextView txtExpiry;
    private RelativeLayout rltSex;
    private TextView txtSex;

    private ImageView imvWorker1;
    private ImageView imvWorker2;
    private ImageView imvWorker3;
    private ImageView imvEdit1;
    private ImageView imvEdit2;
    private ImageView imvEdit3;
    private LinearLayout lnlLogChange;
    private TextView txtLogChange;

    private Worker mWorker;
    private long worker_id;

    private int iChooseImage;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_detail);

        if (getIntent().hasExtra("worker_id")) {
            worker_id = getIntent().getLongExtra("worker_id", 0);
            mWorker = WorkerController.getById(this, worker_id);
            if (mWorker == null) {
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
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        edtBarcode = (EditText) findViewById(R.id.edtBarcode);
        edtName = (EditText) findViewById(R.id.edtName);
        rltDormitory = (RelativeLayout) findViewById(R.id.rltDormitory);
        txtDormitory = (TextView) findViewById(R.id.txtDormitory);
        rltBlock = (RelativeLayout) findViewById(R.id.rltBlock);
        txtBlock = (TextView) findViewById(R.id.txtBlock);
        rltLevel = (RelativeLayout) findViewById(R.id.rltLevel);
        txtLevel = (TextView) findViewById(R.id.txtLevel);
        rltRoom = (RelativeLayout) findViewById(R.id.rltRoom);
        txtRoom = (TextView) findViewById(R.id.txtRoom);
        edtUnitNo = (EditText) findViewById(R.id.edtUnitNo);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtWorkPermit = (EditText) findViewById(R.id.edtWorkPermit);
        edtPassport = (EditText) findViewById(R.id.edtPassport);
        rltNationality = (RelativeLayout) findViewById(R.id.rltNationality);
        txtNationality = (TextView) findViewById(R.id.txtNationality);
        rltExpiry = (RelativeLayout) findViewById(R.id.rltExpiry);
        txtExpiry = (TextView) findViewById(R.id.txtExpiry);
        rltSex = (RelativeLayout) findViewById(R.id.rltSex);
        txtSex = (TextView) findViewById(R.id.txtSex);
        lnlLogChange = (LinearLayout) findViewById(R.id.lnlLogChange);
        txtLogChange = (TextView) findViewById(R.id.txtLogChange);

        imvWorker1 = (ImageView) findViewById(R.id.imvWorker1);
        imvWorker2 = (ImageView) findViewById(R.id.imvWorker2);
        imvWorker3 = (ImageView) findViewById(R.id.imvWorker3);
        imvEdit1 = (ImageView) findViewById(R.id.imvEdit1);
        imvEdit2 = (ImageView) findViewById(R.id.imvEdit2);
        imvEdit3 = (ImageView) findViewById(R.id.imvEdit3);

        rltDormitory.setOnClickListener(this);
        rltBlock.setOnClickListener(this);
        rltLevel.setOnClickListener(this);
        rltRoom.setOnClickListener(this);
        rltNationality.setOnClickListener(this);
        rltExpiry.setOnClickListener(this);
        rltSex.setOnClickListener(this);
        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        imvWorker1.setOnClickListener(this);
        imvWorker2.setOnClickListener(this);
        imvWorker3.setOnClickListener(this);
        imvEdit1.setOnClickListener(this);
        imvEdit2.setOnClickListener(this);
        imvEdit3.setOnClickListener(this);

        txtNameActionBar.setText("Worker Detail");
        edtBarcode.setEnabled(false);
    }

    private void initData() {
        edtBarcode.setText(mWorker.getBarcode());
        edtName.setText(mWorker.getName());
        edtUnitNo.setText(mWorker.getUnit_number());
        edtCompany.setText(mWorker.getCompany());
        edtWorkPermit.setText(mWorker.getWork_permit());
        edtPassport.setText(mWorker.getPassport());

        Dormitory dormitory = DormitoryController.getById(this, mWorker.getDormitory_id());
        if (dormitory != null) {
            txtDormitory.setText(dormitory.getName());
        }

        Block block = BlockController.getById(this, mWorker.getBlock_id());
        if (block != null) {
            txtBlock.setText(block.getName());
        }

        Level level = LevelController.getById(this, mWorker.getLevel_id());
        if (level != null) {
            txtLevel.setText(level.getName());
        }

        Room room = RoomController.getById(this, mWorker.getRoom_id());
        if (room != null) {
            txtRoom.setText(room.getName());
        }

        Nationality nationality = NationalityController.getByCode(WorkerDetailActivity.this, mWorker.getNationality_code());
        if (nationality != null) {
            txtNationality.setText(nationality.getName());
        }

        txtExpiry.setText(mWorker.getExpiry());

        txtSex.setText(mWorker.getSex() == 1 ? "Male" : "Female");

        imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_1(), imvWorker1, options);
        imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_2(), imvWorker2, options);
        imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_3(), imvWorker3, options);

        getLogChange();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltDormitory:
                showPopupListDormitory();
                break;
            case R.id.rltBlock:
                if (mWorker.getDormitory_id() != null) {
                    showPopupListBlock();
                } else {
                    showToastError(getString(R.string.choose_dormitory));
                }
                break;
            case R.id.rltLevel:
                if (mWorker.getBlock_id() != null) {
                    showPopupListLevel();
                } else {
                    showToastError(getString(R.string.choose_block));
                }
                break;
            case R.id.rltRoom:
                if (mWorker.getLevel_id() != null) {
                    showPopupListRoom();
                } else {
                    showToastError(getString(R.string.choose_level));
                }
                break;
            case R.id.rltNationality:
                showPopupListNationality();
                break;
            case R.id.rltExpiry:
                showDatePicker();
                break;
            case R.id.rltSex:
                showPopupListSex();
                break;
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                prepareUpdateWorker();
                break;
            case R.id.imvWorker1:

                break;
            case R.id.imvWorker2:

                break;
            case R.id.imvWorker3:

                break;
            case R.id.imvEdit1:
                iChooseImage = 1;
                showPopupPickImage();
                break;
            case R.id.imvEdit2:
                iChooseImage = 2;
                showPopupPickImage();
                break;
            case R.id.imvEdit3:
                iChooseImage = 3;
                showPopupPickImage();
                break;
        }
    }

    private void prepareUpdateWorker() {
        String barcode = edtBarcode.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String unit_no = edtUnitNo.getText().toString().trim();
        String company = edtCompany.getText().toString().trim();
        String work_permit = edtWorkPermit.getText().toString().trim();
        String passport = edtPassport.getText().toString().trim();
        if (barcode.length() == 0) {
            showToastError(getString(R.string.blank_barcode));
        } else if (name.length() == 0) {
            showToastError(getString(R.string.blank_name));
        } else if (unit_no.length() == 0) {
            showToastError(getString(R.string.blank_unit_no));
        } else if (company.length() == 0) {
            showToastError(getString(R.string.blank_company));
        } else if (work_permit.length() == 0) {
            showToastError(getString(R.string.blank_work_permit));
        } else if (passport.length() == 0) {
            showToastError(getString(R.string.blank_passport));
        } else if (mWorker.getDormitory_id() == null) {
            showToastError(getString(R.string.choose_dormitory));
        } else if (mWorker.getBlock_id() == null) {
            showToastError(getString(R.string.choose_block));
        } else if (mWorker.getLevel_id() == null) {
            showToastError(getString(R.string.choose_level));
        } else if (mWorker.getRoom_id() == null) {
            showToastError(getString(R.string.choose_room));
        } else if (mWorker.getNationality_code() == null) {
            showToastError(getString(R.string.choose_nationality));
        } else if (mWorker.getExpiry() == null) {
            showToastError(getString(R.string.choose_expiry));
        } else if (mWorker.getSex() == null) {
            showToastError(getString(R.string.choose_sex));
        } else {
            mWorker.setBarcode(barcode);
            mWorker.setName(name);
            mWorker.setUnit_number(unit_no);
            mWorker.setCompany(company);
            mWorker.setWork_permit(work_permit);
            mWorker.setPassport(passport);

            showPopupLogChange();
        }
    }

    private void updateWorker(Bitmap bitmap) {
        showProgressDialog(false);
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.updateWorker(new WorkerCallback.UpdateWorkerCallback() {
            @Override
            public void onSuccess(boolean success, String result) {
                if (success) {
                    WorkerController.insertOrUpdate(WorkerDetailActivity.this, mWorker);
                    getLogChange();
                    StaticFunction.sendBroadCast(WorkerDetailActivity.this, BroadcastValue.WORKER_NOTIFY_LIST);

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
        }, mWorker, bitmap);
    }

    private void changeImage(final Uri uri, final Bitmap bmSign) {
        String column_image = "";
        switch (iChooseImage) {
            case 1:
                column_image = "image_1";
                mWorker.setLog_message("Change image_1");
                break;
            case 2:
                column_image = "image_2";
                mWorker.setLog_message("Change image_2");
                break;
            case 3:
                column_image = "image_3";
                mWorker.setLog_message("Change image_3");
                break;
        }
        showProgressDialog(false);
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.changeImageWorker(new WorkerCallback.ChangeImageCallback() {
            @Override
            public void onSuccess(boolean success, String result, String image_1, String image_2, String image_3) {
                if (success) {
                    mWorker.setImage_1(image_1);
                    mWorker.setImage_2(image_2);
                    mWorker.setImage_3(image_3);
                    WorkerController.insertOrUpdate(WorkerDetailActivity.this, mWorker);
                    imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_1(), imvWorker1, options);
                    imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_2(), imvWorker2, options);
                    imageLoader.displayImage(StaticFunction.BASE_URL + mWorker.getImage_3(), imvWorker3, options);
                    getLogChange();
                    if (iChooseImage == 1) {
                        StaticFunction.sendBroadCast(WorkerDetailActivity.this, BroadcastValue.WORKER_NOTIFY_LIST);
                    }

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
        }, mWorker, column_image, uri, bmSign);
    }

    private void getNationality() {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.getNationality(new WorkerCallback.GetNationalityCallback() {
            @Override
            public void onSuccess(boolean success, String result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getLogChange() {
        WorkerVolley workerVolley = new WorkerVolley(this);
        workerVolley.getLogChange(new WorkerCallback.GetLogChangeCallback() {
            @Override
            public void onSuccess(boolean success, String result, JSONArray jsonArray) {
                setListLogChange(jsonArray);
            }

            @Override
            public void onError(String error) {

            }
        }, worker_id + "");
    }

    private void setListLogChange(JSONArray array) {
        int size = array.length();
        if (size == 0) {
            txtLogChange.setVisibility(View.GONE);
            lnlLogChange.setVisibility(View.GONE);
        } else {
            txtLogChange.setVisibility(View.VISIBLE);
            lnlLogChange.setVisibility(View.VISIBLE);
        }

        lnlLogChange.removeAllViews();

        for (int i = 0; i < size; i++) {
            try {
                JSONObject object = array.getJSONObject(i);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewRow = inflater.inflate(R.layout.row_log_change, null);
                ImageView imv = (ImageView) viewRow.findViewById(R.id.imv);
                TextView txt = (TextView) viewRow.findViewById(R.id.txt);
                TextView txtTime = (TextView) viewRow.findViewById(R.id.txtTime);

                txt.setText(object.getString("log_message"));
                String time = object.getString("created_at");
                txtTime.setText(time.substring(0, 10));
                imageLoader.displayImage(StaticFunction.BASE_URL + object.getString("image_sign"), imv, options);

                lnlLogChange.addView(viewRow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPopupLogChange() {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_log_change);

        final EditText edtLogChange = (EditText) dialog.findViewById(R.id.edtLogChange);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        edtLogChange.setText(mWorker.getLog_message());

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log_change = edtLogChange.getText().toString().trim();
                if (log_change.length() == 0) {
                    showToastError(getString(R.string.blank_log_change));
                } else {
                    mWorker.setLog_message(log_change);
                    showPopupSignature();
                    dialog.dismiss();
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showPopupSignature() {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_signature);

        final LinearLayout lnlRoot = (LinearLayout) dialog.findViewById(R.id.lnlRoot);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lnlRoot.getLayoutParams();
        int screenWidth = StaticFunction.getScreenWidth(this);
        params.width = screenWidth;
        params.height = screenWidth;
        lnlRoot.setLayoutParams(params);

        final SignatureView signatureView = new SignatureView(this);
        signatureView.init(screenWidth, screenWidth);
        lnlRoot.addView(signatureView);

        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnlRoot.setDrawingCacheEnabled(false);
                lnlRoot.destroyDrawingCache();
                lnlRoot.buildDrawingCache();
                updateWorker(lnlRoot.getDrawingCache());
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showPopupSignatureToChangeImage(final Uri uri) {
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_signature);

        final LinearLayout lnlRoot = (LinearLayout) dialog.findViewById(R.id.lnlRoot);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lnlRoot.getLayoutParams();
        int screenWidth = StaticFunction.getScreenWidth(this);
        params.width = screenWidth;
        params.height = screenWidth;
        lnlRoot.setLayoutParams(params);

        final SignatureView signatureView = new SignatureView(this);
        signatureView.init(screenWidth, screenWidth);
        lnlRoot.addView(signatureView);

        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnlRoot.setDrawingCacheEnabled(false);
                lnlRoot.destroyDrawingCache();
                lnlRoot.buildDrawingCache();
                changeImage(uri, lnlRoot.getDrawingCache());
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                txtDormitory.setText(listDormitory.get(position).getName());
                mWorker.setDormitory_id(listDormitory.get(position).getId());
                mWorker.setBlock_id(null);
                mWorker.setLevel_id(null);
                mWorker.setRoom_id(null);
                txtBlock.setText("");
                txtLevel.setText("");
                txtRoom.setText("");
                showPopupListBlock();
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

    public void showPopupListBlock() {
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
        final List<Block> listBlock = BlockController.getByDormitoryId(this, mWorker.getDormitory_id() + "");
        for (Block block : listBlock) {
            list.add(block.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtBlock.setText(listBlock.get(position).getName());
                mWorker.setBlock_id(listBlock.get(position).getId());
                mWorker.setLevel_id(null);
                mWorker.setRoom_id(null);
                txtLevel.setText("");
                txtRoom.setText("");
                showPopupListLevel();
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
            showToastError("There is no block of " + txtDormitory.getText().toString());
            dialog.dismiss();
        }
    }

    public void showPopupListLevel() {
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
        txtTitle.setText("Level");

        List<String> list = new ArrayList<>();
        final List<Level> listLevel = LevelController.getByBlockId(this, mWorker.getBlock_id() + "");
        for (Level level : listLevel) {
            list.add(level.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtLevel.setText(listLevel.get(position).getName());
                mWorker.setLevel_id(listLevel.get(position).getId());
                mWorker.setRoom_id(null);
                txtRoom.setText("");
                showPopupListRoom();
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

        if (listLevel.size() == 0) {
            showToastError("There is no level of " + txtBlock.getText().toString());
            dialog.dismiss();
        }
    }

    public void showPopupListRoom() {
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
        txtTitle.setText("Room");

        List<String> list = new ArrayList<>();
        final List<Room> listRoom = RoomController.getByLevelId(this, mWorker.getLevel_id() + "");
        for (Room room : listRoom) {
            list.add(room.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtRoom.setText(listRoom.get(position).getName());
                mWorker.setRoom_id(listRoom.get(position).getId());
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

        if (listRoom.size() == 0) {
            showToastError("There is no room of " + txtLevel.getText().toString());
            dialog.dismiss();
        }
    }

    public void showPopupListNationality() {
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
        txtTitle.setText("Nationality");

        List<String> list = new ArrayList<>();
        final List<Nationality> listNationality = NationalityController.getAll(this);
        for (Nationality nationality : listNationality) {
            list.add(nationality.getName());
        }

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtNationality.setText(listNationality.get(position).getName());
                mWorker.setNationality_code(listNationality.get(position).getCode());
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

        if (listNationality.size() == 0) {
            showToastError("There is no nationality");
            getNationality();
            dialog.dismiss();
        }
    }

    public void showPopupListSex() {
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
        txtTitle.setText("Sex");

        final List<String> list = new ArrayList<>();
        list.add("Female");
        list.add("Male");

        ItemListCallback itemListCallback = new ItemListCallback() {
            @Override
            public void ItemClick(int position) {
                txtSex.setText(list.get(position));
                mWorker.setSex(position);
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
    }

    private void showDatePicker() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "";
        if (mWorker.getExpiry() == null) {
            Calendar c = Calendar.getInstance();
            dateInString = formatter.format(c.getTime());
        } else {
            dateInString = mWorker.getExpiry();
        }
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (java.text.ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat ftDay = new SimpleDateFormat("dd");
        SimpleDateFormat ftMonth = new SimpleDateFormat("MM");
        SimpleDateFormat ftYear = new SimpleDateFormat("yyyy");

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }
                        SimpleDateFormat ftDay = new SimpleDateFormat(
                                "yyyy-MM-dd");

                        String strDate = ftDay.format(date);
                        txtExpiry.setText(strDate);
                        mWorker.setExpiry(strDate);
                    }

                }, Integer.parseInt(ftYear.format(date)),
                Integer.parseInt(ftMonth.format(date)) - 1,
                Integer.parseInt(ftDay.format(date)));
        dp.setTitle("Calendar");
        dp.setMessage("Select Expiry Date");

        dp.show();
    }

    ///// choose image
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
                try {
                    switch (iChooseImage) {
                        case 1:
                            showPopupSignatureToChangeImage(uri);
                            break;
                        case 2:
                            showPopupSignatureToChangeImage(uri);
                            break;
                        case 3:
                            showPopupSignatureToChangeImage(uri);
                            break;
                    }
                } catch (Exception e) {

                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = getFile();
            if (uri != null) {
                try {
                    switch (iChooseImage) {
                        case 1:
                            showPopupSignatureToChangeImage(uri);
                            break;
                        case 2:
                            showPopupSignatureToChangeImage(uri);
                            break;
                        case 3:
                            showPopupSignatureToChangeImage(uri);
                            break;
                    }
                } catch (Exception e) {

                }
            }
        }
    }
    ///// choose image
}
