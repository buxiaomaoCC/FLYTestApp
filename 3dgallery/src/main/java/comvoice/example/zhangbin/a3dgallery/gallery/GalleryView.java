package comvoice.example.zhangbin.a3dgallery.gallery;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @描述 TODO
 * @项目名称 App_imooc
 * @包名 com.android.imooc.async
 * @类名 GalleryView
 * @author chenlin
 * @date 2012年6月5日 下午9:14:48
 * @version 1.0
 */

@SuppressWarnings("all")
public class GalleryView extends Gallery {

	private static final String TAG = "gallery";
	private int mGalleryCenterX = 0;
	private int mMaxAngle = 50;// 最大旋转角度
	private Camera mCamera;

	public GalleryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GalleryView(Context context) {
		this(context, null);
	}

	public GalleryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStaticTransformationsEnabled(true);
		mCamera = new Camera();
	}

	/**
	 * 实现子view的变化效果 Transformation指定当前item的效果
	 */
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		int rotateAngle = 0;
		// 如果child的中心点与gallery的中心点不一致，需要计算旋转角度
		int childCenterX = getChildCenterX(child);
		if (childCenterX != mGalleryCenterX) {
			// 两个中心点距离
			int distance = mGalleryCenterX - childCenterX;
			float percent = distance * 1.0f / child.getWidth();
			rotateAngle = (int) (percent * mMaxAngle);// 得到旋转的角度

			// 因为distance有可能大于图片的宽度，所以得到角度有可能大于最大的角度
			if (Math.abs(rotateAngle) > mMaxAngle) {
				rotateAngle = rotateAngle > 0 ? mMaxAngle : -mMaxAngle;
			}
		}
		//设置变化之前，要把上面的一个动画清除
		t.clear();
		//设置变化的效果为矩阵类型
		t.setTransformationType(Transformation.TYPE_MATRIX);
		//开始旋转
		startAnimate(child, rotateAngle, t);

		return true;
	}

	/**
	 * 开始动画效果
	 * @param child
	 * @param rotateAngle
	 * @param t
	 */
	private void startAnimate(View child, int rotateAngle, Transformation t) {
		//if (child instanceof ImageView) {
			ImageView iv = (ImageView) child;
			int absAngle = Math.abs(rotateAngle);
			
			mCamera.save();
			//3.实现放大效果
			
			//仔细看图片，发现图片在x,y轴上都没有变化，但有一边缩小，另一边放大，是如何实现的？
			//这里就要用到了z轴了，只要改变轴的数值就能实现了
			mCamera.translate(0, 0, 100);
			int zoom = -250 + (absAngle * 2);
			mCamera.translate(0, 0, zoom);
			
			//2.设置透明度 (0到255) 255完全显示,中间的absAngle=0,所以没有透明度
			iv.setAlpha((int) (255 - absAngle * 2.5));
			
			//3.旋转
			mCamera.rotateY(rotateAngle);
			
			//4.转换成矩阵
			Matrix matrix = t.getMatrix();
			//给matrix赋值
			mCamera.getMatrix(matrix);
			
			//矩阵前乘
			matrix.preTranslate(-iv.getWidth()/2, -iv.getHeight()/2);
			//矩阵后乘
			matrix.postTranslate(iv.getWidth()/2, iv.getHeight()/2);
			
			mCamera.restore();
		//}
		
	}

	/**
	 * gallery中心点
	 * 
	 * @return
	 */
	public int getGalleryCenterX() {
		return this.getWidth() / 2;
	}

	/**
	 * child中心点
	 * 
	 * @return
	 */
	public int getChildCenterX(View child) {
		return child.getLeft() + child.getWidth() / 2;
	}

	/**
	 * 设置最大旋转角度
	 * 
	 * @param maxAngel
	 */
	public void setAngle(int maxAngel) {
		this.mMaxAngle = maxAngel;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mGalleryCenterX = getGalleryCenterX();
		Logger.i(TAG, "mGalleryCenterX = " + mGalleryCenterX);
		Logger.i(TAG, "w/2 = " + w / 2);
		super.onSizeChanged(w, h, oldw, oldh);
	}
}
