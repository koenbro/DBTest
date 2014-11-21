package com.koenbro.android.dbtest;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


/**
 * This application is a testbed to (1) learn more about SQLite databases, with a focus on
 * FOREIGN KEY; (2) test refactoring in to abstract classes and/or interfaces; (3) document the
 * project using javadoc. Note the
 * <a href="http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html">javadoc
 * documentation</a>,  the <a
 * href="https://docs.oracle.com/javase/tutorial/java/annotations/basics.html">annotations
 * basics</a> from Oracle, and the relevant <a href="http://stackoverflow.com/questions/tagged/javadoc">Stack Overflow tag</a>.
 * @author laszlo
 * @version 1.0
 */
public class MainActivity extends Activity {
    private final String[] mProjects = {"journal article", "garage remodel", "taxes"};
    private final String[] mTasks = {"call contractor", "buy tools (yess!", "scour internet"};
    private ProjectDBAdapter mProjectDBAdapter;
    private ProjectAdapter projectAdapter;
    private TaskDBAdapter mTaskDBAdapter;
    private DBAdapter mDBAdapter;
    private ListView projectListView;
    private String emailedFilename;
    private String EmailId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBAdapter = new DBAdapter(this);
        try{
            mDBAdapter.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        mDBAdapter.close();
        mProjectDBAdapter = new ProjectDBAdapter(this);
        mTaskDBAdapter = new TaskDBAdapter(this);
        projectsAdapterLoad();
    }

    /**
     * reload the latest list to refresh screen
     */
    @Override
    public void onResume(){
        super.onResume();
        projectsAdapterLoad();
    }

    /**
     * inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * user choice on action menu
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_project:
                addProject();
                Toast.makeText(MainActivity.this, "Project added", Toast.LENGTH_SHORT).show();
                onResume();
                return true;
            case R.id.action_add_task:
                addTask();
                Toast.makeText(MainActivity.this,"Task added", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_send_data:
                emailedFilename = getResources().getString(R.string.emailed_file);
                exportDatabase(DBContract.DB_NAME, emailedFilename);
                EmailId = getResources().getString(R.string.email_id);
                //sendEmail(EmailId, emailedFilename);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * add a randomly-generated project. Name is from {@code mProjects}
     */
    private void addProject() {
        int i  = mProjects.length;
        if (i==0) {
            i=1;
        } else {
            i = (int) (Math.random() * i);
        }
        Project p = new Project();
        p.setName(mProjects[i]);
        p.setCompleted((int)(Math.random()*100));
        mProjectDBAdapter = new ProjectDBAdapter(this);
        mProjectDBAdapter.open();
        mProjectDBAdapter.addProject(p);
        mProjectDBAdapter.close();
    }

    /**
     * call {@code generateData} which returns an ArrayList of objects of class {@code Project} and
     * connect it to a {@code ListView}
     */
    private void projectsAdapterLoad() {
        projectAdapter = new ProjectAdapter(this, generateData());
        projectListView = (ListView)findViewById(R.id.projectListView);
        projectListView.setAdapter(projectAdapter);
    }

    /**
     * get a list of all projects
     * @return ArrayList of with objects of class {@code Project}
     */
    private ArrayList<Project> generateData() {
        ArrayList<Project> allProjects;
        mProjectDBAdapter.open();
        allProjects = mProjectDBAdapter.getAllProjects();
        mProjectDBAdapter.close();
        return allProjects;
    }
    private void addTask() {

    }

    /**
     * export the database to a card, where it can be accessed directly for debugging. It can
     * also be emailed
     * @param databaseName a constant from {@code DBContract.DB_NAME}
     * @param backupDBPath name of file on disk; from {@code getResources().getString(R.string.emailed_file)}
     */
    public void exportDatabase(String databaseName, String backupDBPath) {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {        }
    }


    /**
     * method to email database. This is used to debug a database an a non-rooted device
     * @param email address of recipient
     * @param fileToSend file from storage (card) to send
     */
    private void sendEmail(String email, String fileToSend) {
        File file = new File(Environment.getExternalStorageDirectory(), fileToSend);
        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("application/octet-stream");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailedFilename + "_" +
                timeStamp()[0] + "_" + timeStamp()[1]);
        String to[] = { email };
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_TEXT, "Here is the db.");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        startActivityForResult(Intent.createChooser(intent, "Send mail..."),
                1222);
    }

    /**
     * get a time stamp. This can have many uses, including for stamping an emailed db file
     * @return String[] with 2 elements: day and time
     */
    private String[] timeStamp(){
        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        String day =  now.year +"-"+(now.month+1)+"-"+now.monthDay;
        String time = now.format("%k:%M:%S");
        String[] timeStamp = new String[2];
        timeStamp[0] = day;
        timeStamp[1] = time;
        return (timeStamp);
    }



}
