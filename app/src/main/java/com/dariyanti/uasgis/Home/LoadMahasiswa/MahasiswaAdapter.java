package com.dariyanti.uasgis.Home.LoadMahasiswa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dariyanti.uasgis.API.MultipartJSONRequest;
import com.dariyanti.uasgis.API.MyRequest;
import com.dariyanti.uasgis.API.URL;
import com.dariyanti.uasgis.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private Context context;
    private List<MahasiswaModel> mahasiswaModels;
    private String latitude, longitude, nbi;
    private Dialog mEditMahasiswaDialog, mDetailMahasiswaDialog;

    private EditText et_nbi, et_nama, et_tempat_lahir, et_tanggal_lahir, et_telepon, et_alamat, et_lat, et_long, et_tujuan;
    private ImageView iv_foto, iv_v_foto;
    private Button btn_browse_foto, btn_simpan;
    private String mImagePath;
    private TextView tv_judul,tv_v_nbi,tv_v_nama, tv_v_tempat_lahir, tv_v_tanggal_lahir, tv_v_telephone, tv_v_alamat, tv_v_lat, tv_v_long;
    private RequestQueue requestQueue;

    public MahasiswaAdapter(Context context, List models){
        this.context = context;
        this.mahasiswaModels = models;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_nama, text_nbi;
        public Button button_edit, button_hapus, button_detail;
        public LinearLayout button_directions;
        public ViewHolder(View v){
            super(v);
            text_nama = v.findViewById(R.id.text_nama);
            text_nbi = v.findViewById(R.id.text_nbi);
            button_directions = v.findViewById(R.id.button_directions);
            button_edit = v.findViewById(R.id.button_edit);
            button_hapus = v.findViewById(R.id.button_hapus);
            button_detail = v.findViewById(R.id.button_detail);
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
        viewHolder.button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("anu","kuda");
                detailMahasiswa(i);
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

    private void detailMahasiswa(int position) {
        mDetailMahasiswaDialog = new Dialog(context);
        mDetailMahasiswaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDetailMahasiswaDialog.setContentView(R.layout.dialog_detail_mahasiswa);

        tv_v_nbi = mDetailMahasiswaDialog.findViewById(R.id.tv_v_nbi);
        tv_v_nama= mDetailMahasiswaDialog.findViewById(R.id.tv_v_nama);
        tv_v_tempat_lahir= mDetailMahasiswaDialog.findViewById(R.id.tv_v_tempat_lahir);
        tv_v_tanggal_lahir= mDetailMahasiswaDialog.findViewById(R.id.tv_v_tanggal_lahir);
        tv_v_telephone= mDetailMahasiswaDialog.findViewById(R.id.tv_v_telephone);
        tv_v_alamat= mDetailMahasiswaDialog.findViewById(R.id.tv_v_alamat);
        tv_v_lat= mDetailMahasiswaDialog.findViewById(R.id.tv_v_lat);
        tv_v_long= mDetailMahasiswaDialog.findViewById(R.id.tv_v_long);
        iv_v_foto = mDetailMahasiswaDialog.findViewById(R.id.iv_v_foto);

        tv_v_nbi.setText(mahasiswaModels.get(position).getNbi());
        tv_v_nama.setText(mahasiswaModels.get(position).getNama());
        tv_v_tempat_lahir.setText(mahasiswaModels.get(position).getTempat());
        tv_v_tanggal_lahir.setText(mahasiswaModels.get(position).getTanggal_lahir());
        tv_v_telephone.setText(mahasiswaModels.get(position).getTelepon());
        tv_v_lat.setText(mahasiswaModels.get(position).getLatitude());
        tv_v_long.setText(mahasiswaModels.get(position).getLongitude());
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);
        Glide.with(context).load(mahasiswaModels.get(position).getFoto()).apply(requestOptions).into(iv_v_foto);
        mDetailMahasiswaDialog.show();
        Window window = mDetailMahasiswaDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getItemCount() {
        return mahasiswaModels.size();
    }

    private void hapusMahasiswa(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Konfirmasi Hapus User")
                .setMessage("Apakah Anda ingin menghapus user "+mahasiswaModels.get(position).getNama()+" ?")
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDelMahasiswa(position);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void myDelMahasiswa(final int position) {
        String url = URL.MAIN_URL +mahasiswaModels.get(position).getId();
        Log.d("url",url);

        requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("status");

                    if (success.equals("success")) {
                        Toast.makeText(context, "Hapus data sukses!", Toast.LENGTH_LONG).show();
                        mahasiswaModels.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Hapus data gagal", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Gagal update",Toast.LENGTH_LONG).show();
            }
        }) {

        };
        requestQueue.add(request);
    }

    private void editMahasiswa(final int position){
        mEditMahasiswaDialog = new Dialog(context);
        mEditMahasiswaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEditMahasiswaDialog.setContentView(R.layout.dialog_input_mahasiswa);
        et_nbi = mEditMahasiswaDialog.findViewById(R.id.et_nbi);
        et_nama = mEditMahasiswaDialog.findViewById(R.id.et_nama);
        et_alamat = mEditMahasiswaDialog.findViewById(R.id.et_alamat);
        et_tempat_lahir = mEditMahasiswaDialog.findViewById(R.id.et_tempat_lahir);
        et_tanggal_lahir = mEditMahasiswaDialog.findViewById(R.id.et_tanggal_lahir);
        et_telepon = mEditMahasiswaDialog.findViewById(R.id.et_telepon);
        et_lat = mEditMahasiswaDialog.findViewById(R.id.et_lat);
        et_long = mEditMahasiswaDialog.findViewById(R.id.et_long);
        iv_foto = mEditMahasiswaDialog.findViewById(R.id.iv_foto);
        btn_browse_foto = mEditMahasiswaDialog.findViewById(R.id.btn_browse_foto);
        btn_simpan = mEditMahasiswaDialog.findViewById(R.id.btn_simpan);
        tv_judul = mEditMahasiswaDialog.findViewById(R.id.judul);

        //set data
        tv_judul.setText("Edit Mahasiswa");
        et_nbi.setText(mahasiswaModels.get(position).getNbi());
        et_nama.setText(mahasiswaModels.get(position).getNama());
        et_alamat.setText(mahasiswaModels.get(position).getAlamat());
        et_tempat_lahir.setText(mahasiswaModels.get(position).getTempat());
        et_tanggal_lahir.setText(mahasiswaModels.get(position).getTanggal_lahir());
        et_telepon.setText(mahasiswaModels.get(position).getTelepon());
        et_lat.setText(mahasiswaModels.get(position).getLatitude());
        et_long.setText(mahasiswaModels.get(position).getLongitude());
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);
        Glide.with(context).load(mahasiswaModels.get(position).getFoto()).apply(requestOptions).into(iv_foto);

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
                }).show(((FragmentActivity)context).getSupportFragmentManager());
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
                DatePickerDialog dpd = new DatePickerDialog(context,
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
                    prosesEditMahasiswa(position);
                }
            }
        });

        mEditMahasiswaDialog.show();
        Window window = mEditMahasiswaDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void prosesEditMahasiswa(int position) {

        String url = URL.MAIN_URL +mahasiswaModels.get(position).getId();
//        Log.d("url",url);
        MultipartJSONRequest request = new MultipartJSONRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String success = response.getString("status");

                            if (success.equals("success")){
                                Toast.makeText(context, "Edit data sukses!", Toast.LENGTH_LONG).show();
                                mEditMahasiswaDialog.dismiss();
                            }else{
                                Toast.makeText(context, "Edit data gagal", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(context, "Gagal : "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());
                    }
                });


        //Log.d("imgpath", mImagePath);
        request.addStringParam("_method","PUT");
        request.addFile("photo", mImagePath);
        request.addStringParam("nbi", et_nbi.getText().toString());
        request.addStringParam("name",et_nama.getText().toString());
        request.addStringParam("place_of_birth",et_tempat_lahir.getText().toString());
        request.addStringParam("date_of_birth",et_tanggal_lahir.getText().toString());
        request.addStringParam("phone",et_telepon.getText().toString());
        request.addStringParam("address",et_alamat.getText().toString());
        request.addStringParam("latitude", et_lat.getText().toString());
        request.addStringParam("longitude",et_long.getText().toString());
        request.setShouldCache(false);
        Log.d("updatedata", MyRequest.getDebugReqString(url, request));
        MyRequest.getInstance(context).addToRequestQueue(request);


