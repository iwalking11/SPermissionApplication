package com.example.iwalk.spermissionapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends SPermissionActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_call).setOnClickListener(this);
        findViewById(R.id.btn_contacts).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:

                //6.0权限适配
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askCameraPermission();
                } else {
                    Intent intent = new Intent(); //调用照相机
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }

                break;

            case R.id.btn_call:

                //6.0权限适配
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askCallPermission();
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }

                break;

            case R.id.btn_contacts:

                //6.0权限适配
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askLocationPermission();
                } else {
                    Toast.makeText(MainActivity.this, "定位权限开启成功", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    /**
     * 请求定位权限需要对MIUI手机单独适配
     */
    private void askLocationPermission() {
        requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionHandler() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onGranted() {
                if ("Xiaomi".equals(Build.MANUFACTURER)) {
                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, getApplicationInfo().uid, getPackageName());
                    if (checkOp == AppOpsManager.MODE_ALLOWED) {
                        Toast.makeText(MainActivity.this, "定位权限开启成功", Toast.LENGTH_SHORT).show();
                    } else if (checkOp == AppOpsManager.MODE_IGNORED) {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("权限申请")
                                .setMessage("请在设置-应用-权限中开启定位权限，以保证功能的正常使用")
                                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);

                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .setCancelable(false)
                                .show();
                    } else {
                    }
                } else {
                    Toast.makeText(MainActivity.this, "定位权限开启成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDenied() {
            }

            @Override
            public boolean onNeverAsk() {
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("权限申请")
                        .setMessage("请在设置-应用-权限中开启定位权限，以保证功能的正常使用")
                        .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show();

                return true;
            }

        });
    }

    private void askCameraPermission() {
        requestPermission(new String[]{Manifest.permission.CAMERA}, new PermissionHandler() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(); //调用照相机
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }

            @Override
            public void onDenied() {
            }

            @Override
            public boolean onNeverAsk() {
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("权限申请")
                        .setMessage("请在设置-应用-权限中开启相机权限，以保证功能的正常使用")
                        .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show();

                return true;
            }

        });
    }

    private void askCallPermission() {
        requestPermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionHandler() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onGranted() {
//                LogUtil.e(Build.MANUFACTURER + "***************************");
//                if ("Xiaomi".equals(Build.MANUFACTURER)) {
//                    AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
//                    int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, getApplicationInfo().uid, getPackageName());
//                    if (checkOp == AppOpsManager.MODE_IGNORED) {
//                        new android.app.AlertDialog.Builder(ShopAuthFirstStep.this)
//                                .setTitle("权限申请")
//                                .setMessage("请在设置-应用-权限中开启拨打电话权限，以保证功能的正常使用")
//                                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                        intent.setData(uri);
//                                        startActivity(intent);
//
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("取消", null)
//                                .setCancelable(false)
//                                .show();
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001688681"));
//                        if (ActivityCompat.checkSelfPermission(ShopAuthFirstStep.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        ShopAuthFirstStep.this.startActivity(intent);
//                    }
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4001688681"));
//                    if (ActivityCompat.checkSelfPermission(ShopAuthFirstStep.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    ShopAuthFirstStep.this.startActivity(intent);
//                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                MainActivity.this.startActivity(intent);
            }

            @Override
            public void onDenied() {
            }

            @Override
            public boolean onNeverAsk() {
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("权限申请")
                        .setMessage("请在设置-应用-权限中开启拨打电话权限，以保证功能的正常使用")
                        .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .show();

                return true;
            }

        });
    }
}
