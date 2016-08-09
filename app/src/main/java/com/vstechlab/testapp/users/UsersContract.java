package com.vstechlab.testapp.users;

import android.support.annotation.NonNull;

import com.vstechlab.testapp.BasePresenter;
import com.vstechlab.testapp.BaseView;
import com.vstechlab.testapp.data.User;

import java.util.List;

public interface UsersContract {
    interface View extends BaseView<Presenter> {

        void showLoadingIndicator(boolean show);

        void showLoadingUsersError();

        void showUsers(@NonNull List<User> users);

        void showUserDetailsUI(int userId);

        void showNoUsers();

        void showNoInternet();
    }

    interface Presenter extends BasePresenter {

        void loadUsers(boolean forceUpdate);

        void openUserDetails(@NonNull User user);

        void onUserDelete(@NonNull User user);

    }
}
