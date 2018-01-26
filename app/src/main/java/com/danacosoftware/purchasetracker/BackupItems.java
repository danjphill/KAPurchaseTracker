package com.danacosoftware.purchasetracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.plus.Plus;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Daniel Phillips on 8/21/2016.
 */
public class BackupItems extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener{
    private static final String TAG = "Google Drive Activity";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final  int REQUEST_CODE_OPENER = 2;
    private static int BUFFER = 2000;
    private GoogleApiClient mGoogleApiClient;
    private boolean fileOperation = false;
    private DriveId mFileId;
    public DriveFile file;
    MaterialDialog SaveProgress;
    MaterialDialog OpenProgress;


    ArrayAdapter<String> adapter;
    ArrayList<String> BackupOptions =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_lists_layout);
        setTitle("Backup");
        ListView BackupListView = (ListView)findViewById(R.id.BackuplistView);
        BackupOptions.add("Backup to Google Drive");
        BackupOptions.add("Restore from Google Drive");
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                BackupOptions);
        BackupListView.setAdapter(adapter);

        BackupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    fileOperation = true;
                    // create new contents resource
                    Drive.DriveApi.newDriveContents(mGoogleApiClient)
                            .setResultCallback(driveContentsCallback);
                }else{
                    new AlertDialogWrapper.Builder(BackupItems.this)
                            .setTitle("Warning")
                            .setMessage("This Action Will Add Backed Up Data to Current Data, Continue?")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fileOperation = false;
                                    // create new contents resource
                                    Drive.DriveApi.newDriveContents(mGoogleApiClient)
                                            .setResultCallback(driveContentsCallback);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }
            }
        });


    }


    /**
     * Called when the activity will start interacting with the user.
     * At this point your activity is at the top of the activity stack,
     * with user input going to it.
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {


        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        Toast.makeText(getApplicationContext(), "Connection Failed" + result.toString(), Toast.LENGTH_LONG).show();

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * It invoked when Google API client connected
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        Toast.makeText(getApplicationContext(), "Connected to Google Drive", Toast.LENGTH_LONG).show();
    }

    /**
     * It invoked when connection suspend
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    public void onClickCreateFile(View view){
        fileOperation = true;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);

    }

    public void onClickOpenFile(View view){
        fileOperation = false;

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    /**
     *  Open list of folder and file of the Google Drive
     */
    public void OpenFileFromGoogleDrive(){

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "application/dspt", "text/html" })
                .build(mGoogleApiClient);
        try {
            startIntentSenderForResult(

                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (SendIntentException e) {

            Log.w(TAG, "Unable to send intent", e);
        }

    }


    /**
     * This is Result result handler of Drive contents.
     * this callback method call CreateFileOnGoogleDrive() method
     * and also call OpenFileFromGoogleDrive() method, send intent onActivityResult() method to handle result.
     */
    final ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {

                    if (result.getStatus().isSuccess()) {

                        if (fileOperation == true) {

                            CreateFileOnGoogleDrive(result);

                        } else {

                            OpenFileFromGoogleDrive();

                        }
                    }


                }
            };

    /**
     * Create a file in root folder using MetadataChangeSet object.
     * @param result
     */
    public void CreateFileOnGoogleDrive(DriveContentsResult result){
       //CreatePTFolder();

        final DriveContents driveContents = result.getDriveContents();
        SaveProgress = new MaterialDialog.Builder(BackupItems.this)
                .title("Restoring Files")
                .content("Please Wait")
                .progress(true, 0)
                .show();
        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {

                // write content to DriveContents
                //Backup Files
                DatabaseHandler db = new DatabaseHandler(BackupItems.this);
                List<ItemObject> AllItems = new ArrayList<ItemObject>(db.getAllItems());
                List<String> ItemPaths = new ArrayList<String>();
                File ItemsListFile = new File(getExternalCacheDir()+"/ItemsList.dspl");

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(ItemsListFile.toString()));

                for (int i = 0;i<AllItems.size();i++ ){
                    ItemObject CurrentItem = AllItems.get(i);
                    outputStreamWriter.write(CurrentItem.getBarcode()+ "," + CurrentItem.getType()+"," + CurrentItem.getColour()+"," + CurrentItem.getPattern()+"," + CurrentItem.getGender()+"," + CurrentItem.getBrand()+"," + CurrentItem.getSize()+"," + CurrentItem.getPrice()+"," + CurrentItem.getStore()+"," + CurrentItem.getDescription()+"," + CurrentItem.getPieces()+"," + CurrentItem.getAccessories()+"," + CurrentItem.getPicture());
                    outputStreamWriter.write(System.getProperty("line.separator"));
                    if(!(CurrentItem.getPicture().equals("none"))){
                        ItemPaths.add(CurrentItem.getPicture());
                    }

                }
                    outputStreamWriter.close();
                ItemPaths.add(ItemsListFile.toString());
                OutputStream outputStream = driveContents.getOutputStream();

                // first parameter is d files second parameter is zip
                // file name
                ZipManager zipManager = new ZipManager();
                    File itemLoacation = new File (getExternalCacheDir() + "/BackupTemp1.dspt");
                // calling the zip function
                    String[] itemstoCompress = ItemPaths.toArray(new String[0]);
                try{
                    zipManager.zip(itemstoCompress, itemLoacation.toString());
                    Log.d(TAG,getCacheDir() + "/test.dspt");


                InputStream inputStream = new FileInputStream(itemLoacation);
                try {
                if (inputStream != null) {
                    byte[] data = new byte[1024];
                    while (inputStream.read(data) != -1) {
                        //Reading data from local storage and writing to google drive
                        outputStream.write(data);
                    }
                    inputStream.close();
                }


                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                    DateTime dt = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("PurchaseTracker_Backup_"+ dt.toString())
                            .setMimeType("application/dspt")
                            .setStarred(true).build();

                    // create a file in root folder
                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .createFile(mGoogleApiClient, changeSet, driveContents)
                            .setResultCallback(fileCallback);
                    ItemsListFile.delete();
                    itemLoacation.delete();
                }catch(IOException e ){
                    e.printStackTrace();
                }
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }


            }
        }.start();
    }

    /**
     * Handle result of Created file
     */
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toast.makeText(getApplicationContext(), "Google Drive Backup: Successful",Toast.LENGTH_LONG).show();
                        SaveProgress.dismiss();

                    }else{
                        Toast.makeText(getApplicationContext(), "Google Drive Backup: Failed",Toast.LENGTH_LONG).show();
                        SaveProgress.dismiss();
                    }

                    return;

                }
            };

    /**
     *  Handle Response of selected file
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_OPENER:

                if (resultCode == RESULT_OK) {
                    OpenProgress = new MaterialDialog.Builder(BackupItems.this)
                            .title("Restoring Files")
                            .content("Please Wait")
                            .progress(false, 150, false)
                            .show();


                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.e("file id", mFileId.getResourceId() + "");

                    DriveFile file = mFileId.asDriveFile();
                    file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, new DriveFile.DownloadProgressListener() {
                        @Override
                        public void onProgress(long bytesDownloaded, long bytesExpected) {


                            int progress =  (int)(bytesDownloaded*100/bytesExpected);
                            Log.d("Progress",progress+"");
                            OpenProgress.setProgress(progress);
                        }
                    })
                            .setResultCallback(contentsOpenedCallback);


//                    String url = "https://drive.google.com/open?id="+ mFileId.getResourceId();
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);

                }

                break;

            default:
                //super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    ResultCallback<DriveContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // display an error saying file can't be opened
                        Log.e(TAG,"File Cannot Be Open");
                        Toast.makeText(getApplicationContext(), "Restore Failed", Toast.LENGTH_LONG).show();
                        OpenProgress.dismiss();
                        return;
                    }
                    // DriveContents object contains pointers
                    // to the actual byte stream

                    File file = new File(getExternalCacheDir(), "BackupTemp2.zip");
                    try {
                    DriveContents contents = result.getDriveContents();
                        if(!file.exists()) {
                            file.createNewFile();
                        }
                        InputStream reader = contents.getInputStream();
                        OutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                        int bufferSize = 1024;
                        int len = 0;
                        byte[] buffer = new byte[bufferSize];

                        while ((len = reader.read(buffer)) != -1) {
                            stream.write(buffer, 0, len);
                        }
                        if(stream != null)
                            stream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ZipManager zipManager = new ZipManager();
                    zipManager.unzip(file.toString(),BackupItems.this);
                    DatabaseHandler db = new DatabaseHandler(BackupItems.this);
                    File itemfileList = new File(getExternalCacheDir()+"/ItemsList.dspl");
                    try {
                        InputStream inputStream = new FileInputStream(itemfileList);

                        if ( inputStream != null ) {
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String receiveString = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            while ( (receiveString = bufferedReader.readLine()) != null ) {
                              String[] Lineparts =  receiveString.split(",");
                                db.addItem(new ItemObject(Lineparts[0],Lineparts[1],Lineparts[2],Lineparts[3],Lineparts[4],Lineparts[5],Lineparts[6],Lineparts[7],Lineparts[8],Lineparts[9],Lineparts[10],Lineparts[11],Lineparts[12]));
                            }

                            inputStream.close();

                        }
                    }

                    catch (FileNotFoundException e) {
                        Log.e("File Reader", "File not found: " + e.toString());
                        Toast.makeText(getApplicationContext(), "Restore Failed", Toast.LENGTH_LONG).show();
                        OpenProgress.dismiss();
                    } catch (IOException e) {
                        Log.e("File Reader", "Can not read file: " + e.toString());
                        Toast.makeText(getApplicationContext(), "Restore Failed", Toast.LENGTH_LONG).show();
                        OpenProgress.dismiss();
                    }
                    itemfileList.delete();
                    file.delete();
                    Toast.makeText(getApplicationContext(), "Restore Complete", Toast.LENGTH_LONG).show();
                    OpenProgress.dismiss();
                }
            };

    public void CreatePTFolder(){
    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
            .setTitle("New folder").build();
    Drive.DriveApi.getRootFolder(mGoogleApiClient).createFolder(
            mGoogleApiClient, changeSet).setResultCallback(folderCreatedCallback);

}
    ResultCallback<DriveFolder.DriveFolderResult> folderCreatedCallback = new
            ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(DriveFolder.DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG,"Error while trying to create the folder");
                        return;
                    }
                    Log.e(TAG,"Created a folder: " + result.getDriveFolder().getDriveId());
                }
            };


}


