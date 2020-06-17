package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

public class newsmoduledev extends AppCompatActivity implements NewsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsmoduledev);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}