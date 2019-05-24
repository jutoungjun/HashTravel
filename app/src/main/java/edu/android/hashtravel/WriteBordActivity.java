package edu.android.hashtravel;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteBordActivity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Spinner spinnerCategory, spinnerContinent, spinnerCountry;
    private String category, continent, country;
    private EditText textSubject, textDesc, textTag;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button loadGalleryButton, writeFinishButton;
    private ImageView writeImage1, writeImage2, writeImage3;
    private Uri uri;
    private List<Uri> uriList = new ArrayList<>();
    private StorageReference storageReference;
    private String date;
    private int fileNameCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_bord);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));

        storageReference = FirebaseStorage.getInstance().getReference();
        loadGalleryButton = findViewById(R.id.imageInputButton);
        writeFinishButton = findViewById(R.id.writeFinishButton);
        writeImage1 = findViewById(R.id.writeImage1);
        writeImage2 = findViewById(R.id.writeImage2);
        writeImage3 = findViewById(R.id.writeImage3);

        spinnerCategory = findViewById(R.id.writeSpinnerCategory);
        spinnerContinent = findViewById(R.id.writeSpinnerContinent);
        spinnerCountry = findViewById(R.id.writeSpinnerCountry);

        textSubject = findViewById(R.id.textSubject);
        textDesc = findViewById(R.id.textDesc);
        textTag = findViewById(R.id.textTag);

        writeFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInputDashBoard();
                if(date != null){
                    finish();

                }


            }
        });

        loadGalleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });
        spinnerContinent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adpter;
                if(position == 1) {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Asia_Country, android.R.layout.simple_spinner_item);
                } else if(position == 2) {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Europe_Country, android.R.layout.simple_spinner_item);
                } else if(position == 3) {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.America_, android.R.layout.simple_spinner_item);
                } else if(position == 4) {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.SouthAmerica_Country, android.R.layout.simple_spinner_item);
                } else if(position == 5) {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Africa_Country, android.R.layout.simple_spinner_item);
                } else if(position == 6){
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Oceania_, android.R.layout.simple_spinner_item);
                } else {
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.initialCountry, android.R.layout.simple_spinner_item);
                }

                adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerCountry.setAdapter(adpter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    } // end onCreate()

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                        && data != null && data.getData() != null) {
            uri = data.getData();
            uriList.add(uri);
        }
        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(data.getData());
            Bitmap img = BitmapFactory.decodeStream(in);
            in.close();
            if(writeImage1.getDrawable() == null) {
                writeImage1.setImageBitmap(img);
            }else if(writeImage1.getDrawable() != null && writeImage2.getDrawable() == null) {
                writeImage2.setImageBitmap(img);
            }else if(writeImage1.getDrawable() != null && writeImage2.getDrawable() != null && writeImage3.getDrawable() ==null) {
                writeImage3.setImageBitmap(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onClickInputDashBoard() {

        category = spinnerCategory.getSelectedItem().toString();
        continent = spinnerContinent.getSelectedItem().toString();
        country = spinnerCountry.getSelectedItem().toString();

        Intent intent = getIntent();
        firebaseUser = intent.getExtras().getParcelable("mAuth");

        String key = mDatabase.child("posts").push().getKey();

        String subject = textSubject.getText().toString();
        String desc = textDesc.getText().toString();
        String hashTag = textTag.getText().toString();

        if (subject.equals("")) {
            Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (desc.equals("")) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (hashTag.equals("")) {
            Toast.makeText(this, "해시태그를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            date = formatter.format(now);

            DashBoard dashBoard = null;
            if (!subject.equals("") && !desc.equals("") && !hashTag.equals("")) {
                dashBoard = new DashBoard(firebaseUser.getUid(), firebaseUser.getDisplayName(), date, category, continent, country, subject, desc, hashTag, 0, null);
            }

            if (dashBoard != null) {
                Map<String, Object> postValues = dashBoard.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, postValues);
                mDatabase.updateChildren(childUpdates);


                if (uri != null) {
                    for (Uri uploadPhoto : uriList) {

                        uploadPhoto(uploadPhoto);
                    }
                }



            }
            Toast.makeText(WriteBordActivity.this, "입력 완료", Toast.LENGTH_SHORT).show();
        }

    }




    // end onClickInputDashBoard

    public void uploadPhoto(Uri uploadUri){
        final String filename = date + fileNameCount + ".png";
        fileNameCount ++;

        StorageReference riversRef = storageReference.getStorage().getReference ("images/" + filename);

        riversRef.putFile(uploadUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(WriteBordActivity.this, "사진 업로드 성공", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(WriteBordActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}