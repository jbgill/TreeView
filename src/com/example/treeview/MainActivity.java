package com.example.treeview;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    TreeView tv = (TreeView) findViewById(R.id.TreeView1);
    
    TreeNode tn1 = new TreeNode(this);
    tn1.setName("Joseph T Schmoe");
    tn1.setYears("2100 - 2190");
    tn1.setGender("M");
    
    TreeNode tn2 = new TreeNode(this);
    tn2.setName("Papa John Schmoe");
    tn2.setYears("2080 - 2145");
    tn2.setGender("M");
    
    TreeNode tn3 = new TreeNode(this);
    tn3.setName("Mama Maidenschmoe");
    tn3.setYears("2080 - 2150");
    tn3.setGender("F");
    
    TreeNode tn4 = new TreeNode(this);
    tn4.setName("Grandpa Joe Schmoe");
    tn4.setYears("2080 - 2145");
    tn4.setGender("M");
    
    TreeNode tn5 = new TreeNode(this);
    tn5.setName("Granny S Papasmom");
    tn5.setYears("2080 - 2150");
    tn5.setGender("F");
    
    TreeNode tn6 = new TreeNode(this);
    tn6.setName("Grandpa MaidenSchmoe");
    tn6.setYears("2080 - 2145");
    tn6.setGender("M");
    
    TreeNode tn7 = new TreeNode(this);
    tn7.setName("Granny Q Mamasmom");
    tn7.setYears("2080 - 2150");
    tn7.setGender("F");

    tn1.setFather(tn2);
    tn1.setMother(tn3);
    tn2.setFather(tn4);
    tn2.setMother(tn5);
    tn3.setFather(tn6);
    tn3.setMother(tn7);
    
    tv.addView(tn1);
    tv.addView(tn2);
    tv.addView(tn3);
    tv.addView(tn4);
    tv.addView(tn5);
    tv.addView(tn6);
    tv.addView(tn7);
    tv.setRootNode(tn1);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
