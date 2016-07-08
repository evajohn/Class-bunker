package app.androidhive.info.realm;

import app.androidhive.info.realm.authenticator.BootstrapAuthenticatorActivity;
import app.androidhive.info.realm.core.TimerService;
import app.androidhive.info.realm.ui.BootstrapActivity;
import app.androidhive.info.realm.ui.BootstrapFragmentActivity;
import app.androidhive.info.realm.ui.BootstrapTimerActivity;
import app.androidhive.info.realm.ui.CheckInsListFragment;
import app.androidhive.info.realm.ui.MainActivity;
import app.androidhive.info.realm.ui.NavigationDrawerFragment;
import app.androidhive.info.realm.ui.NewsActivity;
import app.androidhive.info.realm.ui.NewsListFragment;
import app.androidhive.info.realm.ui.UserActivity;
import app.androidhive.info.realm.ui.UserListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AndroidModule.class,
                BootstrapModule.class
        }
)
public interface BootstrapComponent {

    void inject(BootstrapApplication target);

    void inject(BootstrapAuthenticatorActivity target);

    void inject(BootstrapTimerActivity target);

    void inject(MainActivity target);

    void inject(CheckInsListFragment target);

    void inject(NavigationDrawerFragment target);

    void inject(NewsActivity target);

    void inject(NewsListFragment target);

    void inject(UserActivity target);

    void inject(UserListFragment target);

    void inject(TimerService target);

    void inject(BootstrapFragmentActivity target);
    void inject(BootstrapActivity target);


}
