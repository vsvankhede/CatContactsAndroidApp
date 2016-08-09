package com.vstechlab.testapp.edituser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vstechlab.testapp.R;
import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersRepository;
import com.vstechlab.testapp.data.source.local.UsersLocalDataSource;
import com.vstechlab.testapp.data.source.remote.UsersRemoteDataSource;
import com.vstechlab.testapp.users.UsersActivity;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity implements UserEditContract.View{

    public static final String EXTRA_USER = "extra_user";
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private User mUser;

    @BindView(R.id.edit_iv_profile) ImageView mIvProfile;
    @BindView(R.id.edit_et_name) EditText mEtUsername;
    @BindView(R.id.btn_edit) Button mBtnEdit;

    private UserEditContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mUser = getIntent().getParcelableExtra(EXTRA_USER);
        }

        setUpActionBar();

        UsersRepository repository = UsersRepository.getInstance(
                UsersRemoteDataSource.getInstance(),
                UsersLocalDataSource.getInstance(this)
        );

        mPresenter = new UserEditPresenter(repository, this);

        mEtUsername.setText(mUser.getUsername());
        Picasso.with(this)
                .load(mUser.getProfile())
                .into(mIvProfile);

    }

    private void setUpActionBar() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mUser.getUsername());
    }

    @OnClick(R.id.btn_edit)
    public void onClickEdit() {
        if (mEtUsername.getText().toString().isEmpty()) {
            mEtUsername.setError(getString(R.string.err_empty_username));
            return;
        }
        User user = new User(mUser.getId(), mEtUsername.getText().toString(), mUser.getProfile());
        mPresenter.onEdit(user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void setPresenter(UserEditContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUsersList() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
