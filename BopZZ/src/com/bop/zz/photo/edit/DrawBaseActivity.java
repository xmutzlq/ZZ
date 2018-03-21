package com.bop.zz.photo.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.kdroid.photoedit.operate.OperateUtils;
import com.kdroid.photoedit.scrawl.DrawingBoardView;
import com.kdroid.photoedit.scrawl.ScrawlTools;
import com.kdroid.photoedit.utils.FileUtils;

/**
 * 涂鸦
 */
public class DrawBaseActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

	private DrawingBoardView drawView;
	private ScrawlTools casualWaterUtil = null;
	private LinearLayout drawLayout;
	private String mPath;

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
		return R.layout.layout_draw;
	}

	@Override
	protected void findViews() {
		setCenter("涂鸦");
		drawView = (DrawingBoardView) findViewById(R.id.drawView);
		drawLayout = (LinearLayout) findViewById(R.id.drawLayout);
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void loadData() {
		Intent intent = getIntent();
		mPath = intent.getStringExtra("camera_path");
		timer.schedule(task, 10, 1000);
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

	final Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (drawLayout.getWidth() != 0) {
					// Log.i("jarlen", drawLayout.getWidth() + "");
					// Log.i("jarlen", drawLayout.getHeight() + "");
					// 取消定时器
					timer.cancel();
					compressed();
				}
			}
		}
	};

	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			myHandler.sendMessage(message);
		}
	};

	private void compressed() {

		OperateUtils operateUtils = new OperateUtils(this);

		// Bitmap bit = BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.river);
		Bitmap bit = BitmapFactory.decodeFile(mPath);

		Bitmap resizeBmp = operateUtils.compressionFiller(bit, drawLayout);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resizeBmp.getWidth(),
				resizeBmp.getHeight());

		drawView.setLayoutParams(layoutParams);

		casualWaterUtil = new ScrawlTools(this, drawView, resizeBmp);

		Bitmap paintBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.crayon);

		// int[] res = new int[]{
		// R.drawable.stamp0star,R.drawable.stamp1star,R.drawable.stamp2star,R.drawable.stamp3star
		// };

		casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_WATER, paintBitmap,
				0xffadb8bd);

		// casualWaterUtil.creatStampPainter(DrawAttribute.DrawStatus.PEN_STAMP,res,0xff00ff00);

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_paint_one:
			Bitmap paintBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
			casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_WATER,
					paintBitmap1, 0xffadb8bd);
			break;
		case R.id.action_paint_two:
			Bitmap paintBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.crayon);
			casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_CRAYON,
					paintBitmap2, 0xffadb8bd);
			break;
		case R.id.action_size:
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 2;
			Bitmap paintBitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker, option);
			casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_WATER,
					paintBitmap3, 0xffadb8bd);
			break;
		case R.id.action_eraser:
			Bitmap paintBitmap6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.eraser);

			casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_ERASER,
					paintBitmap6, 0xffadb8bd);
			break;
		case R.id.action_color:
			Bitmap paintBitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);
			casualWaterUtil.creatDrawPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_WATER,
					paintBitmap4, 0xff002200);
			break;

		case R.id.action_pic:
			int[] res = new int[] { R.drawable.stamp0star, R.drawable.stamp1star, R.drawable.stamp2star,
					R.drawable.stamp3star };

			casualWaterUtil.creatStampPainter(com.kdroid.photoedit.scrawl.DrawAttribute.DrawStatus.PEN_STAMP, res,
					0xff00ff00);
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

			this.finish();

			break;
		case 2:
			Bitmap bit = casualWaterUtil.getBitmap();

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
