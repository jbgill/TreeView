package com.example.treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

public class TreeView extends ZoomViewGroup {

  private Paint paint;
  
  private TreeNode rootNode = null;
  
  // margin between left size of view and root node
  private static final int leftMargin = 5;
  // horiz gap between nodes
  private static final int hGap = 30;
  
  public TreeView(Context context) {
    super(context);
    init();
  }

  public TreeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TreeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    // by default this is true in ViewGroups, set to false so onDraw gets called
    this.setWillNotDraw(false);
    
    // paint object for lines
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLUE);
    paint.setStrokeWidth(2f);
    paint.setStyle(Style.STROKE);

  }
  
  public void setRootNode(TreeNode tn) {
    rootNode = tn;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    // At this time we need to call setMeasuredDimensions(). Lets just call the
    // parent View's method (see
    // https://github.com/android/platform_frameworks_base/blob/master/core/java/android/view/View.java)
    // that does:
    // setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),
    // widthMeasureSpec),
    // getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    //

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    measureChildren(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // TODO:  the following is just a simple 2-generation version
    //        we need to make it handle more generations.
    if (rootNode != null) {
      //this.adjustTranslationForBounds();
      float scaleFactor = this.getCanvasCamera().getScale();
      
      int myHeight = this.getHeight();
      int vCenter = (int) (myHeight / 2);
      int vThird = (int) (myHeight / 3);
      int nodeWidth = (int) (rootNode.getMeasuredWidth() / scaleFactor);
      int nodeHeight = (int) (rootNode.getMeasuredHeight() / scaleFactor);
      
      int coordX = leftMargin;
      int coordY = vCenter - (int)(nodeHeight/2);
      coordX = scaleAndTranslateX(coordX);
      coordY = scaleAndTranslateY(coordY);
      rootNode.layout(coordX, coordY, rootNode.getMeasuredWidth() + coordX,
        rootNode.getMeasuredHeight() + coordY);
      TreeNode father = rootNode.getFather();
      if (father != null) {
        coordX = leftMargin + nodeWidth + hGap;
        coordY = vThird - (int)(nodeHeight/2);
        coordX = scaleAndTranslateX(coordX);
        coordY = scaleAndTranslateY(coordY);
        father.layout(coordX, coordY, father.getMeasuredWidth() + coordX,
            father.getMeasuredHeight() + coordY);
      }
      TreeNode mother = rootNode.getMother();
      if (mother != null) {
        coordX = leftMargin + nodeWidth + hGap;
        coordY = (2*vThird) - (int)(nodeHeight/2);
        coordX = scaleAndTranslateX(coordX);
        coordY = scaleAndTranslateY(coordY);
        mother.layout(coordX, coordY, mother.getMeasuredWidth() + coordX,
            mother.getMeasuredHeight() + coordY);
      }
      
    }
    
  }
  
  private int scaleAndTranslateX(int val) {
    CanvasCamera cc = this.getCanvasCamera();
    float scaleFactor = cc.getScale();
    float translateX = cc.getTranslation().x;
    int x = (int)(val * scaleFactor);
    return (int) (x + translateX);
  }
  
  private int scaleAndTranslateY(int val) {
    CanvasCamera cc = this.getCanvasCamera();
    float scaleFactor = cc.getScale();
    float translateY = cc.getTranslation().y;
    int y = (int)(val * scaleFactor);
    return (int) (y + translateY);
  }

  @Override
  public void onDraw(Canvas canvas) {
    prepareCanvasZoom(canvas);

    /* put the canvas drawing code here: */

    doCanvasPaint(canvas);

    /* end of drawing code */

    super.onDraw(canvas);
  }
  
  public void doCanvasPaint(Canvas canvas) {
    int myHeight = this.getHeight();
    int vCenter = (int) (myHeight / 2);
    int vThird = (int) (myHeight / 3);
    int nodeWidth = rootNode.getOriginalWidth();
    
    float startX = leftMargin + nodeWidth;
    float startY = vCenter;
    float endX = startX + (hGap/2);
    float endY = startY;
    canvas.drawLine(startX, startY, endX, endY, paint);
    Path path = new Path();
    startX = leftMargin + nodeWidth + hGap;
    startY = vThird;
    path.moveTo(startX,  startY);
    endX = leftMargin + nodeWidth + hGap/2;
    endY = vThird;
    path.lineTo(endX,  endY);
    endY = vThird*2;
    path.lineTo(endX,  endY);
    endX = leftMargin + nodeWidth + hGap;
    path.lineTo(endX, endY);
    canvas.drawPath(path, paint);
    
  }
  

}
