package com.example.treeview;


import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    TreeView tv = (TreeView) findViewById(R.id.TreeView1);
    
    TreeNode tn1 = new TreeNode(this);
    tn1.setName("James Tiberius Kirk");
    tn1.setYears("2100 - 2190");
    tn1.setGender(TreeNode.Gender.M);
    tn1.setPid("AAAA-111");
    Bitmap img = BitmapFactory.decodeResource(this.getResources(), R.drawable.jtkirk);
    tn1.setPhoto(img);
    
    TreeNode tn2 = new TreeNode(this);
    tn2.setName("Papa Kirk");
    tn2.setYears("2080 - 2145");
    tn2.setGender(TreeNode.Gender.M);
    tn2.setPid("BBBB-222");
    
    TreeNode tn3 = new TreeNode(this);
    tn3.setName("Mama Klingon");
    tn3.setYears("2080 - 2150");
    tn3.setGender(TreeNode.Gender.F);
    tn3.setPid("CCCC-333");
    
    TreeNode tn4 = new TreeNode(this);
    tn4.setName("Grand Papa Kirk");
    tn4.setYears("2080 - 2145");
    tn4.setGender(TreeNode.Gender.M);
    tn4.setPid("DDDD-444");
    
    TreeNode tn5 = new TreeNode(this);
    tn5.setName("Grand Mama Spock");
    tn5.setYears("2080 - 2145");
    tn5.setGender(TreeNode.Gender.F);
    tn5.setPid("EEEE-555");
    
    TreeNode tn6 = new TreeNode(this);
    tn6.setName("Grand Papa Klingon");
    tn6.setYears("2080 - 2145");
    tn6.setGender(TreeNode.Gender.M);
    tn6.setPid("FFFF-666");
    
    TreeNode tn7 = new TreeNode(this);
    tn7.setName("Grand Mama Vulcan");
    tn7.setYears("2080 - 2145");
    tn7.setGender(TreeNode.Gender.F);
    tn7.setPid("GGGG-777");
    
    tn1.setFather(tn2);
    tn1.setMother(tn3);
    tn2.setFather(tn4);
    tn2.setMother(tn5);
    tn3.setFather(tn6);
    tn3.setMother(tn7);
    
    tv.setRootNode(tn1);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
