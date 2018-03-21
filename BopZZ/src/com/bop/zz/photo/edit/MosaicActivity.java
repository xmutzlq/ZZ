package com.bop.zz.photo.edit;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.kdroid.photoedit.mosaic.DrawMosaicView;
import com.kdroid.photoedit.mosaic.MosaicUtil;
import com.kdroid.photoedit.utils.FileUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * 马赛克
 */
public class MosaicActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

	private DrawMosaicView mosaic;

	private String mPath;
	private int mWidth, mHeight;
	private int size = 5;
	
	private Bitmap srcBitmap = null;

	@Override
	protected void setTitle(String title) {
		super.setTitle(getResources().getString(R.string.str_back));
	}
	
	@Override
	protected View getContentView() {
		return null;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.layout_mosaic;
	}

	@Override
	protected void findViews() {
		setCenter("马赛克");
		mosaic = (DrawMosaicView) findViewById(R.id.mosaic);
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	protected void loadData() {
		Intent intent = getIntent();
		mPath = intent.getStringExtra("camera_path");
		mosaic.setMosaicBackgroundResource(mPath);

		srcBitmap = BitmapFactory.decodeFile(mPath);

		mWidth = srcBitmap.getWidth();
		mHeight = srcBitmap.getHeight();
		Bitmap bit = MosaicUtil.getMosaic(srcBitmap);

		mosaic.setMosaicResource(bit);
		mosaic.setMosaicBrushWidth(10);
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
		case R.id.action_base:
			Bitmap bitmapMosaic = MosaicUtil.getMosaic(srcBitmap);
			mosaic.setMosaicResource(bitmapMosaic);
			break;
		case R.id.action_ground_glass:
			Bitmap bitmapBlur = MosaicUtil.getBlur(srcBitmap);
			mosaic.setMosaicResource(bitmapBlur);
			break;
		case R.id.action_flower:
			Bitmap bit = BitmapFactory.decodeResource(this.getResources(), R.drawable.hi4);
			bit = FileUtils.ResizeBitmap(bit, mWidth, mHeight);
			mosaic.setMosaicResource(bit);
			break;
		case R.id.action_size:
			if (size >= 30) {
				size = 5;
			} else {
				size += 5;
			}
			mosaic.setMosaicBrushWidth(size);
			break;
		case R.id.action_eraser:
			mosaic.setMosaicType(MosaicUtil.MosaicType.ERASER);
			break;
		default:
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

			recycle();
			this.finish();
			break;
		case 2:
			Bitmap bit = mosaic.getMosaicBitmap();

			FileUtils.writeImage(bit, mPath, 100);

			Intent okData = new Intent();
			okData.putExtra("camera_path", mPath);
			setResult(RESULT_OK, okData);
			recycle();
			MosaicActivity.this.finish();
			break;
		default:

			break;
		}
	}

	private void recycle() {
		if (srcBitmap != null) {
			srcBitmap.recycle();
			srcBitmap = null;
		}
	}
}
