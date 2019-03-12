package com.example.adindariztiaputri.creditapplicationfrsdk.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.artoz.sdk.Creditapplicationv10Client;
import com.artoz.sdk.CreateApplicationRequest;
import com.artoz.sdk.Channel;
import com.artoz.sdk.CreateApplicationResponse;
import com.artoz.sdk.CreateApplicationResponse_ServiceResponse;
import com.artoz.sdk.DaftarKabupatenRequest;
import com.artoz.sdk.DaftarKabupatenRequest_ServiceResponse;
import com.artoz.sdk.DaftarKabupatenResponse_ServiceResponse;
import com.artoz.sdk.DaftarKecamatanRequest;
import com.artoz.sdk.DaftarKecamatanRequest_ServiceResponse;
import com.artoz.sdk.DaftarKecamatanResponse_ServiceResponse;
import com.artoz.sdk.DaftarKelurahanRequest;
import com.artoz.sdk.DaftarKelurahanResponse_ServiceResponse;
import com.artoz.sdk.DaftarKodeposRequest;
import com.artoz.sdk.DaftarKodeposResponse_ServiceResponse;
import com.artoz.sdk.DaftarProvinsiRequest;
import com.artoz.sdk.DaftarProvinsiRequest_ServiceResponse;
import com.artoz.sdk.DaftarProvinsiResponse_ServiceResponse;
import com.artoz.sdk.GetApplicationRequest;
import com.artoz.sdk.GetApplicationResponse_ServiceResponse;
import com.artoz.sdk.MethodCallOptions;
import com.artoz.sdk.ResponseTempats;
import com.artoz.sdk.Status;
import com.artoz.sdk.creditapplicationv10;
import com.example.adindariztiaputri.creditapplicationfrsdk.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static
    {
        System.loadLibrary("artoz-sdk-android");
    }

    private ArrayList<String> listProvinsi, listKabupaten, listKecamatan, listKelurahan;
    private List<ResponseTempats> arrayProvinsi;
    Context mContext;
    Spinner spinnerProvinsi, spinnerKabupaten, spinnerKecamatan, spinnerKelurahan;
    String kelurahanName;

    int provinsiId, kabupatenId, kecamatanId, kelurahanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        spinnerProvinsi = findViewById(R.id.province);
        spinnerKabupaten= findViewById(R.id.kabupaten);
        spinnerKecamatan = findViewById(R.id.kecamatan);
        spinnerKelurahan = findViewById(R.id.kelurahan);

        listProvinsi = new ArrayList<>();
        listKabupaten = new ArrayList<>();

        arrayProvinsi = new ArrayList<>();


        creditapplicationv10.Initialize();

        final MethodCallOptions options = new MethodCallOptions();

//        resp = client.DaftarProvinsi(req, options)
        options.UseOfflineCache(true);
        Channel channel = creditapplicationv10.MakeDefaultSSLChannel("api-gateway.nextapi.navcore.com");
        final Creditapplicationv10Client client = new Creditapplicationv10Client(channel);
//        GetApplicationResponse_ServiceResponse resp = client.DaftarProvinsi(req, options);




        try {

            GetApplicationRequest request = new GetApplicationRequest();
            request.set_id(67);
            GetApplicationResponse_ServiceResponse response = client.GetApplication(request, options);
//            GetApplicationResponse_ServiceResponse response = client.;
            if (response.GetStatus().ok()){
                final String data = response.GetMessage().get_name();
//                String coba = options.toString();


                Toast.makeText(this, data + "options ", Toast.LENGTH_SHORT).show();
            } else {
                String err = response.GetStatus().error_message();
                Log.w("app", err);
            }
        } catch (Exception e){
            Log.e("app", "Exception caught", e);

        }

//        try {
            DaftarProvinsiRequest daftarProvinsiRequest = new DaftarProvinsiRequest();
            final DaftarProvinsiResponse_ServiceResponse response = client.DaftarProvinsi(daftarProvinsiRequest, options);
//            Log.d("DEBUG", ""+response.GetStatus().ok());
//            Log.d("DEBUG", ""+options.IsOfflineCacheUsed());
//            Log.d("DEBUGg", ""+client.IsResponseForDaftarProvinsiAvailable());
            if(response.GetStatus().ok()){
//                arrayProvinsi.add(response.GetMessage());
                String coba = response.GetMessage().get_tempats(8).get_nama();
                int panjangArray = response.GetMessage().tempats_size();

//                Toast.makeText(this, coba + " panjang " + panjangArray, Toast.LENGTH_SHORT).show();






                provinsiId = response.GetMessage().get_tempats(0).get_id();
                for (int i = 0; i < panjangArray; i++){

                    String namaProvinsi = response.GetMessage().get_tempats(i).get_nama();
//                    int id_prov = response.GetMessage().get_tempats(i).get_id();
                    Log.d("DEBUG" + i, namaProvinsi);

//                    ResponseTempats temp = new ResponseTempats(id_prov, );
                    listProvinsi.add(namaProvinsi);
//                    arrayProvinsi.add(response.GetMessage().get_tempats(i));

                }
                Log.w("ouputan", response.GetMessage().toString());
//                Log.v("mamam")

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, listProvinsi);
//                ArrayAdapter<ResponseTempats> adapter = new ArrayAdapter<ResponseTempats>(mContext, android.R.layout.simple_spinner_dropdown_item, arrayProvinsi);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvinsi.setAdapter(adapter);


                spinnerProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        int id_prov = listProvinsi.get(position);
