package com.yunding.dut.ui.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarActivity;

public class RegisterActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitleInCenter(getString(R.string.register_title));
        setShowNavigation(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
