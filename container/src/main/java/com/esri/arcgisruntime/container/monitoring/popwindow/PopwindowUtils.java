package com.esri.arcgisruntime.container.monitoring.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.PopWindowAdapter;
import com.esri.arcgisruntime.container.monitoring.adapter.SearchPopWindowAdapter;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.SearchNumberBean;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.ui.activity.MainActivity;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.MyNumberKeyListener;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.view.CustomDividerItemDecoration;

import java.util.List;


/**
 * Created by 李波 on 2017/1/20.
 *
 * popwindow
 */

public class PopwindowUtils {
	private static PopupWindow popupWindow=null;
	private static int type;  //0 - 集装箱编号 1 - 关锁编号


	/**
	 * Created by 李波 on 2019/3/8.
	 * 下拉选项的popwindow
	 */
	public static  void PullDownPopWindow(final Context context, View view, List<String> data, OnClickNumberType onClickNumberType) {

		if (popupWindow == null) {
			//加载popwindow布局
			View contentView = View.inflate(context,R.layout.popwindow, null);
			//设置布局里各种控件功能

			RecyclerView recyclerView = contentView.findViewById(R.id.rvPopwindow);
			PopWindowAdapter popWindowAdapter = new PopWindowAdapter(context,data);
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
			recyclerView.addItemDecoration(new CustomDividerItemDecoration(context, CustomDividerItemDecoration.VERTICAL_LIST));
			recyclerView.setAdapter(popWindowAdapter);
			popWindowAdapter.setOnItemClickListener(new PopWindowAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int pos) {
//					MyToast.showLong(data.get(pos));
					onClickNumberType.onNumberType(data.get(pos),pos);
					dimssWindow();
				}
			});

			//每个item高度为 40dp
			int popHeight = SizeUtils.dp2px(context, data.size()*40)+data.size();
			int maxH = SizeUtils.dp2px(context, 6*40)+6;
			popHeight = popHeight >= maxH ? maxH : popHeight;
			//初始化pop 注意：popwindow最好指定固定大小，否则无法显示，不能以为布局设置了大小就没事了。
			//因为布局这时还没加载不知道大小,如果设置成-2 包裹 将造成无法显示问题
			popupWindow = new PopupWindow(contentView, -1,popHeight, true);
			//注意了，popupwindow 播放动画的话，需要加上背景
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			//设置pop获取焦点，否则嵌套在它里面的listview的setOnItemClickListener无法获取焦点响应
			popupWindow.setFocusable(true);


			// 坐标，把view的坐标设置到传递进来的数组里
			int[] location = new int[2];
			view.getLocationInWindow(location);


			//取出坐标，设置popupwindow的位置（考虑的时候要算上状态栏，因为是****以全屏做为基础来算的绝对位置***）
			popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], view.getHeight()+location[1]);

			View viewbg = ((MainActivity)context).getViewbg();
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewbg.getLayoutParams();
			layoutParams.height = ScreenUtils.getScreenHeight(context)-(view.getHeight()+location[1]);
			viewbg.setLayoutParams(layoutParams);

			((MainActivity)context).getViewbg().setVisibility(View.VISIBLE);

			//设置popwindow关闭时的监听
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
						popupWindow = null;
					//让屏幕回复不透明状态
//					backgroundAlpha((Activity) context, 1f);
					((MainActivity)context).getViewbg().setVisibility(View.GONE);
					layoutParams.height = ScreenUtils.getScreenHeight(context);
					viewbg.setLayoutParams(layoutParams);
				}
			});

			//显示popwindow时让屏幕半透明，到达遮罩层的效果
