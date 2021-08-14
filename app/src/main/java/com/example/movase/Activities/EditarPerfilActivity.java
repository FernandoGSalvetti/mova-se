package com.example.movase.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.movase.Enum.Esportes;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.FireStore;
import com.example.movase.Repositories.UserFirebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditarPerfilActivity extends AppCompatActivity {
    String[] esportes = new String[]{"Basquete",
            "Calistenia",
            "Corrida",
            "Caminhada",
            "Musculação",
            "Futebol",
            "Futsal",
            "Fut7",
            "Volêi"};
    private TextInputEditText etApelido;
    private String caminhoDaFoto;
    private TextInputEditText etNomeCompleto;
    private CircleImageView fotoPerfil;
    private AutoCompleteTextView spinnerEsportesPref;
    private UsuarioViewModel usuarioViewModel;
    private MaterialButton btAbrirCamera;
    private MaterialButton btSalvar;
    private final int CAMERA_PERMISSION_CODE = 100;
    private Uri imgUri = null;
    private Intent abrirCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private StorageReference mStorage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializaComponentes();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Bundle bundle = getIntent().getExtras();
        usuarioViewModel = bundle.getParcelable("usuario");
        etNomeCompleto.setEnabled(false);
        ArrayAdapter<String> adapterEsportesPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, esportes);
        spinnerEsportesPref.setAdapter(adapterEsportesPref);
        populaComponentes(adapterEsportesPref);
        btAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        spinnerEsportesPref.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String esporteSelecionado = parent.getItemAtPosition(position).toString();
                usuarioViewModel.setEsportePreferido(esporteSelecionado);
            }
        });
        etApelido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usuarioViewModel.setApelido(s.toString());
            }
        });
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        caminhoDaFoto = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != this.RESULT_CANCELED){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, timeStamp, null);
            imgUri = Uri.parse(path);
            fotoPerfil.setImageURI(imgUri);
        }
    }

    private void populaComponentes(ArrayAdapter adapter) {
        etApelido.setText(usuarioViewModel.getApelido());
        etNomeCompleto.setText(usuarioViewModel.getNomeCompleto());
        selectValue(adapter, usuarioViewModel.getEsportePreferido());
        if(usuarioViewModel.getFotoPerfil().isEmpty()){
        }else{
            Picasso.with(this).load(usuarioViewModel.getFotoPerfil()).transform(new CropCircleTransformation()).into(fotoPerfil);
        }

    }
    private void selectValue(ArrayAdapter adapter, String value) {
        for (int i=0;i<esportes.length;i++) {
            if (esportes[i].equals(value)) {
                spinnerEsportesPref.setText(spinnerEsportesPref.getAdapter().getItem(i).toString(), false);
            }
        }
    }
    private void salvaDados(){
        FireStore fireStore = new FireStore();
        UserFirebase userFirebase = new UserFirebase();
        fireStore.getDb()
                .collection("usuarios")
                .document(userFirebase.getCurrentUserUser().getUid())
                .set(usuarioViewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditarPerfilActivity.this, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                    Intent voltarParaHomeFragment = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(voltarParaHomeFragment);
                }else{
                    Toast.makeText(EditarPerfilActivity.this, "Falha ao alterar dados", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage() {
        UserFirebase userFirebase = new UserFirebase();
        FireStore fireStore = new FireStore();
        if (imgUri != null) {
            StorageReference ref = storageReference.child("selfie/" + userFirebase.getCurrentUserUser().getUid());
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            usuarioViewModel.setFotoPerfil(downloadUrl);
                            salvaDados();
                        }
                    });
                }
            });
        }else{
            salvaDados();
        }
    }
    private void inicializaComponentes() {
        btAbrirCamera = findViewById(R.id.activity_editar_perfil_bt_abrir_camera);
        etApelido = findViewById(R.id.activity_editar_perfil_etApelido);
        etNomeCompleto = findViewById(R.id.activity_editar_perfil_etNome);
        fotoPerfil = findViewById(R.id.activity_editar_perfil_ivPerfil);
        spinnerEsportesPref = findViewById(R.id.activity_editar_perfil_esporte_preferido);
        btSalvar = findViewById(R.id.activity_editar_perfil_btSalvar);
    }
}