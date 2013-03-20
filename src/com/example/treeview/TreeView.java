package com.example.treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class TreeView extends ViewGroup {

  // the minimum and maximum zoom
  private float minZoom = .5f;
  private float maxZoom = 5f;

  // the max pannable canvas width and height
  private float maxCanvasWidth = 900;
  private float maxCanvasHeight = 900;

  private volatile float scaleFactor = 1f;
  private ScaleGestureDetector detector;

  // the mode that we're in
  private static final int NONE = 0;
  private static final int DRAG = 1;
  private static final int ZOOM = 2;

  private int mode;

  // the X and Y coordinate of the finger when it first
  // touches the screen
  private float startX = 0f;
  private float startY = 0f;

  // the amount we need to translate the
  // canvas along the X
  // and the Y coordinate
  private float translateX = 0f;
  private float translateY = 0f;

  // the amount we translated the X and Y
  // coordinates, the last time we
  // panned.
  private float previousTranslateX = 0f;
  private float previousTranslateY = 0f;

  private boolean dragged = false;

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
    
    detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    
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
      this.adjustTranslationForBounds();
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
    
    
//    int childCount = getChildCount();
//    for (int i = 0; i < childCount; i++) {
//      View v = getChildAt(i);
//      this.adjustTranslationForBounds();
//      int coord = (int) ((200 * (i + 1)) * scaleFactor);
//      int coordX = (int) ((coord + translateX) );
//      int coordY = (int) ((coord + translateY) );
//      v.layout(coordX, coordY, v.getMeasuredWidth() + coordX,
//          v.getMeasuredHeight() + coordY);
//    }

  }
  
  private int scaleAndTranslateX(int val) {
    int x = (int)(val * scaleFactor);
    return (int) (x + translateX);
  }
  
  private int scaleAndTranslateY(int val) {
    int y = (int)(val * scaleFactor);
    return (int) (y + translateY);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction() & MotionEvent.ACTION_MASK) {

    case MotionEvent.ACTION_DOWN:
      mode = DRAG;

      // We assign the current X and Y coordinate of the finger to startX and
      // startY minus the previously translated
      startX = event.getX() - previousTranslateX;
      startY = event.getY() - previousTranslateY;
      break;

    case MotionEvent.ACTION_MOVE:
      translateX = event.getX() - startX;
      translateY = event.getY() - startY;

      // We cannot use startX and startY directly because we have adjusted their
      // values using the previous translation values.
      double distance = Math.sqrt(Math.pow(event.getX()
          - (startX + previousTranslateX), 2)
          + Math.pow(event.getY() - (startY + previousTranslateY), 2));

      if (distance > 0) {
        dragged = true;
      }

      break;

    case MotionEvent.ACTION_POINTER_DOWN:
      mode = ZOOM;
      break;

    case MotionEvent.ACTION_UP:
      mode = NONE;
      dragged = false;

      // All fingers went up, so let's save the value of translateX and
      // translateY into previousTranslateX and
      // previousTranslate
      previousTranslateX = translateX;
      previousTranslateY = translateY;
      break;

    case MotionEvent.ACTION_POINTER_UP:
      mode = DRAG;

      // This is not strictly necessary; we save the value of translateX and
      // translateY into previousTranslateX
      // and previousTranslateY when the second finger goes up
      previousTranslateX = translateX;
      previousTranslateY = translateY;
      break;
    }

    detector.onTouchEvent(event);

    // Only redraw if zooming or dragging and zoomed-in
    // if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
    if ((mode == DRAG && dragged) || mode == ZOOM) {
      for (int i = 0; i < getChildCount(); i++) {
        View v = getChildAt(i);
        if (v instanceof TreeNode) {
          ((TreeNode)v).setScaleFactor(scaleFactor);
          // we need to requestLayout on the child so it gets re-measured AND redrawn.
          // Otherwise it will just be invalidated and redrawn in its old size
          v.requestLayout();
        }

      }
      this.requestLayout();
      invalidate();
    }

    return true;
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);
    
    this.adjustTranslationForBounds();

    // The translation amount also gets scaled according to how much
    // we've zoomed into the canvas.
    canvas.translate(translateX / scaleFactor, translateY / scaleFactor);

    /* put the canvas drawing code here: */

    doCanvasPaint(canvas);

    /* end of drawing code */

    canvas.restore();
  }
  
  public void doCanvasPaint(Canvas canvas) {
    int myHeight = this.getHeight();
    int vCenter = (int) (myHeight / 2);
    int vThird = (int) (myHeight / 3);
    int nodeWidth = (int) (rootNode.getMeasuredWidth() / scaleFactor);
    int nodeHeight = (int) (rootNode.getMeasuredHeight() / scaleFactor);
    
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
  
  private void adjustTranslationForBounds() {
    // these appear to be scaled values:
    int width = getWidth();
    int height = getHeight();

    // make sure we can't scroll the right edge of the "virtual canvas" off the
    // right side of view
    if ((translateX / scaleFactor) < (-maxCanvasWidth + (width / scaleFactor))) {
      translateX = (-maxCanvasWidth + (width / scaleFactor)) * scaleFactor;
    }

    // make sure we can't scroll the bottom edge of the virtual canvas off the
    // bottom of view
    if ((translateY / scaleFactor) < (-maxCanvasHeight + (height / scaleFactor))) {
      translateY = (-maxCanvasHeight + (height / scaleFactor)) * scaleFactor;
    }

    // don't scroll left edge of virtual canvas to right
    if (translateX > 0) {
      translateX = 0;
    }

    // don't scroll top edge of virtual canvas down
    if (translateY > 0) {
      translateY = 0;
    }
  }

  public float getTranslateX() {
    return translateX;
  }

  public void setTranslateX(float translateX) {
    this.translateX = translateX;
    previousTranslateX = translateX;
  }

  public float getTranslateY() {
    return translateY;
  }

  public void setTranslateY(float translateY) {
    this.translateY = translateY;
    previousTranslateY = translateY;
  }

  private class ScaleListener extends
      ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      scaleFactor *= detector.getScaleFactor();
      scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom));
      return true;
    }
  }

}
