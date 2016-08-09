package com.vstechlab.testapp.edituser;

import android.support.annotation.NonNull;

import com.vstechlab.testapp.BasePresenter;
import com.vstechlab.testapp.BaseView;
import com.vstechlab.testapp.data.User;

public interface UserEditContract {

    interface View extends BaseView<Presenter> {

        void showUsersList();

    }

    interface Presenter extends BasePresenter {

        void onEdit(@NonNull User user);
    }
}
