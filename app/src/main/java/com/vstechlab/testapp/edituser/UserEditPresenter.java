package com.vstechlab.testapp.edituser;

import android.support.annotation.NonNull;

import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersRepository;

public class UserEditPresenter implements UserEditContract.Presenter {

    private final UsersRepository mUsersRepository;

    private final UserEditContract.View mEditView;

    public UserEditPresenter(@NonNull UsersRepository mUsersRepository,
                             @NonNull UserEditContract.View mEditView) {
        this.mUsersRepository = mUsersRepository;
        this.mEditView = mEditView;

        mEditView.setPresenter(this);
    }

    @Override
    public void onEdit(@NonNull User user) {
        mUsersRepository.editUser(user);
        mEditView.showUsersList();
    }

    @Override
    public void start() {

    }
}
