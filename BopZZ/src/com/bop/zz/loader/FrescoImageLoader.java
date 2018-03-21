package com.bop.zz.loader;

import com.bop.zz.photo.widget.GFImageView;
import com.bop.zz.photo.widget.photodraweeview.PhotoDraweeView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class FrescoImageLoader implements com.bop.zz.photo.FrescoImageLoader {

	private Context context;

	public FrescoImageLoader(Context context) {
		this(context, Bitmap.Config.RGB_565);
	}

	public FrescoImageLoader(Context context, Bitmap.Config config) {
		this.context = context;
		ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context).setDownsampleEnabled(true)
				.setBitmapsConfig(config).build();
		Fresco.initialize(context, imagePipelineConfig);
	}

	@Override
	public void displayImage(Activity activity, String path, final PhotoDraweeView imageView,
			final Drawable defaultDrawable, final int width, final int height) {
		
		Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
		PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
		controller.setUri(uri);
		controller.setOldController(imageView.getController());
		controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
		    @Override
		    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
		        super.onFinalImageSet(id, imageInfo, animatable);
		        if (imageInfo == null || imageView == null) {
		            return;
		        }
		        imageView.update(imageInfo.getWidth(), imageInfo.getHeight());
		    }
		});
		imageView.setController(controller.build());
	}

	@Override
	public void clearMemoryCache() {

	}

	@Override
	public void displayImage(Activity activity, String path, final GFImageView imageView, final Drawable defaultDrawable, int width,
			int height) {
		Resources resources = context.getResources();
		GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources).setFadeDuration(500)
				.setPlaceholderImage(defaultDrawable).setFailureImage(defaultDrawable).build();

		final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);
		imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
			@Override
			public void onDetach() {
				draweeHolder.onDetach();
			}

			@Override
			public void onAttach() {
				draweeHolder.onAttach();
			}

			@Override
			public boolean verifyDrawable(Drawable dr) {
				if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
					return true;
				}
				return false;
			}

			@Override
			public void onDraw(Canvas canvas) {
				Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
				if (drawable == null) {
					imageView.setImageDrawable(defaultDrawable);
				} else {
					imageView.setImageDrawable(drawable);
				}
			}
		});
		Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
		ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
				.setResizeOptions(new ResizeOptions(width, height))// 图片目标大小
				.build();
		DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeHolder.getController())
				.setImageRequest(imageRequest).setAutoPlayAnimations(false).build();
		draweeHolder.setController(controller);
	}
}
