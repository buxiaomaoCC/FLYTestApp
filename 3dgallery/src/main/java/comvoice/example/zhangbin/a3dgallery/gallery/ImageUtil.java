package comvoice.example.zhangbin.a3dgallery.gallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;

import comvoice.example.zhangbin.a3dgallery.R;

/**
 * @描述         图片处理工具
 * @项目名称      App_imooc
 * @包名         com.android.imooc.gallery
 * @类名         ImageUtil
 * @author      chenlin
 * @date        2012年9月5日 下午10:05:38
 * @version     1.0
 */

public class ImageUtil {

	/**
	 * 根据图片id获得有倒影的图片
	 * @param resId
	 * @return
	 */
	public static Bitmap getReverseBitmapById(Context context, int resId) {
		int padding  = context.getResources().getDimensionPixelOffset(R.dimen.image_paddding);//图片的间距
		//绘制原图
		Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
		
		//图片的默认矩阵
//		float[] values = {
//				1.0f, 0f, 0f,
//				0f, 1.0f, 0f,
//				0f, 0f, 1.0f
//		};
		
		
		//绘制原图的下一半图片
		Matrix matrix = new Matrix();
		matrix.setScale(1, -1);
		//matrix.setValues(values);
		Bitmap inverseBitmap = Bitmap.createBitmap(sourceBitmap, 0, sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(), sourceBitmap.getHeight()/2, matrix, false);
		
        
        //合成图片
        Bitmap groupbBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight() + sourceBitmap.getHeight() / 2 + padding,
        		sourceBitmap.getConfig());
        
        
        Canvas gCanvas = new Canvas(groupbBitmap);
        //把原图画在合成图片的上面
        gCanvas.drawBitmap(sourceBitmap, 0, 0, null);
        //以图片的左上角与坐标
        gCanvas.drawBitmap(inverseBitmap, 0, sourceBitmap.getHeight() + padding, null);
        
        //添加遮罩
        Paint paint = new Paint();
        TileMode tile = TileMode.CLAMP;
		LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight() + padding, 0, groupbBitmap.getHeight(), 0x70ffffff,  Color.TRANSPARENT, tile);
        paint.setShader(shader);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        
		gCanvas.drawRect(0, sourceBitmap.getHeight() + padding, sourceBitmap.getWidth(), groupbBitmap.getHeight(), paint);
		
		return groupbBitmap;
	}

}


