package com.example.treeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class TreeNode {
  
  private String name = "Testing";
  private String years = "";
  
  public static final int WIDTH = 250;
  public static final int HEIGHT = 60;
  
  private TreeNode father = null;
  private TreeNode mother = null;
  
  private static Paint rectPaint;
  private static Paint textPaint;
  private RectF rect;
  private Bitmap bmp;
  private Context context;
  
  static {
    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Style.FILL);
    rectPaint.setColor(Color.YELLOW);
    
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setStyle(Style.FILL);
    textPaint.setTextSize(18);    
  }

  public TreeNode(Context context) {
    this.context = context;
    init();
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
    rect = new RectF(0, 0, WIDTH, HEIGHT);
    
    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.kirk);
  }

  public void draw(Canvas canvas, float x, float y) {
    rect.offsetTo(x, y); // rect now defines the bounds of this node, and where it is on the canvas
    canvas.drawRoundRect(rect, 8, 8, rectPaint);
    canvas.drawText(name, 65+x, 25+y, textPaint);
    canvas.drawText(years, 65+x, 55+y, textPaint);
    
    canvas.drawBitmap(bmp, 5+x, 5+y, null);
    
  }
  
}
