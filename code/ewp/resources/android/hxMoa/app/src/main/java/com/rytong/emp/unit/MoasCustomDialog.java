package com.rytong.emp.unit;

import com.ghbank.moas.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;

public class MoasCustomDialog extends Dialog{

	public MoasCustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MoasCustomDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public void disMiss() {
		cancel();
	}
	
	public static class Builder {
		private Context mContext;
		private String mTitle;
		private String mMessage;
		private String positive;
		private String negative;
		private DialogInterface.OnClickListener positiveListener;
		private DialogInterface.OnClickListener negativeListener;
		private LayoutInflater mInflater;

		public Builder(Context context) {
			this.mContext = context;
			if (mInflater == null) {
				mInflater = LayoutInflater.from(mContext);
			}
		}

		/**
		 * 设置dialog内容
		 */
		public Builder setMessage(int id) {
			this.mMessage = mContext.getText(id).toString();
			return this;
		}

		/**
		 * 设置dialog内容
		 */
		public Builder setMessage(String message) {
			this.mMessage = message;
			return this;
		}

		/**
		 * 设置dialog标题
		 */
		public Builder setTitle(int id) {
			this.mTitle = mContext.getText(id).toString();
			return this;
		}

		/**
		 * 设置dialog标题
		 */
		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		/**
		 * 设置dialog布局文件
		 */
		public Builder setContentView(View v) {
			return this;
		}

		/**
		 * 设置肯定按钮文字及按钮监�?
		 */
		public Builder setPositiveButton(int id,
				DialogInterface.OnClickListener listener) {
			this.positive = mContext.getText(id).toString();
			this.positiveListener = listener;
			return this;
		}

		/**
		 * 设置肯定按钮文字及按钮监�?
		 */
		public Builder setPositiveButton(String text,
				DialogInterface.OnClickListener listener) {
			this.positive = text;
			this.positiveListener = listener;
			return this;
		}

		/**
		 * 设置否按钮文字及按钮监听
		 */
		public Builder setNegativeButton(int id,
				DialogInterface.OnClickListener listener) {
			this.negative = mContext.getText(id).toString();
			this.negativeListener = listener;
			return this;
		}

		/**
		 * 设置否按钮文字及按钮监听
		 */
		public Builder setNegativeButton(String text,
				DialogInterface.OnClickListener listener) {
			this.negative = text;
			this.negativeListener = listener;
			return this;
		}

		public MoasCustomDialog listDialog(View view) {
			final MoasCustomDialog dialog = new MoasCustomDialog(mContext,
					R.style.dialogselect);

			dialog.addContentView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			Window window = dialog.getWindow();
			int width = (int) (MoasUnit.width_ * 0.9);
			int height = ViewGroup.LayoutParams.WRAP_CONTENT;
			window.setLayout(width, height);
			window.setGravity(Gravity.CENTER);
			dialog.setContentView(view);
			dialog.setCancelable(false);
			return dialog;
		}

	}
}
