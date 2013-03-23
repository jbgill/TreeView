package com.example.treeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;

public class TreeNode {
  
  private String name = "Testing";
  private String years = "";
  private boolean selected = false;
  private String gender = "M";
  
  public static final int WIDTH = 500;
  public static final int HEIGHT = 120;
  //public static final int WIDTH = 250;
  //public static final int HEIGHT = 60;
  
  private static final int CORNER_RADIUS = 16;
  private static final int TEXT_LEFT = 130;
  private static final int PIC_MARGIN = 10;
  private static final int LINE1_VERT_OFFSET = 50;
  private static final int LINE2_VERT_OFFSET = 110;
  private static final int TEXT_SIZE=36;
//  private static final int CORNER_RADIUS = 8;
//  private static final int TEXT_LEFT = 65;
//  private static final int PIC_MARGIN = 5;
//  private static final int LINE1_VERT_OFFSET = 25;
//  private static final int LINE2_VERT_OFFSET = 55;
//  private static final int TEXT_SIZE=18;
  
  

  
  private TreeNode father = null;
  private TreeNode mother = null;
  
  private static Paint rectPaint;
  private static Paint selectedRectPaint;
  private static Paint textPaint;
  private RectF rect;
  private Bitmap bmp;
  private Context context;
  
  static {
    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Style.FILL);
    rectPaint.setColor(Color.WHITE);
    
    selectedRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    selectedRectPaint.setStyle(Style.FILL);
    selectedRectPaint.setColor(Color.LTGRAY);
    
    
    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setStyle(Style.FILL);
    textPaint.setTextSize(TEXT_SIZE);    
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
  
  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  private void init() {
    rect = new RectF(0, 0, WIDTH, HEIGHT);
    
    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.kirk);
  }

  public void draw(Canvas canvas, float x, float y) {
    rect.offsetTo(x, y); // rect now defines the bounds of this node, and where it is on the canvas
    if (selected) {
      canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, selectedRectPaint);
    } else {
      canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, rectPaint);
    }
    canvas.drawText(name, TEXT_LEFT+x, LINE1_VERT_OFFSET+y, textPaint);
    canvas.drawText(years, TEXT_LEFT+x, LINE2_VERT_OFFSET+y, textPaint);
    
    canvas.drawBitmap(bmp, PIC_MARGIN+x, PIC_MARGIN+y, null);
    
  }
  
  /**
   * Is point p in the bounds of this TreeNode?
   * @param p
   * @return
   */
  public boolean isPointInBounds(PointF p) {
    return rect.contains(p.x, p.y);    
  }
  
}
