package com.lkl.ansuote.hdqlibrary.mode;


import androidx.annotation.NonNull;

import java.util.List;

public interface PermissionCallback {

    void onSucceed(int requestCode, @NonNull List<String> grantPermissions);

    void onFailed(int requestCode, @NonNull List<String> deniedPermissions);

}