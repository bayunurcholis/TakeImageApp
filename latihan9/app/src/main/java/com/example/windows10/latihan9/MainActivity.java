package com.example.windows10.latihan9;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button b_gallery_view, b_choose_image;
    private ImageView imageView;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIR = "/Gambar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Ambil Gambar");
        b_gallery_view = (Button) findViewById(R.id.btn_view_gambar_list);
        b_choose_image = (Button) findViewById(R.id.btn_ambil_gambar);
        imageView = (ImageView) findViewById(R.id.imgv_gambar);
        b_choose_image.setOnClickListener(this);
        b_gallery_view.setOnClickListener(this);

        requestMultiplePermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ambil_gambar:
                showImageDialog();
                break;
            case R.id.btn_view_gambar_list:
                Intent viewgalery = new Intent(MainActivity.this, GalleryPhoto.class);
                startActivity(viewgalery);
                break;
        }
    }

    private void showImageDialog() {
        AlertDialog.Builder picturedialog = new AlertDialog.Builder(this);
        picturedialog.setTitle("Select Action");
        String[] picturedialogitems = {
                "Select photo from Gallery",
                "Capture photo from Camera"
        };
        picturedialog.setItems(picturedialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        chooseGallery();
                        break;
                    case 1:
                        chooseCamera();
                }
            }
        });
        picturedialog.show();
    }

    private void chooseCamera() {
        Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(icamera, CAMERA);
    }

    private void chooseGallery() {
        Intent igallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(igallery, GALLERY);
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permission are grantes by user !", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error !", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIR);
if (!wallpaperDirectory.exists())
{
    wallpaperDirectory.mkdirs();
}
try{
    File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
    f.createNewFile();
    FileOutputStream fo = new FileOutputStream(f);
    fo.write(bytes.toByteArray());
    MediaScannerConnection.scanFile(this,
            new String[]{f.getPath()},
            new String[]{"image/jpeg"}, null);
    fo.close();
    Log.d("TAG_IMAGE","File Saved ::---&get;" + f.getAbsolutePath());
    return f.getAbsolutePath();

}
catch (IOException el)
{
    el.printStackTrace();
}
return  "";
    }

    @Override
    protected void onActivityResult(int requesCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requesCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED)
        {
            return;
        }
        if (requesCode == GALLERY)
        {
            if (data != null)
            {
                Uri contentURI = data.getData();
                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Log.d("TAG_IMAGE","File Saved ::---&get;"+ path);
                    Toast.makeText(MainActivity.this, "Image Saved!",Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