//        requestQueue = Volley.newRequestQueue(context);
//        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("status");
//
//
//                    if (success.equals("success")) {
//                        Toast.makeText(context, "Edit data sukses!", Toast.LENGTH_LONG).show();
//                        mEditMahasiswaDialog.dismiss();
//                    } else {
//                        Toast.makeText(context, "Edit data gagal", Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                }catch (Exception e){
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context,"Gagal update ke jadwak",Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> request  = new HashMap<String, String>();
//                request.put("nbi", et_nbi.getText().toString());
//                request.put("name", et_nama.getText().toString());
//                request.put("place_of_birth", et_tempat_lahir.getText().toString());
//                request.put("date_of_birth", et_tanggal_lahir.getText().toString());
//                request.put("phone", et_telepon.getText().toString());
//                request.put("address", et_alamat.getText().toString());
//                request.put("latitude", et_lat.getText().toString());
//                request.put("longitude", et_long.getText().toString());
//
//                return request;
//            }
//        };
//        requestQueue.add(request);
    }

    private void getDirections(final int position){
        latitude = mahasiswaModels.get(position).getLatitude();
        longitude = mahasiswaModels.get(position).getLongitude();
        nbi = mahasiswaModels.get(position).getNbi();

        Intent intent = new Intent("direction");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("latitude",latitude+"");
        intent.putExtra("longitude",longitude+"");
        intent.putExtra("title",nbi+"");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
