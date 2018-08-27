package comvoice.example.zhangbin.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangbin on 2018/6/11.
 */

public class MyCircleView extends View{
    private int src;
    public MyCircleView(Context context) {
        super(context);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public MyCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private void init(Context context,AttributeSet attrs){
        //第二个参数就是我们在styles.xml文件中的<declare-styleable>标签
        //即属性集合的标签，在R文件中名称为R.styleable+name
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.MyCircleView);
        //第一个参数为属性集合里面的属性，第二个参数为设置的默认值
        if(array!=null){
            src=array.getResourceId(R.styleable.MyCircleView_MyDefaultSize,R.drawable.one);
            //最后将TypeArray对象回收
            array.recycle();
        }
    }
    /**
     * 调用父view的onDraw函数，它可以帮我们实现一些基本的绘制功能
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap sfBitmap = null;//缩放后的bitmap
        //1、获取Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), src);
        //2、裁剪：对原始bitmap进行处理，看是否需要进行缩放处理，如果bitmap宽度或高度只要不等于圆的直径。
        if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getWidth()) {
            sfBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getWidth(), false);
        } else {
            sfBitmap = bitmap;
        }
        Bitmap output = Bitmap.createBitmap(sfBitmap.getWidth(), sfBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //3、实例化新的一张画布Canvas
        Canvas canvas_new = new Canvas(output);
        //4、对画布进行裁剪
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setFilterBitmap(true);//抗锯齿，对位图进行滤波处理
        paint.setDither(true);//设置防抖动，图像比较柔和
        canvas_new.drawCircle(sfBitmap.getWidth() / 2, sfBitmap.getWidth() / 2, sfBitmap.getWidth() / 2, paint);
        //5、核心部分，设置两张图片的相交模式，在这里就是上面绘制的Circle和下面绘制的Bitmap
        Rect rect = new Rect(0, 0, sfBitmap.getWidth(), sfBitmap.getWidth());
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取两层绘制交集,显示上层
        canvas_new.drawBitmap(sfBitmap, rect, rect, paint);
        //6、把输出bitmap放到输出画布上去
        canvas.drawBitmap(output, 0, 0, null);
    }
}
