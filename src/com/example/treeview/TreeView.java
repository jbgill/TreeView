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

  private Paint bracketLinePaint;
  private Path bracketLinePath = new Path();
  
  private TreeNode rootNode = null;
  private TreeNode selectedNode = null;
  
  // margin between sides of view and nodes
  private static final int MARGIN = 5;
  
  // horizontal gap between nodes
  private static final int H_GAP = 30;
  
  // minimum vertical gap between nodes
  private static final int MIN_V_GAP = 30;
  
  // how much room will the tree take unscaled?
  private int treeWidth=0;
  private int treeHeight=0;
  
  // used in calculating initial scale and translate, and detecting size changes:
  private int calcWidth=0;
  private int calcHeight=0;
  
  
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
    bracketLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bracketLinePaint.setColor(Color.BLUE);
    bracketLinePaint.setStrokeWidth(2f);
    bracketLinePaint.setStyle(Style.STROKE);
    
    // how much unscaled room the tree takes:
    treeWidth = TreeNode.WIDTH*3 + H_GAP*2 + MARGIN*2;
    treeHeight = TreeNode.HEIGHT*4 + MIN_V_GAP*3 + MARGIN*2;
  }
  
  private void calculateInitialScaleAndTranslate(Canvas canvas) {
    CanvasCamera cc = getCanvasCamera();
    int oldCalcWidth = calcWidth;
    int oldCalcHeight = calcHeight;
    calcWidth = this.getWidth();
    calcHeight = this.getHeight();
    // if our dimensions changed, we need to recenter the tree in the view:
    if (oldCalcHeight!=calcHeight || oldCalcWidth!=calcWidth) {
      float startScale = ((float)calcWidth) / ((float)treeWidth);
      cc.setScale(startScale);
      float heightDiff = calcHeight - (treeHeight*startScale);
      cc.setTranslation(0, heightDiff/2/startScale);
    }
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
    TreeNode clickedNode = this.findNodeByContainedPoint(rootNode, transformedPoint);
    if (clickedNode != null) {
      if (selectedNode != null) {
        selectedNode.setSelected(false);
      }
      selectedNode = clickedNode;
      clickedNode.setSelected(true);
      invalidate();
    } 
  }
  
  private TreeNode findNodeByContainedPoint(TreeNode node, PointF p) {
    if (node.isPointInBounds(p)) {
      return node;
    } else {
      TreeNode father = node.getFather();
      TreeNode mother = node.getMother();
      if (father != null) {
        TreeNode n = findNodeByContainedPoint(father, p);
        if (n != null) {
          return n;
        }
      }
      if (mother != null) {
        TreeNode n = findNodeByContainedPoint(mother, p);
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
    // this call needs to be made every time because the 
    //height fluctuates as the view settles
    this.calculateInitialScaleAndTranslate(canvas);
    CanvasCamera cc = getCanvasCamera();
    float scaleFactor = cc.getScale();
    float translateX = cc.getTranslation().x;
    float translateY = cc.getTranslation().y;

    canvas.save();

    canvas.scale(scaleFactor, scaleFactor);
    canvas.translate(translateX , translateY);

    /* put the canvas drawing code here: */

    drawTree(canvas);

    /* end of drawing code */

    canvas.restore();
    
  }
  
  private void drawTree(Canvas canvas) {
    //int myHeight = this.getHeight();
    int vCenter = (int) (treeHeight / 2);
    int nodeWidth = TreeNode.WIDTH;
    int nodeHeight = TreeNode.HEIGHT;
    
    rootNode.draw(canvas, MARGIN, vCenter - nodeHeight/2);
    TreeNode father = rootNode.getFather();
    TreeNode mother = rootNode.getMother();
    if (father != null || mother != null) {
      drawBracketLines(canvas, 
                       MARGIN + nodeWidth, 
                       MARGIN + nodeHeight + MIN_V_GAP/2, 
                       H_GAP, 
                       nodeHeight*2 + MIN_V_GAP*2);      
    }
    if (father != null) {
      father.draw(canvas, 
                  MARGIN + nodeWidth + H_GAP, 
                  MARGIN + nodeHeight + MIN_V_GAP/2 - nodeHeight/2);
      TreeNode fathersFather = father.getFather();
      TreeNode fathersMother = father.getMother();
      if (fathersFather != null || fathersMother != null) {
        drawBracketLines(canvas, 
            MARGIN + nodeWidth*2 + H_GAP, 
            MARGIN + nodeHeight/2, 
            H_GAP, 
            nodeHeight + MIN_V_GAP);
        if (fathersFather!=null) {
          fathersFather.draw(canvas, 
                             MARGIN + 2*H_GAP + 2*nodeWidth, 
                             MARGIN);
        }
        if (fathersMother != null) {
          fathersMother.draw(canvas, 
                             MARGIN + 2*H_GAP + 2*nodeWidth, 
                             MARGIN + MIN_V_GAP + nodeHeight);
        }
      }
    }
    if (mother != null) {
      mother.draw(canvas, 
                  MARGIN + nodeWidth + H_GAP, 
                  MARGIN + nodeHeight*3 + MIN_V_GAP*2.5f - nodeHeight/2);
      TreeNode mothersFather = mother.getFather();
      TreeNode mothersMother = mother.getMother();
      if (mothersFather != null || mothersMother != null) {
        drawBracketLines(canvas, 
            MARGIN + nodeWidth*2 + H_GAP, 
            MARGIN + nodeHeight/2 + nodeHeight*2 + 2*MIN_V_GAP, 
            H_GAP, 
            nodeHeight + MIN_V_GAP);
        if (mothersFather!=null) {
          mothersFather.draw(canvas, 
                             MARGIN + 2*H_GAP + 2*nodeWidth, 
                             MARGIN + 2*MIN_V_GAP + 2*nodeHeight);
        }
        if (mothersMother != null) {
          mothersMother.draw(canvas, 
                             MARGIN + 2*H_GAP + 2*nodeWidth, 
                             MARGIN + 3*MIN_V_GAP + 3*nodeHeight);
        }
      }
    }

  }
  
  /**
   * draw the lines that connect a node to its parent nodes
   * @param canvas - the canvas to draw on
   * @param x - the upper left corner x-coord of the box bounding the lines
   * @param y - the upper left corner y-coord of the box bounding the lines
   * @param width - the width of the box bounding the lines
   * @param height - the height of the box bounding the lines
   */
  private void drawBracketLines(Canvas canvas, float x, float y, float width, float height) {
    float middleY = y + (height/2);
    float middleX = x + (width/2);
    float rightX = x+width;
    float bottomY = y+height;
    canvas.drawLine(x, middleY, middleX, middleY, bracketLinePaint);
    bracketLinePath.reset();
    bracketLinePath.moveTo(rightX, y);
    bracketLinePath.lineTo(middleX, y);
    bracketLinePath.lineTo(middleX, bottomY);
    bracketLinePath.lineTo(rightX, bottomY);
    canvas.drawPath(bracketLinePath, bracketLinePaint);  
  }
  
  
  

}
