package app.androidhive.info.realm;

import android.accounts.AccountsException;
import android.app.Activity;

import app.androidhive.info.realm.core.BootstrapService;

import java.io.IOException;

public interface BootstrapServiceProvider {
    BootstrapService getService(Activity activity) throws IOException, AccountsException;
}
