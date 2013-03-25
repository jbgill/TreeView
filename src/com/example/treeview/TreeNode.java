package com.example.treeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class TreeNode {
  public enum Gender {
    M, // Male
    F, // Female
    U; // Unknown
  }
  
  private String name = "Testing";
  private String years = "";
  private boolean selected = false;
  private Gender gender = Gender.U;
  private String pid = "";
  private Bitmap photo = null;
  
  public static final int WIDTH = 500;
  public static final int HEIGHT = 120;
  
  private static final int CORNER_RADIUS = 16;
  private static final int TEXT_LEFT = 130;
  private static final int PIC_MARGIN = 10;
  private static final int PIC_SIZE = 100;
  private static final int LINE1_VERT_OFFSET = 37;
  private static final int LINE2_VERT_OFFSET = 75;
  private static final int LINE3_VERT_OFFSET = 110;
  private static final int NAME_TEXT_SIZE = 36;
  private static final int OTHER_TEXT_SIZE = 24;
  
  
  private TreeNode father = null;
  private TreeNode mother = null;
  
  private static Paint rectPaint;
  private static Paint selectedRectPaint;
  private static Paint nameTextPaint;
  private static Paint otherTextPaint;
  private static Bitmap circleOverlay;
  private static Bitmap circleOverlaySelected;
  private static Bitmap femaleImg;
  private static Bitmap maleImg;
  private static Bitmap unknownImg;
  
  // rect is the bounding rectangle for this tree node "widget"
  private RectF rect;
  
  private Context context;
  
  static {
    rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rectPaint.setStyle(Style.FILL);
    rectPaint.setColor(Color.WHITE);
    
    selectedRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    selectedRectPaint.setStyle(Style.FILL);
    selectedRectPaint.setColor(Color.LTGRAY);
    
    
    nameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    nameTextPaint.setColor(Color.BLACK);
    nameTextPaint.setStyle(Style.FILL);
    nameTextPaint.setTextSize(NAME_TEXT_SIZE);
    
    otherTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    otherTextPaint.setColor(Color.DKGRAY);
    otherTextPaint.setStyle(Style.FILL);
    otherTextPaint.setTextSize(OTHER_TEXT_SIZE);
    
    makeCircleOverlays();
  }
  
  private static void makeCircleOverlays() {
    // Fill the canvas with the background color then paint a transparent
    // circle in the middle
    Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    Rect rect = new Rect(0, 0, PIC_SIZE, PIC_SIZE);
    
    Bitmap bitmap = Bitmap.createBitmap(PIC_SIZE,
             PIC_SIZE, Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    canvas.drawRect(rect, rectPaint);
    canvas.drawCircle(PIC_SIZE/2, PIC_SIZE/2, PIC_SIZE/2, clearPaint);
    circleOverlay = bitmap;
    
    bitmap = Bitmap.createBitmap(PIC_SIZE,
        PIC_SIZE, Config.ARGB_8888);
    canvas = new Canvas(bitmap);
    canvas.drawRect(rect, selectedRectPaint);
    canvas.drawCircle(PIC_SIZE/2, PIC_SIZE/2, PIC_SIZE/2, clearPaint);
    circleOverlaySelected = bitmap;
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
  
  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public Bitmap getPhoto() {
    return photo;
  }

  public void setPhoto(Bitmap photo) {
    this.photo = photo;
    // images need to be PIC_SIZE x PIC_SIZE
    if (photo != null && (photo.getWidth() != PIC_SIZE || photo.getHeight() != PIC_SIZE)) {
      this.photo = Bitmap.createScaledBitmap(photo, PIC_SIZE, PIC_SIZE, true);
    }
  }

  private void init() {
    rect = new RectF(0, 0, WIDTH, HEIGHT);
    
    femaleImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.female_sill);
    femaleImg = Bitmap.createScaledBitmap(femaleImg, PIC_SIZE, PIC_SIZE, true);
    maleImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.male_sill);
    maleImg = Bitmap.createScaledBitmap(maleImg, PIC_SIZE, PIC_SIZE, true);
    unknownImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown_sill);
    unknownImg = Bitmap.createScaledBitmap(unknownImg, PIC_SIZE, PIC_SIZE, true);
    
  }

  public void draw(Canvas canvas, float x, float y) {
    rect.offsetTo(x, y); // rect now defines the bounds of this node, and where it is on the canvas
    if (selected) {
      canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, selectedRectPaint);
    } else {
      canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, rectPaint);
    }
    canvas.drawText(name, TEXT_LEFT+x, LINE1_VERT_OFFSET+y, nameTextPaint);
    canvas.drawText(years, TEXT_LEFT+x, LINE2_VERT_OFFSET+y, otherTextPaint);
    canvas.drawText(pid, TEXT_LEFT+x, LINE3_VERT_OFFSET+y, otherTextPaint);
    Bitmap bmp=photo;
    if (photo == null) {
      if (gender.equals(Gender.M)) {
        bmp = maleImg;
      } else if (gender.equals(Gender.F)) {
        bmp = femaleImg;
      } else {
        bmp = unknownImg;
      }
    }
    canvas.drawBitmap(bmp, PIC_MARGIN+x, PIC_MARGIN+y, null);
    if (selected) {
      canvas.drawBitmap(circleOverlaySelected, PIC_MARGIN+x, PIC_MARGIN+y, null);
    } else {
      canvas.drawBitmap(circleOverlay, PIC_MARGIN+x, PIC_MARGIN+y, null);
    }
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
