package com.org.sfv.rlm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.sfv.rlm.adapter.DemoRealmAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        DemoRealmAdapter.OnItemLongClickListener,
        RealmChangeListener<RealmResults<DemoRealmObject>> {

    /**
     * Declare a Realm object
     */
    private Realm mRealm;

    private List<DemoRealmObject> mListObjects;
    private DemoRealmAdapter mDemoRealmAdapter;
    private DemoRealmObject demoRealmObject;
    private RealmResults<DemoRealmObject> mRealmResults;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.edit_text_id) EditText mEdtId;
    @BindView(R.id.edit_text_title) EditText mEdtTitle;
    @BindView(R.id.edit_text_name) EditText mEdtName;
    @BindView(R.id.button_add) Button mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        // Create an Realm instance
        mRealm = Realm.getDefaultInstance();

        mListObjects = new ArrayList<>();
        mDemoRealmAdapter = new DemoRealmAdapter(this, mListObjects);
        mDemoRealmAdapter.setOnItemClickListener(this);
        mBtnAdd.setOnClickListener(this);

        // Initial RecyclerView
        setUpRecyclerView();

        // Action get data from Realm
        getDataFromRealm();
    }

    /**
     * Add new item into Realm
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        demoRealmObject = new DemoRealmObject();
        switch (v.getId()) {
            case R.id.button_add:
                if (TextUtils.isEmpty(mEdtTitle.getText().toString())) return;
                if (!TextUtils.isEmpty(getTitleRealm()) && (!TextUtils.isEmpty(getNameRealm()))) {

                    demoRealmObject.setId(getIdRealm());
                    demoRealmObject.setTitle(getTitleRealm().toString());
                    demoRealmObject.setName(getNameRealm().toString());
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(demoRealmObject);
                        }
                    });
                } else if (TextUtils.isEmpty(getTitle()) || TextUtils.isEmpty(getNameRealm())) {
                    Toast.makeText(this, "Title and Name is not empty!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRealmResults.removeChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    /**
     * Delete an object from Realm
     *
     * @param object
     */
    @Override
    public void onItemLongClick(DemoRealmObject object) {
        deleteItem(object);
    }

    /**
     * This action call when have any change into Realm
     *
     * @param results
     */
    @Override
    public void onChange(RealmResults<DemoRealmObject> results) {
        mListObjects.clear();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                mListObjects.add(results.get(i));
            }
        } else {
            Toast.makeText(this, "Data in realm is empty now", Toast.LENGTH_LONG).show();
        }
        mDemoRealmAdapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mDemoRealmAdapter);
    }

    private void getDataFromRealm() {
        mListObjects.clear();
        mRealm.beginTransaction();
        mRealmResults = mRealm.where(DemoRealmObject.class).findAll();
        mRealmResults.addChangeListener(this);
        if (mRealmResults.size() > 0) {
            for (int i = 0; i < mRealmResults.size(); i++) {
                mListObjects.add(mRealmResults.get(i));
            }
        } else {
            Toast.makeText(this, "Data in realm is empty now", Toast.LENGTH_LONG).show();
        }
        mRealm.commitTransaction();
        mDemoRealmAdapter.notifyDataSetChanged();
    }

    public void deleteItem(DemoRealmObject object) {
        final int id = object.getId();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DemoRealmObject> results = realm.where(DemoRealmObject.class).equalTo("id", id).findAll();
                results.deleteAllFromRealm();
            }
        });
    }

    public int getIdRealm() {
        return Integer.parseInt(mEdtId.getText().toString().trim());
    }

    public String getTitleRealm() {
        return mEdtTitle.getText().toString().trim();
    }

    public String getNameRealm() {
        return mEdtName.getText().toString().trim();
    }
}
