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
    tn1.setName("James Tiberius Kirk");
    tn1.setYears("2100 - 2190");
    
    TreeNode tn2 = new TreeNode(this);
    tn2.setName("Papa Kirk");
    tn2.setYears("2080 - 2145");
    
    TreeNode tn3 = new TreeNode(this);
    tn3.setName("Mama Kirk");
    tn3.setYears("2080 - 2150");

    tn1.setFather(tn2);
    tn1.setMother(tn3);
    
    tv.addView(tn1);
    tv.addView(tn2);
    tv.addView(tn3);
    tv.setRootNode(tn1);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
