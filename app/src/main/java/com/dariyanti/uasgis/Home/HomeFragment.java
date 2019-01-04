package com.dariyanti.uasgis.Home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dariyanti.uasgis.API.MultipartJSONRequest;
import com.dariyanti.uasgis.API.MyRequest;
import com.dariyanti.uasgis.API.URL;
import com.dariyanti.uasgis.Home.LoadMahasiswa.MahasiswaAdapter;
import com.dariyanti.uasgis.Home.LoadMahasiswa.MahasiswaModel;
import com.dariyanti.uasgis.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private Dialog mTambahMahasiswaDialog, mShowMahasiswaDialog;
    private EditText et_nbi, et_nama, et_tempat_lahir, et_tanggal_lahir, et_telepon, et_alamat, et_lat, et_long, et_tujuan;
    private ImageView iv_foto;
    private Button btn_browse_foto, btn_simpan;
    private FloatingActionButton fab_position;
    private String mImagePath;

    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;
    private List<MahasiswaModel> mahasiswaModels;

    private RecyclerView recyclerView;
    private MahasiswaAdapter mahasiswaAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GoogleMap mGoogleMap;
    private LatLngBounds.Builder mBuilder;
    private Marker mMarker;
    private LatLng mLatLng;
    private Location mLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionsGranted = false;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_maps);
        supportMapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        et_tujuan = view.findViewById(R.id.et_tujuan);
        fab_position = view.findViewById(R.id.fab_position);
        mahasiswaModels = new ArrayList<>();

        et_tujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMahasiswa();
            }
        });

        fab_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyPosition();
                getMyPosition();
            }
        });

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
        mTambahMahasiswaDialog = new Dialog(getActivity());
        mTambahMahasiswaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTambahMahasiswaDialog.setContentView(R.layout.dialog_input_mahasiswa);
        et_nbi = mTambahMahasiswaDialog.findViewById(R.id.et_nbi);
        et_nama = mTambahMahasiswaDialog.findViewById(R.id.et_nama);
        et_alamat = mTambahMahasiswaDialog.findViewById(R.id.et_alamat);
        et_tempat_lahir = mTambahMahasiswaDialog.findViewById(R.id.et_tempat_lahir);
        et_tanggal_lahir = mTambahMahasiswaDialog.findViewById(R.id.et_tanggal_lahir);
        et_telepon = mTambahMahasiswaDialog.findViewById(R.id.et_telepon);
        et_lat = mTambahMahasiswaDialog.findViewById(R.id.et_lat);
        et_long = mTambahMahasiswaDialog.findViewById(R.id.et_long);
        iv_foto = mTambahMahasiswaDialog.findViewById(R.id.iv_foto);
        btn_browse_foto = mTambahMahasiswaDialog.findViewById(R.id.btn_browse_foto);
        btn_simpan = mTambahMahasiswaDialog.findViewById(R.id.btn_simpan);

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

        mTambahMahasiswaDialog.show();
        Window window = mTambahMahasiswaDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    private void prosesTambahMahasiswa(){
        String url = URL.MAIN_URL ;

        MultipartJSONRequest request = new MultipartJSONRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String success = response.getString("status");

                            if (success.equals("success")){
                                Toast.makeText(getContext(), "Tambah data sukses!", Toast.LENGTH_LONG).show();
                                mTambahMahasiswaDialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), "Tambah data gagal", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(getContext(), "Gagal : "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("erro",error.toString());
                    }
                });


        request.addFile("photo", mImagePath);
        request.addStringParam("nbi", et_nbi.getText().toString());
        request.addStringParam("name",et_nama.getText().toString());
        request.addStringParam("latitude", et_lat.getText().toString());
        request.addStringParam("longitude",et_long.getText().toString());
        request.setShouldCache(false);
        Log.d("savedata", MyRequest.getDebugReqString(url, request));
        MyRequest.getInstance(getContext()).addToRequestQueue(request);



    }

    private void showMahasiswa(){
        mShowMahasiswaDialog = new Dialog(getActivity());
        mShowMahasiswaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mShowMahasiswaDialog.setContentView(R.layout.dialog_load_data);

        recyclerView = mShowMahasiswaDialog.findViewById(R.id.item_mahasiswa);

        if (mahasiswaAdapter!=null){
            mahasiswaModels.clear();
            showMahasiswaDataset();
        }else {
            showMahasiswaDataset();
        }


        mShowMahasiswaDialog.show();
        Window window = mShowMahasiswaDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);

    }

    private void showMahasiswaDataset(){
//        Toast.makeText(getActivity(), "Sudah muncul", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(),URL.BASE_URL + URL.MAIN_URL, Toast.LENGTH_SHORT).show();
        jsonArrayRequest = new JsonArrayRequest(URL.MAIN_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject mahasiswa = null;
//                Toast.makeText(getActivity(),response.toString(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i< response.length(); i++){
                    try{
                        mahasiswa = response.getJSONObject(i);
                        MahasiswaModel model = new MahasiswaModel();
                        model.setNama(mahasiswa.getString("name"));
                        model.setNbi(mahasiswa.getString("nbi"));
                        model.setTempat(mahasiswa.getString("place_of_birth"));
                        model.setTanggal_lahir(mahasiswa.getString("date_of_birth"));
                        model.setTelepon(mahasiswa.getString("phone"));
                        model.setAlamat(mahasiswa.getString("address"));
                        model.setFoto(mahasiswa.getString("photo"));
                        model.setLatitude(mahasiswa.getString("latitude"));
                        model.setLongitude(mahasiswa.getString("longitude"));
                        mahasiswaModels.add(model);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                setMahasiswaAdapter(mahasiswaModels);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error Fetching Data = "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    private void setMahasiswaAdapter(List mahasiswa){
        mahasiswaAdapter = new MahasiswaAdapter(getActivity(), mahasiswa);
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mahasiswaAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        updateMyPosition();
        getMyPosition();

    }

    private void getMyPosition() {
        try{
            if(mLocationPermissionsGranted){
                Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
                locationTask.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            mLocation = task.getResult();
                            mLatLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(mLatLng)
                                    .title(
                                            String.valueOf(mLocation.getLatitude()) +" "+
                                                    String.valueOf(mLocation.getLongitude()+" L")
                                    )
                            );
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,
                                    16
                            ));
                        }else{
                            Toast.makeText(getContext(), "Belum Ada Akses GPS", Toast.LENGTH_SHORT).show();
                            checkGpsPermissions();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateMyPosition(){
        if(mGoogleMap == null){
            return;
        }

        try{
            if(mLocationPermissionsGranted){
                mGoogleMap.setMyLocationEnabled(true);

            }else{
                mGoogleMap.setMyLocationEnabled(false);
                Toast.makeText(getContext(), "Belum Ada Akses GPS", Toast.LENGTH_SHORT).show();
                checkGpsPermissions();
            }
        }catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void checkGpsPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
            Toast.makeText(getContext(), "Akses GPS Aktif", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionsGranted = true;
                    Toast.makeText(getContext(), "Akses GPS Aktif", Toast.LENGTH_SHORT).show();
                }
            }
        }
        updateMyPosition();
    }
}
