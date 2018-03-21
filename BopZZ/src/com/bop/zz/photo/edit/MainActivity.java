package com.bop.zz.photo.edit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.bop.zz.R;
import com.bop.zz.base.BaseActivity;
import com.bop.zz.photo.edit.core.PhotoEditController;
import com.bop.zz.photo.edit.utils.Constants;
import com.bop.zz.widget.PhotoEditModelPopWindow;
import com.bop.zz.widget.PhotoEditModelPopWindow.ItemClickListener;
import com.kdroid.photoedit.operate.OperateUtils;
import com.kdroid.photoedit.utils.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 测试首页
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ItemClickListener {

	private LinearLayout content_layout;
	private Button addPictureFromPhotoBtn;
	private Button addPictureFromCameraBtn;
	private ImageView pictureShow;
	private Button testBtn;
	private Class<?> intentClass;
	private int intentType = 0;

	private static final String EXTRA_NAME_IMG_PATH = "img_path";
	
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;

	/* 边框 */
	private static final int PHOTO_FRAME_WITH_DATA = 3024;

	/* 马赛克 */
	private static final int PHOTO_MOSAIC_WITH_DATA = 3025;

	/* 涂鸦 */
	private static final int PHOTO_DRAW_WITH_DATA = 3026;

	/* 剪切 */
	private static final int PHOTO_CROP_WITH_DATA = 3027;

	/* 滤镜 */
	private static final int PHOTO_FILTER_WITH_DATA = 3028;

	/* 增强 */
	private static final int PHOTO_ENHANCE_WITH_DATA = 3029;

	/* 旋转 */
	private static final int PHOTO_REVOLVE_WITH_DATA = 3030;

	/* 图像变形 */
	private static final int PHOTO_WARP_WITH_DATA = 3031;

	/* 添加水印图片 */
	private static final int PHOTO_ADD_WATERMARK_DATA = 3032;
	/* 添加文字 */
	private static final int PHOTO_ADD_TEXT_DATA = 3033;

	/* 测试接口 */
	private static final int PHOTO_TEST_TEXT_DATA = 3034;

	/* 照相机拍照得到的图片 */
	private File mCurrentPhotoFile;
	private String photoPath = null, tempPhotoPath, camera_path;

	private PhotoEditController mPhotoEditController;
	
	private OperateUtils operateUtils;

	private PhotoEditModelPopWindow mPhotoEditModelPopWindow;
	
	public static void openPhotoEditTestActivity(Context context, String imgPath) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra(EXTRA_NAME_IMG_PATH, imgPath);
		context.startActivity(intent);
	}
	
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
		return R.layout.activity_photo_test;
	}

	@Override
	protected void findViews() {
		setCenter("图像编辑");
		pictureShow = (ImageView) findViewById(R.id.pictureShow);
		testBtn = (Button) findViewById(R.id.testBtn);
		content_layout = (LinearLayout) findViewById(R.id.mainLayout);
		addPictureFromPhotoBtn = (Button) findViewById(R.id.addPictureFromPhoto);
		addPictureFromCameraBtn = (Button) findViewById(R.id.addPictureFromCamera);
	}

	@Override
	protected void setListener() {
		testBtn.setOnClickListener(this);
		addPictureFromPhotoBtn.setOnClickListener(this);
		addPictureFromCameraBtn.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		operateUtils = new OperateUtils(this);
		String imgPath = getIntent().getStringExtra(EXTRA_NAME_IMG_PATH);
		Bitmap resizeBmp = operateUtils.compressionFiller(imgPath, content_layout);
		mPhotoEditModelPopWindow = new PhotoEditModelPopWindow(mTitleRight, this);
		pictureShow.setImageBitmap(resizeBmp);
		camera_path = SaveBitmap(resizeBmp, "saveTemp");
		photoPath = imgPath;
		
		mPhotoEditController = new PhotoEditController(this, pictureShow, camera_path);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPhotoEditController.editPhoto(PhotoEditController.EDIT_TYPE_FILTER);
				mPhotoEditController.editPhoto(PhotoEditController.EDIT_TYPE_FRAME);
			}
		}, 2000);
	}
	
	@Override
	public void onBackPressed() {
		if(!mPhotoEditController.backToPre()) {
			super.onBackPressed();
		}
	}
	
	@Override
	protected View buildTitleRight() {
		ImageView textView = new ImageView(this);
		textView.setPadding(10, 30, 10, 30);
		textView.setId(2016);
		textView.setBackgroundResource(R.drawable.top_more);
		textView.setOnClickListener(this);
		return textView;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.addPictureFromCamera:
			getPictureFormCamera();
			break;
		case R.id.testBtn:
			if (photoPath == null) {
				Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
				return;
			}
			if (intentClass == null) {
				Toast.makeText(MainActivity.this, "请图片操作类型", Toast.LENGTH_SHORT).show();
				return;
			}
			// 将图片路径photoPath传到所要调试的模块
			Intent photoFrameIntent = new Intent(MainActivity.this, intentClass);
			photoFrameIntent.putExtra("camera_path", camera_path);
			MainActivity.this.startActivityForResult(photoFrameIntent, intentType);
			break;
		case 2016:
			mPhotoEditModelPopWindow.showAsDropDown(0, 30);
			break;
		}
	}

	/* 从相机中获取照片 */
	private void getPictureFormCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		tempPhotoPath = FileUtils.DCIMCamera_PATH + FileUtils.getNewFileName() + ".jpg";

		mCurrentPhotoFile = new File(tempPhotoPath);

		if (!mCurrentPhotoFile.exists()) {
			try {
				mCurrentPhotoFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}

	private void compressed() {
		Bitmap resizeBmp = operateUtils.compressionFiller(photoPath, content_layout);
		pictureShow.setImageBitmap(resizeBmp);
		camera_path = SaveBitmap(resizeBmp, "saveTemp");
	}

	final Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (content_layout.getWidth() != 0) {
					Log.i("LinearLayoutW", content_layout.getWidth() + "");
					Log.i("LinearLayoutH", content_layout.getHeight() + "");
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

	// 将生成的图片保存到内存中
	public String SaveBitmap(Bitmap bitmap, String name) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(Constants.filePath);
			if (!dir.exists())
				dir.mkdir();
			File file = new File(Constants.filePath + name + ".jpg");
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
					out.flush();
					out.close();
				}
				return file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case CAMERA_WITH_DATA:

			photoPath = tempPhotoPath;
			if (content_layout.getWidth() == 0) {
				timer.schedule(task, 10, 1000);
			} else {
				compressed();
			}

			break;

		case PHOTO_FRAME_WITH_DATA:
		case PHOTO_MOSAIC_WITH_DATA:
		case PHOTO_DRAW_WITH_DATA:
		case PHOTO_CROP_WITH_DATA:
		case PHOTO_FILTER_WITH_DATA:
		case PHOTO_ENHANCE_WITH_DATA:
		case PHOTO_REVOLVE_WITH_DATA:
		case PHOTO_WARP_WITH_DATA:
		case PHOTO_ADD_WATERMARK_DATA:
		case PHOTO_ADD_TEXT_DATA:
		case PHOTO_TEST_TEXT_DATA:

			String resultPath = data.getStringExtra("camera_path");
			Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
			pictureShow.setImageBitmap(resultBitmap);
			break;

		default:
			break;
		}

	}

	@Override
	public void onPopItemClick(MenuItem item) {
		if (photoPath == null) {
			Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
			return ;
		}

		switch (item.getItemId()) {
		case R.id.action_filter: //滤镜
			intentClass = ImageFilterActivity.class;
			intentType = PHOTO_FILTER_WITH_DATA;
			mPhotoEditController.editPhoto(PhotoEditController.EDIT_TYPE_FILTER);
			break;
		case R.id.action_wrap: //图像变形
			intentClass = WarpActivity.class;
			intentType = PHOTO_WARP_WITH_DATA;
			break;
		case R.id.action_crop: //剪切
			intentClass = ImageCropActivity.class;
			intentType = PHOTO_CROP_WITH_DATA;
			break;
		case R.id.action_draw: //涂鸦
			intentClass = DrawBaseActivity.class;
			intentType = PHOTO_DRAW_WITH_DATA;
			break;
		case R.id.action_frame: //添加相框
			intentClass = PhotoFrameActivity.class;
			intentType = PHOTO_FRAME_WITH_DATA;
			break;
		case R.id.action_addtv: //添加文字
			intentClass = AddTextActivity.class;
			intentType = PHOTO_ADD_TEXT_DATA;
			break;
		case R.id.action_addwm: //添加水印
			intentClass = AddWatermarkActivity.class;
			intentType = PHOTO_ADD_WATERMARK_DATA;
			break;
		case R.id.action_mosaic: //马赛克
			intentClass = MosaicActivity.class;
			intentType = PHOTO_MOSAIC_WITH_DATA;
			break;
		case R.id.action_enchance: //增强
			intentClass = EnhanceActivity.class;
			intentType = PHOTO_ENHANCE_WITH_DATA;
			break;
		case R.id.action_rotate: //翻转
			intentClass = RevolveActivity.class;
			intentType = PHOTO_REVOLVE_WITH_DATA;
			break;
		default:
			intentClass = null;
			intentType = 0;
			break;
		}

		
		
//		Toast.makeText(MainActivity.this, "请点击测试按钮", Toast.LENGTH_SHORT).show();
	}
}
