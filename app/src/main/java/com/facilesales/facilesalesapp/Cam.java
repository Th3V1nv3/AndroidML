package com.facilesales.facilesalesapp;

import static com.facilesales.facilesalesapp.ProductManager.EXTRA_REPLY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facilesales.facilesalesapp.database.AvailProduct;
import com.facilesales.facilesalesapp.database.AvailProductRepository;
import com.facilesales.facilesalesapp.database.ProductViewModel;
import com.facilesales.facilesalesapp.databinding.ActivityCamBinding;
import com.facilesales.facilesalesapp.databinding.ActivityMainBinding;
import com.facilesales.facilesalesapp.ml.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class Cam extends AppCompatActivity {
    private ActivityCamBinding binding;
    private TextView result, confidence;
    private ImageView imageView;
    private Button picture;
    private FloatingActionButton fab;
    private int imageSize = 224;
    private final int REQUEST_CODE_CAM = 1;
    public final String CAM_EXTRA_NAME = "com.facilesales.facilesalesapp.CAM";
    private ProductViewModel viewModel;
    private com.facilesales.facilesalesapp.pojos.AvailProduct productFound;
    private List<com.facilesales.facilesalesapp.pojos.AvailProduct> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = binding.imageView2;
        result = binding.result;
        picture = binding.button;
        fab = binding.go;
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        picture.setOnClickListener(new View.OnClickListener() {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(CAM_EXTRA_NAME,productFound);
                Toast.makeText(getApplicationContext(),"Added product "+productFound.getId(),Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }
    public void classifyImage(Bitmap image){
        String[] classes = {"Rectangle", "Star", "Cross", "Triangle"};
        String[] values = {"4444","5555","8888","3333"};
        String displayableResults = "";
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
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            if((int)confidences[maxPos] < 65) {

                displayableResults = classes[maxPos];
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please re-take picture",Toast.LENGTH_SHORT).show();
                displayableResults = "Keep camera close to the image and make sure the image is clear";
            }

            result.setText(displayableResults);
            int myId = Integer.parseInt(values[maxPos]);
            model.close();

            if(isProductAvailable(myId)){
                fab.setVisibility(View.VISIBLE);

            }
            else{
                Toast.makeText(getApplicationContext(),"Product not available",Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_LONG).show();
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


    public boolean isProductAvailable(int id)
    {
        AvailProduct availProduct = viewModel.getProduct(id);

        if(availProduct == null){
            return false;
        }else{
            productFound =
                    new com.facilesales.facilesalesapp.pojos.AvailProduct(
                            availProduct.getId(),availProduct.getName(),availProduct.cost,availProduct.sellingPrice,1,availProduct.quantity);
            return true;
        }
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