package com.example.dv.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SelectAlbumActivity extends Activity {
    private RadioGroup rdoGrpSelect;
    private DatabaseHelper helper;
    private DatabaseDb db;
    private Cursor dbCur;
    private ButtonClickEvent btns;
    private SomeGlobalVar vars;
    private String[] path;
    private int[] albumNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_album);
        init();
        createRadioButtonOptions();
        btnAction();
    }
    private void init(){
        albumNum = new int[1];
        vars = new SomeGlobalVar();
        path = new String[1];
        rdoGrpSelect = (RadioGroup)findViewById(R.id.rdoGrpSelect);
        helper = DatabaseHelper.getsInstance(getApplicationContext());
        db = new DatabaseDb(helper.getWritableDatabase());
        dbCur = db.getAlbumInfo();
        btns = new ButtonClickEvent(getApplicationContext(),this);
    }
    private void btnAction(){
        btns.btnSelectBackClicked((Button)findViewById(R.id.btnSelectBack));

        btns.btnSelectSelectClicked((Button)findViewById(R.id.btnSelectSelect),rdoGrpSelect,getIntent().getIntExtra(vars.WHICH_LOAD_CALLED,-1), path,albumNum);
    }
    private void createRadioButtonOptions(){
        int count = dbCur.getCount();
        if(count == 0){
            Toast.makeText(getApplicationContext(), "Currently there is no album created. Please create a new album first.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        for(int i=0; i<dbCur.getCount(); i++){
            dbCur.moveToNext();
            RadioButton rb = new RadioButton(this);
            rb.setId(dbCur.getInt(0));
            rb.setText(dbCur.getString(1));
            rdoGrpSelect.addView(rb);

        }
        ((RadioButton)rdoGrpSelect.getChildAt(0)).setChecked(true);

    }
    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        db.setDb(helper.getWritableDatabase());
        dbCur = db.getAlbumInfo();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1001:
                    Intent intent = new Intent();
                    intent.putExtra(vars.PATH,path[0]);
                    intent.putExtra(vars.INTENT_ALBUM_NO,albumNum[0]);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
            }
        }
    }
}
