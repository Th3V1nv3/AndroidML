package com.facilesales.facilesalesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facilesales.facilesalesapp.database.AppDatabase;
import com.facilesales.facilesalesapp.pojos.AvailProduct;
import com.facilesales.facilesalesapp.database.AvailProductsDao;
import com.facilesales.facilesalesapp.ml.Model;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ProductManager extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.facilesales.facilesalesapp.REPLY";
    public static final String INPUT_REPLY = "com.facilesales.facilesalesapp.INPUT";
    private AppDatabase db;
    private AvailProductsDao apDao;
    private TextView result,confidence;
    private Button capture;
    private ImageView imageView;
    private FloatingActionButton fab;
    private int imageSize = 224;
    private AvailProduct availProduct;
    private int maxPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);

        result = (TextView)findViewById(R.id.result);
        capture = (Button)findViewById(R.id.capture);
        fab = (FloatingActionButton)findViewById(R.id.fab2);
        imageView = (ImageView)findViewById(R.id.imageView2);
        availProduct = new AvailProduct();
        maxPos = -1;


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        // Start of the Modal bottom sheet code
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent replyIntent = new Intent();
                String[] values = {"4444","5555","8888","3333"};
                replyIntent.putExtra(INPUT_REPLY, values[maxPos]);
                setResult(RESULT_OK, replyIntent);

                finish();

            }

        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            maxPos = -1;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Rectangle", "Star", "Cross", "Triangle"};
            result.setText(classes[maxPos]);

            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void showAddNewDialog()
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_inputs);


        EditText id = (EditText)bottomSheetDialog.findViewById(R.id.enterid);
        EditText name = (EditText)bottomSheetDialog.findViewById(R.id.enterName);
        EditText cost = (EditText)bottomSheetDialog.findViewById(R.id.enterCost);
        EditText price = (EditText)bottomSheetDialog.findViewById(R.id.enterPrice);
        EditText quantity = (EditText)bottomSheetDialog.findViewById(R.id.enterQuantity);
        Button save = (Button)bottomSheetDialog.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                availProduct.setName(name.getText().toString());
                availProduct.setId(Integer.parseInt(id.getText().toString()));
                availProduct.setQuantity(Integer.parseInt(id.getText().toString()));
                availProduct.setCost(Double.parseDouble(cost.getText().toString()));
                availProduct.setSellingPrice(Double.parseDouble(cost.getText().toString()));

                // this line closes the dialog
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}