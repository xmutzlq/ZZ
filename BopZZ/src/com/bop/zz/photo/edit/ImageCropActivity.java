package com.bop.zz.photo.edit;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.kdroid.photoedit.crop.CropImageType;
import com.kdroid.photoedit.crop.CropImageView;
import com.kdroid.photoedit.utils.FileUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 剪切
 */
public class ImageCropActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

	private CropImageView cropImage;

	private String mPath = null;

	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.crop_image;
	}

	@Override
	protected void findViews() {
		cropImage = (CropImageView) findViewById(R.id.cropmageView);
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void loadData() {
		Intent intent = getIntent();

		mPath = intent.getStringExtra("camera_path");
		Bitmap bit = BitmapFactory.decodeFile(mPath);

		Bitmap hh = BitmapFactory.decodeResource(this.getResources(), R.drawable.crop_button);

		cropImage.setCropOverlayCornerBitmap(hh);
		cropImage.setImageBitmap(bit);

		// Bitmap bit =
		// BitmapFactory.decodeResource(this.getResources(),R.drawable.hi0);

		cropImage.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格

		cropImage.setFixedAspectRatio(false);// 自由剪切
	}

	@Override
	protected View buildTitleRight() {
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ImageButton ib1 = new ImageButton(this);
		ib1.setId(1);
		ib1.setBackgroundResource(R.drawable.btn_view_back);
		ImageButton ib2 = new ImageButton(this);
		ib2.setId(2);
		ib2.setBackgroundResource(R.drawable.btn_view_ok);
		ll.addView(ib1);
		ll.addView(ib2);
		return ll;
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_freedom:
			cropImage.setFixedAspectRatio(false);
			break;
		case R.id.action_1_1:
			cropImage.setFixedAspectRatio(true);
			cropImage.setAspectRatio(10, 10);
			break;
		case R.id.action_3_2:
			cropImage.setFixedAspectRatio(true);
			cropImage.setAspectRatio(30, 20);
			break;

		case R.id.action_4_3:
			cropImage.setFixedAspectRatio(true);
			cropImage.setAspectRatio(40, 30);
			break;
		case R.id.action_16_9:
			cropImage.setFixedAspectRatio(true);
			cropImage.setAspectRatio(160, 90);
			break;
		case R.id.action_rotate:
			cropImage.rotateImage(90);
			break;
		case R.id.action_up_down:
			cropImage.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);
			break;
		case R.id.action_left_right:
			cropImage.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);
			break;
		case R.id.action_crop:
			Bitmap cropImageBitmap = cropImage.getCroppedImage();
			Toast.makeText(this, "已保存到相册；剪切大小为 " + cropImageBitmap.getWidth() + " x " + cropImageBitmap.getHeight(),
					Toast.LENGTH_SHORT).show();
			FileUtils.saveBitmapToCamera(this, cropImageBitmap, "crop.jpg");
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case 1:
			Intent cancelData = new Intent();
			setResult(RESULT_CANCELED, cancelData);
			this.finish();
			break;
		case 2:
			Bitmap bit = cropImage.getCroppedImage();
			FileUtils.writeImage(bit, mPath, 100);

			Intent okData = new Intent();
			okData.putExtra("camera_path", mPath);
			setResult(RESULT_OK, okData);
			this.finish();
			break;
		default:

			break;
		}
	}
}
