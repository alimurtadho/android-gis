package com.dariyanti.uasgis.Home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.request.JsonArrayRequest;
import com.dariyanti.uasgis.MainActivity;
import com.dariyanti.uasgis.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private Dialog mDialog;
    private EditText et_nbi, et_nama, et_tempat_lahir, et_tanggal_lahir, et_telepon, et_alamat, et_lat, et_long;
    private ImageView iv_foto;
    private Button btn_browse_foto, btn_simpan;
    private String mImagePath;
    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_tambah:
                tambahMahasiswa();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tambahMahasiswa(){
        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_input_mahasiswa);
        et_nbi = mDialog.findViewById(R.id.et_nbi);
        et_nama = mDialog.findViewById(R.id.et_nama);
        et_alamat = mDialog.findViewById(R.id.et_alamat);
        et_tempat_lahir = mDialog.findViewById(R.id.et_tempat_lahir);
        et_tanggal_lahir = mDialog.findViewById(R.id.et_tanggal_lahir);
        et_telepon = mDialog.findViewById(R.id.et_telepon);
        et_lat = mDialog.findViewById(R.id.et_lat);
        et_long = mDialog.findViewById(R.id.et_long);
        iv_foto = mDialog.findViewById(R.id.iv_foto);
        btn_browse_foto = mDialog.findViewById(R.id.btn_browse_foto);
        btn_simpan = mDialog.findViewById(R.id.btn_simpan);

        btn_browse_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        iv_foto.setImageURI(pickResult.getUri());
                        mImagePath = pickResult.getPath();
                    }
                }).setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {

                    }
                }).show(getActivity().getSupportFragmentManager());
            }
        });

        et_tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                et_tanggal_lahir.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_nbi.getText())){
                    et_nbi.setError("NBI Tidak Boleh Kosong");
                }else if(TextUtils.isEmpty(et_nama.getText())){
                    et_nama.setError("Nama Tidak Boleh Kosong");
                }else if(TextUtils.isEmpty(et_lat.getText())){
                    et_lat.setError("Lat Tidak Boleh Kosong");
                }else if(TextUtils.isEmpty(et_long.getText())){
                    et_long.setError("Long Tidak Boleh Kosong");
                }else{
                    prosesTambahMahasiswa();
                }
            }
        });

        mDialog.show();
        Window window = mDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void prosesTambahMahasiswa(){

    }
}