//			backgroundAlpha((Activity) context, 0.5f);

		}
	}


	/**
	 * Created by 李波 on 2019/3/8.
	 * 编号搜索
	 */
	public static  void popWindowQueryNumber(final Context context, View view, int flag, NumberCache numberCahche, OnCallBackNumberType onCallBackNumberType) {

		if (popupWindow == null) {
			type = flag;
			//加载popwindow布局
			View contentView = View.inflate(context,R.layout.query_number_layout, null);
			//设置布局里各种控件功能
			ImageView ivback = contentView.findViewById(R.id.ivBack);
			EditText  etNumber = contentView.findViewById(R.id.etNumber);
			ImageView ivDelete = contentView.findViewById(R.id.ivDelete);
			TextView tvSearch = contentView.findViewById(R.id.tvSearch);
			TextView tvContainerNumber = contentView.findViewById(R.id.tvContainerNumber);
			TextView tvLockNumber = contentView.findViewById(R.id.tvLockNumber);
			TextView tvDelAll = contentView.findViewById(R.id.tvDelAll);
			RecyclerView recyclerView = contentView.findViewById(R.id.rvSearchHistory);

			//设置限制字符 只能是数字 和 英文
			etNumber.setKeyListener(new MyNumberKeyListener());

			List<SearchNumberBean.RowsBean> data = null;

			if (type==0)
				data = numberCahche.getContainerRows();
			else if (type ==1)
				data = numberCahche.getLockRows();


			SearchPopWindowAdapter popWindowAdapter = new SearchPopWindowAdapter(context,data,type);
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
			recyclerView.addItemDecoration(new CustomDividerItemDecoration(context, CustomDividerItemDecoration.VERTICAL_LIST));
			recyclerView.setAdapter(popWindowAdapter);

			tvDelAll.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);
					popWindowAdapter.setDataLists(null,0);
				}
			});

			ivback.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onCallBackNumberType.dimssPop(etNumber,type);
					dimssWindow();
				}
			});

			ivDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					etNumber.setText("");
				}
			});

			tvSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						String number = etNumber.getText().toString().trim();
						onCallBackNumberType.search(number, type,popWindowAdapter);
				}
			});

			tvContainerNumber.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					type = 0;
					tvContainerNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size1));
					tvContainerNumber.setTextColor(context.getResources().getColor(R.color.black));

					tvLockNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size2));
					tvLockNumber.setTextColor(context.getResources().getColor(R.color.gray));

					List<SearchNumberBean.RowsBean> data = numberCahche.getContainerRows();
					popWindowAdapter.setDataLists(data,type);

				}
			});


			tvLockNumber.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					type = 1;
					tvContainerNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size2));
					tvContainerNumber.setTextColor(context.getResources().getColor(R.color.gray));

					tvLockNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size1));
					tvLockNumber.setTextColor(context.getResources().getColor(R.color.black));

					List<SearchNumberBean.RowsBean> data = numberCahche.getLockRows();

					popWindowAdapter.setDataLists(data,type);

				}
			});

			popWindowAdapter.setOnItemClickListener(new SearchPopWindowAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int pos,SearchNumberBean.RowsBean rowsBean) {
					onCallBackNumberType.onclickSearchHistory(rowsBean,type);
					dimssWindow();
				}
			});

			if (type == 0){ //选中的是集装箱编号
				tvContainerNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size1));
				tvContainerNumber.setTextColor(context.getResources().getColor(R.color.black));

				tvLockNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size2));
				tvLockNumber.setTextColor(context.getResources().getColor(R.color.gray));
			}else if (type == 1){//选中的是关锁编号
				tvContainerNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size2));
				tvContainerNumber.setTextColor(context.getResources().getColor(R.color.gray));

				tvLockNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.title_size1));
				tvLockNumber.setTextColor(context.getResources().getColor(R.color.black));
			}

			//每个item高度为 40dp
//			int popHeight = SizeUtils.dp2px(context, 440+164)+11;
			int popHeight = (int)(ScreenUtils.getScreenHeight(context)*0.7);
			//初始化pop 注意：popwindow最好指定固定大小，否则无法显示，不能以为布局设置了大小就没事了。
			//因为布局这时还没加载不知道大小,如果设置成-2 包裹 将造成无法显示问题
//			popupWindow = new PopupWindow(contentView, -1,popHeight, true);
			popupWindow = new PopupWindow(contentView, -1,-1, true);
			//注意了，popupwindow 播放动画的话，需要加上背景
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			//设置pop获取焦点，否则嵌套在它里面的listview的setOnItemClickListener无法获取焦点响应
			popupWindow.setFocusable(true);


			// 坐标，把view的坐标设置到传递进来的数组里
			int[] location = new int[2];
			view.getLocationInWindow(location);

			//取出坐标，设置popupwindow的位置（考虑的时候要算上状态栏，因为是****以全屏做为基础来算的绝对位置***）
			popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, ScreenUtils.getStatusBarHeight(context));

			onCallBackNumberType.onshow(etNumber);

			//设置popwindow关闭时的监听
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
						popupWindow = null;
					//让屏幕回复不透明状态
//					backgroundAlpha((Activity) context, 1f);
					onCallBackNumberType.dimssPop(etNumber,type);

				}
			});


			//显示popwindow时让屏幕半透明，到达遮罩层的效果
//			backgroundAlpha((Activity) context, 0.5f);

		}
	}



	/**
	 * 消掉popupWindow
	 */
	public static void dimssWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private static void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public interface OnClickNumberType{
    	void onNumberType(String context,int pos);
	}

	public interface OnCallBackNumberType{
    	void dimssPop(EditText editText,int type);
    	void search(String number,int type,SearchPopWindowAdapter popWindowAdapter);
    	void onclickSearchHistory(SearchNumberBean.RowsBean rowsBean ,int type);
    	void onshow(EditText editText);
	}

}
