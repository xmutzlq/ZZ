package com.bop.zz.photo.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TextUtil {

	public static SpannableString formatTextColorBySpannableString(final Context context, String text, int color,
			int start, int end) {
		return formatTextColorBySpannableString(context, text, color, start, end, null, true, true);
	}

	@SuppressWarnings("deprecation")
	public static SpannableString formatTextColorBySpannableString(final Context context, String text, int color,
			int start, int end, final OnClickListener listener, boolean enableTextSize, boolean enableTextBlod) {
		SpannableString ss = new SpannableString(text);
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(enableTextSize) {
			ss.setSpan(new AbsoluteSizeSpan(20, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if(enableTextBlod) {
			ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new TypefaceSpan("serif"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    
		}
		if (listener != null) {
			ss.setSpan(new NoLineCllikcSpan() {

				@Override
				public void onClick(View widget) {
					if (listener != null) {
						listener.onClick(widget);
					}
					if (widget instanceof TextView) {
						((TextView) widget)
								.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
					}
				}
			}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}

	public static class NoLineCllikcSpan extends ClickableSpan {

		public NoLineCllikcSpan() {
			super();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			/** set textColor **/
			ds.setColor(ds.linkColor);
			/** Remove the underline **/
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {
		}
	}
	
	public static String getPhoneNumSecreatValue(String phoneNum) {
		return phoneNum.replaceAll("(\\d{4})\\d{3}(\\d{4})", "$1***$2");
	}
	
}
