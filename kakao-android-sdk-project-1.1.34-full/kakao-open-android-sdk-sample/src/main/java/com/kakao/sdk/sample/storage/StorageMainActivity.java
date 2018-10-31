package com.kakao.sdk.sample.storage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.BaseActivity;
import com.kakao.sdk.sample.common.widget.KakaoToast;
import com.kakao.storage.StorageService;
import com.kakao.storage.response.ImageUploadResponse;
import com.kakao.util.helper.MediaUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author leoshin on 15. 9. 8.
 */
public class StorageMainActivity extends BaseActivity implements OnClickListener {

    private static final int PICK_FROM_GALLERY = 0;
    private Uri uri = null;
    private Button uploadBtn = null;
    private TextView resultTextView = null;
    private ImageView selectedImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_storage_main);

        ((TextView)findViewById(R.id.text_title)).setText(getString(R.string.text_storage));
        uploadBtn = (Button)findViewById(R.id.upload_image);
        resultTextView = (TextView)findViewById(R.id.result);
        selectedImageView = (ImageView)findViewById(R.id.selected_image);

        uploadBtn.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.pick_images).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            uri = data.getData();
            selectedImageView.setImageURI(uri);
            uploadBtn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.pick_images:
                selectedImageView.setImageDrawable(null);
                resultTextView.setText("");

                startImageGallery(this, PICK_FROM_GALLERY);
                break;
            case R.id.upload_image:
                requestUploadImage();
                break;
        }
    }

    private static void startImageGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    private void requestUploadImage() {
        try {
            if (uri == null) {
                throw new FileNotFoundException("File is not Exist!!");
            }

            final File file = new File(MediaUtils.getImageFilePathFromUri(uri, this));
            if (file == null || !file.exists()) {
                throw new FileNotFoundException("File is not Exist!!");
            }

            StorageService.requestImageUpload(new ApiResponseCallback<ImageUploadResponse>() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    redirectLoginActivity();
                }

                @Override
                public void onNotSignedUp() {
                    redirectSignupActivity();
                }

                @Override
                public void onFailure(ErrorResult errorResult) {
                    KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(ImageUploadResponse result) {
                    resultTextView.setText(result.toString());
                    KakaoToast.makeToast(getApplicationContext(), "success to upload image", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDidEnd() {
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                    uploadBtn.setEnabled(false);
                }
            }, file);
        } catch (FileNotFoundException e) {
            KakaoToast.makeToast(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
