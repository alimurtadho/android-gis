package com.dariyanti.uasgis.Home.LoadMahasiswa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dariyanti.uasgis.R;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private Context context;
    private List<MahasiswaModel> mahasiswaModels;
    private String latitude, longitude;
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

    }

    private void getDirections(final int position){
        latitude = mahasiswaModels.get(position).getLatitude();
    }
}
