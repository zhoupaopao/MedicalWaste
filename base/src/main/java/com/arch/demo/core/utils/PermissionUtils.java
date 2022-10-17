package com.arch.demo.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;

import com.arch.demo.core.BaseApplication;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

public class PermissionUtils {

    public abstract static class PermissionResult {
        public abstract void ok();
        public void refuse(){}
    }

    /**
     * 拍照
     */
    public static void takePhoto(PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermission(
                Manifest.permission.CAMERA,
                new CheckPermissionWithRationaleAdapter("如果你拒绝了权限，你将无法拍照，请点击授予权限",
                        new Runnable() {
                            @Override
                            public void run() {
                                //retry
                                takePhoto(permissionResult);
                            }
                        }) {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionOk(Permission permission) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }
                });
    }

    /**
     * 读取imei号等
     */
    public static void readPhoneState(PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermission(
                Manifest.permission.READ_PHONE_STATE,
                new CheckPermissionWithRationaleAdapter("如果您拒绝了权限，您将无法登录",
                        new Runnable() {
                            @Override
                            public void run() {
                                //retry
                                readPhoneState(permissionResult);
                            }
                        }) {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }
                });
    }

    /**
     * 定位
     */
    public static void takeLocation(PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                        Manifest.permission.READ_PHONE_STATE
                ),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
//                        ToastUtil.show(BaseApplication.sApplication,"请手动去设置页打开定位权限!");
                        if (permissionResult != null) {
                            permissionResult.refuse();
                        }
                    }

                });

    }
    /**
     * 后台定位
     */
    public static void takeBackLocation(PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                        Manifest.permission.READ_PHONE_STATE
                ),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
//                        ToastUtil.show(BaseApplication.sApplication,"请手动去设置页打开定位权限!");
                        if (permissionResult != null) {
                            permissionResult.refuse();
                        }
                    }

                });

    }

    public static void write(PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
//                        ToastUtils.show("请手动去设置页打开定位权限!");
                    }

                });

    }

    /**
     * 读写权限
     */
    public static void readWrite(final PermissionResult permissionResult) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        if (permissionResult != null) {
                            permissionResult.ok();
                        }
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
//                        ToastUtils.show("请手动去设置页打开读写权限!");
                    }

                });

    }

}
