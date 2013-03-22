package com.example.treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TreeView extends ZoomView {

  private Paint paint;
  
  private TreeNode rootNode = null;
  private TreeNode selectedNode = null;
  
  // margin between left size of view and root node
  private static final int LEFT_MARGIN = 5;
  
  // horizontal gap between nodes
  private static final int H_GAP = 30;
  
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
    
    // paint object for lines
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLUE);
    paint.setStrokeWidth(2f);
    paint.setStyle(Style.STROKE);

  }
  
  public TreeNode getSelectedNode() {
    return selectedNode;
  }

  public void setSelectedNode(TreeNode selectedNode) {
    this.selectedNode = selectedNode;
  }

  public TreeNode getRootNode() {
    return rootNode;
  }
  
  public void setRootNode(TreeNode tn) {
    rootNode = tn;
  }

  @Override
  public void onClickEvent(MotionEvent event) {
    PointF transformedPoint = getAsAbsoluteCoordinate(event.getX(), event.getY());
    TreeNode clickedNode = this.findNodeByContainingPoint(rootNode, transformedPoint);
    if (clickedNode != null) {
      if (selectedNode != null) {
        selectedNode.setSelected(false);
      }
      selectedNode = clickedNode;
      clickedNode.setSelected(true);
      invalidate();
    } 
  }
  
  private TreeNode findNodeByContainingPoint(TreeNode node, PointF p) {
    if (node.isPointInBounds(p)) {
      return node;
    } else {
      TreeNode father = node.getFather();
      TreeNode mother = node.getMother();
      if (father != null) {
        TreeNode n = findNodeByContainingPoint(father, p);
        if (n != null) {
          return n;
        }
      }
      if (mother != null) {
        TreeNode n = findNodeByContainingPoint(mother, p);
        if (n != null) {
          return n;
        }        
      }
    }
    return null;
  }

  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    CanvasCamera cc = getCanvasCamera();
    float scaleFactor = cc.getScale();
    float translateX = cc.getTranslation().x;
    float translateY = cc.getTranslation().y;

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);
    canvas.translate(translateX , translateY);

    /* put the canvas drawing code here: */

    doCanvasPaint(canvas);

    /* end of drawing code */

    canvas.restore();
  }
  
  public void doCanvasPaint(Canvas canvas) {
    int myHeight = this.getHeight();
    int vCenter = (int) (myHeight / 2);
    int vThird = (int) (myHeight / 3);
    int nodeWidth = TreeNode.WIDTH;
    int nodeHeight = TreeNode.HEIGHT;
    
    float startX = LEFT_MARGIN + nodeWidth;
    float startY = vCenter;
    float endX = startX + (H_GAP/2);
    float endY = startY;
    canvas.drawLine(startX, startY, endX, endY, paint);
    Path path = new Path();
    startX = LEFT_MARGIN + nodeWidth + H_GAP;
    startY = vThird;
    path.moveTo(startX,  startY);
    endX = LEFT_MARGIN + nodeWidth + H_GAP/2;
    endY = vThird;
    path.lineTo(endX,  endY);
    endY = vThird*2;
    path.lineTo(endX,  endY);
    endX = LEFT_MARGIN + nodeWidth + H_GAP;
    path.lineTo(endX, endY);
    canvas.drawPath(path, paint);
    
    rootNode.draw(canvas, LEFT_MARGIN, vCenter - (nodeHeight/2));
    TreeNode father = rootNode.getFather();
    TreeNode mother = rootNode.getMother();
    father.draw(canvas, LEFT_MARGIN + nodeWidth + H_GAP, vThird - (nodeHeight/2));
    mother.draw(canvas, LEFT_MARGIN + nodeWidth + H_GAP, (vThird*2) - (nodeHeight/2));
    
  }

}
