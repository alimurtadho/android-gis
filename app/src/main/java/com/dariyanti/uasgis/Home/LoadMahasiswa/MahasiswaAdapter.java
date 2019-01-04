package com.dariyanti.uasgis.Home.LoadMahasiswa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dariyanti.uasgis.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Calendar;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private Context context;
    private List<MahasiswaModel> mahasiswaModels;
    private String latitude, longitude;
    private Dialog mEditMahasiswaDialog;
    public MahasiswaAdapter(Context context, List models){
        this.context = context;
        this.mahasiswaModels = models;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_nama, text_nbi;
        public Button button_edit, button_hapus, button_directions;
        public ViewHolder(View v){
            super(v);
            text_nama = v.findViewById(R.id.text_nama);
            text_nbi = v.findViewById(R.id.text_nbi);
            button_directions = v.findViewById(R.id.button_directions);
            button_edit = v.findViewById(R.id.button_edit);
            button_hapus = v.findViewById(R.id.button_hapus);
        }
    }

    @Override
    public MahasiswaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_load_data, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MahasiswaAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.text_nama.setText("Nama : "+mahasiswaModels.get(i).getNama());
        viewHolder.text_nbi.setText("NBI : "+mahasiswaModels.get(i).getNbi());
        viewHolder.button_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusMahasiswa(i);
            }
        });
        viewHolder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMahasiswa(i);
            }
        });
        viewHolder.button_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mahasiswaModels.size();
    }

    private void hapusMahasiswa(final int position){

    }

    private void editMahasiswa(final int position){
//        mEditMahasiswaDialog = new Dialog(context);
//        mEditMahasiswaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mEditMahasiswaDialog.setContentView(R.layout.dialog_input_mahasiswa);
//        et_nbi = mEditMahasiswaDialog.findViewById(R.id.et_nbi);
//        et_nama = mEditMahasiswaDialog.findViewById(R.id.et_nama);
//        et_alamat = mEditMahasiswaDialog.findViewById(R.id.et_alamat);
//        et_tempat_lahir = mEditMahasiswaDialog.findViewById(R.id.et_tempat_lahir);
//        et_tanggal_lahir = mEditMahasiswaDialog.findViewById(R.id.et_tanggal_lahir);
//        et_telepon = mEditMahasiswaDialog.findViewById(R.id.et_telepon);
//        et_lat = mEditMahasiswaDialog.findViewById(R.id.et_lat);
//        et_long = mEditMahasiswaDialog.findViewById(R.id.et_long);
//        iv_foto = mEditMahasiswaDialog.findViewById(R.id.iv_foto);
//        btn_browse_foto = mEditMahasiswaDialog.findViewById(R.id.btn_browse_foto);
//        btn_simpan = mEditMahasiswaDialog.findViewById(R.id.btn_simpan);
//
//        btn_browse_foto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
//                    @Override
//                    public void onPickResult(PickResult pickResult) {
//                        iv_foto.setImageURI(pickResult.getUri());
//                        mImagePath = pickResult.getPath();
//                    }
//                }).setOnPickCancel(new IPickCancel() {
//                    @Override
//                    public void onCancelClick() {
//
//                    }
//                }).show(context.getSupportFragmentManager());
//            }
//        });
//
//        et_tanggal_lahir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int mYear, mMonth, mDay, mHour, mMinute;
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                // Launch Date Picker Dialog
//                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                // Display Selected date in textbox
//                                et_tanggal_lahir.setText(year + "-"
//                                        + (monthOfYear + 1) + "-" + dayOfMonth);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                dpd.show();
//            }
//        });
//
//        btn_simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(TextUtils.isEmpty(et_nbi.getText())){
//                    et_nbi.setError("NBI Tidak Boleh Kosong");
//                }else if(TextUtils.isEmpty(et_nama.getText())){
//                    et_nama.setError("Nama Tidak Boleh Kosong");
//                }else if(TextUtils.isEmpty(et_lat.getText())){
//                    et_lat.setError("Lat Tidak Boleh Kosong");
//                }else if(TextUtils.isEmpty(et_long.getText())){
//                    et_long.setError("Long Tidak Boleh Kosong");
//                }else{
//                    prosesTambahMahasiswa();
//                }
//            }
//        });
//
//        mEditMahasiswaDialog.show();
//        Window window = mEditMahasiswaDialog.getWindow();
//        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void getDirections(final int position){
        latitude = mahasiswaModels.get(position).getLatitude();
    }
}
