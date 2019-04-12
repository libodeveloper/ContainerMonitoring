package com.esri.arcgisruntime.container.monitoring.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;


/**
 * 底部弹出框 ClassName: ActionSheet
 */
public class ActionSheet
{

	public final static int PHOTO = 0;
	public final static int CAMERA = 1;


	public interface OnActionSheetSelected
	{
		void onWhichClick(int whichButton);
	}
	public interface OnActionSheetSelectedZhihuan
	{
		void onClickZhihuan(int whichButton);
	}

	private ActionSheet()
	{
	}

	public static Dialog showSheet(Context context,
                                   final OnActionSheetSelected actionSheetSelected,
                                   String content1,
                                   String content2,
                                   String cancel)
	{
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView mTitle = (TextView) layout.findViewById(R.id.title);
		mTitle.setText(content1);
		TextView mContent = (TextView) layout.findViewById(R.id.content);
		mContent.setText(content2);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		mCancel.setText(cancel);

		mTitle.setOnClickListener(new OnClickListener()
	    {

			@Override
			public void onClick(View v)
			{
				actionSheetSelected.onWhichClick(PHOTO);
				dlg.dismiss();
			}
		});
		mContent.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				actionSheetSelected.onWhichClick(CAMERA);
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