//                provinsiId = arrayProvinsi.get(position).get_id();
                        provinsiId = response.GetMessage().get_tempats(position).get_id();

                        Toast.makeText(mContext, ""+provinsiId, Toast.LENGTH_SHORT).show();
                        initializeSpinnerKabupaten(client, options, provinsiId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

//        } catch (Exception e){
//            Log.e("hancur","Exception caught", e);
//        }



    }

    private void initializeSpinnerKabupaten(final Creditapplicationv10Client client, final MethodCallOptions options, int id) {

        listKabupaten = new ArrayList<>();

        DaftarKabupatenRequest request = new DaftarKabupatenRequest();
        request.set_provinsi_id(id);

        final DaftarKabupatenResponse_ServiceResponse response = client.DaftarKabupaten(request, options);
        if(response.GetStatus().ok()){
            int panjangArray = response.GetMessage().tempats_size();

            kabupatenId = response.GetMessage().get_tempats(0).get_id();
            for (int i = 0; i < panjangArray; i++){
                String namaKabupaten = response.GetMessage().get_tempats(i).get_nama();
                listKabupaten.add(namaKabupaten);
            }

            Toast.makeText(mContext, ""+listKabupaten, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, listKabupaten);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKabupaten.setAdapter(adapter);

            spinnerKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kabupatenId = response.GetMessage().get_tempats(position).get_id();
                    Toast.makeText(MainActivity.this, "kabupaten "+kabupatenId, Toast.LENGTH_SHORT).show();

                    initializeSpinnerKecamatan(client, options, kabupatenId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void initializeSpinnerKecamatan(final Creditapplicationv10Client client, final MethodCallOptions options, int id){


        listKecamatan = new ArrayList<>();
        DaftarKecamatanRequest daftarKecamatanRequest = new DaftarKecamatanRequest();
        daftarKecamatanRequest.set_kabupaten_id(id);

        final DaftarKecamatanResponse_ServiceResponse response = client.DaftarKecamatan(daftarKecamatanRequest, options);
        if(response.GetStatus().ok()){
            int panjangArray = response.GetMessage().tempats_size();

            kecamatanId = response.GetMessage().get_tempats(0).get_id();
            for(int i =0; i < panjangArray; i++){
                String namaKecamatan = response.GetMessage().get_tempats(i).get_nama();

                listKecamatan.add(namaKecamatan);

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, listKecamatan);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKecamatan.setAdapter(adapter);


            spinnerKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kecamatanId = response.GetMessage().get_tempats(position).get_id();
                    Toast.makeText(MainActivity.this, "kecamatan: "+kecamatanId, Toast.LENGTH_SHORT).show();

                    initializeSpinnerKelurahan(client, options,kecamatanId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }


    }

    public void initializeSpinnerKelurahan(final Creditapplicationv10Client client, final MethodCallOptions options, int id){
        DaftarKelurahanRequest daftarKelurahanRequest = new DaftarKelurahanRequest();
        daftarKelurahanRequest.set_kecamatan_id(id);

        final DaftarKelurahanResponse_ServiceResponse response = client.DaftarKelurahan(daftarKelurahanRequest, options);
        if(response.GetStatus().ok()){
//            kelurahanId = response.GetMessage().get_tempats(0).get_id();
        int panjangArray = response.GetMessage().tempats_size();
        listKelurahan = new ArrayList<>();

        for (int i=0; i < panjangArray; i++){
            String namaKelurahan = response.GetMessage().get_tempats(i).get_nama();

            listKelurahan.add(namaKelurahan);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, listKelurahan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelurahan.setAdapter(adapter);

        spinnerKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kelurahanId = response.GetMessage().get_tempats(position).get_id();
                kelurahanName = response.GetMessage().get_tempats(position).get_nama();
                Toast.makeText(MainActivity.this, "kelurahan " + kelurahanId, Toast.LENGTH_SHORT).show();

//                initializeSpinnerKodepos(client, options, kelurahanName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    public void initializeSpinnerKodepos(final Creditapplicationv10Client client, final MethodCallOptions options, String kelurahanName){
//        DaftarKodeposRequest daftarKodeposRequest = new DaftarKodeposRequest();
//        daftarKodeposRequest.set_kelurahan(kelurahanName);
//
//        final DaftarKodeposResponse_ServiceResponse response = client.DaftarKodepos(daftarKodeposRequest, options);
//
//        if(response.GetStatus().ok()){
//            int panjangArray = response.GetMessage().kodepos_size();
//
//            for(int i = 0; i < panjangArray; i++){
//                String namaKodepos = response.GetMessage().get_kodepos(i).get_kodepos()
//            }
//        }
//    }

}
