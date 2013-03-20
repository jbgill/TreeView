package com.example.treeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TreeNode extends View {
  
  private float scaleFactor = 1f;
  private String name = "Testing";
  private String years = "";
  
  private static final int width = 250;
  private static final int height = 60;
  
  private TreeNode father = null;
  private TreeNode mother = null;
  
  private Paint rectPaint;
  private Paint textPaint;
  private RectF rect;
  private Bitmap bmp;

  public TreeNode(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public TreeNode(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TreeNode(Context context) {
    super(context);
    init();
  }
  
  public float getScaleFactor() {
    return scaleFactor;
  }

  public void setScaleFactor(float scaleFactor) {
    this.scaleFactor = scaleFactor;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getYears() {
    return years;
  }

  public void setYears(String years) {
    this.years = years;
  }

  public TreeNode getFather() {
    return father;
  }

  public void setFather(TreeNode father) {
    this.father = father;
  }

  public TreeNode getMother() {
    return mother;
  }

  public void setMother(TreeNode mother) {
    this.mother = mother;
  }
  

  private void init() {
    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Style.FILL);
    rectPaint.setColor(Color.YELLOW);
    
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setStyle(Style.FILL);
    textPaint.setTextSize(18);
    
    rect = new RectF(0,0,width,height);
    
    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.kirk);
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);

    // draw it:
    canvas.drawRoundRect(rect, 8, 8, rectPaint);
    canvas.drawText(name, 65, 25, textPaint);
    canvas.drawText(years, 65, 55, textPaint);
    
    canvas.drawBitmap(bmp, 5, 5, null);
    
    canvas.restore();
  }
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int wspec = MeasureSpec.makeMeasureSpec((int) (width * scaleFactor), MeasureSpec.UNSPECIFIED);
    int hspec = MeasureSpec.makeMeasureSpec((int) (height * scaleFactor), MeasureSpec.UNSPECIFIED);

    this.setMeasuredDimension(wspec, hspec);
  }

}
