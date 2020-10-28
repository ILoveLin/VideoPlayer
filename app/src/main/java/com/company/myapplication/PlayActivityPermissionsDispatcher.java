package com.company.myapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

import permissions.dispatcher.GrantableRequest;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * LoveLin
 * <p>
 * Describe
 */
public final class PlayActivityPermissionsDispatcher {
    private static final int REQUEST_SHOTIMAGE = 0;

    private static final String[] PERMISSION_SHOTIMAGE = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private static GrantableRequest PENDING_SHOTIMAGE;

    private static final int REQUEST_STARTGIF = 1;

    private static final String[] PERMISSION_STARTGIF = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private PlayActivityPermissionsDispatcher() {
    }

    static void startGifWithPermissionCheck(@NonNull PlayActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_STARTGIF)) {
//            target.startGif();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_STARTGIF)) {
                target.showRationaleForCamera(new PlayActivityStartGifPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_STARTGIF, REQUEST_STARTGIF);
            }
        }
    }

    static void shotImageWithPermissionCheck(@NonNull PlayActivity target, View v) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOTIMAGE)) {
            target.shotImage(v);
        } else {
            PENDING_SHOTIMAGE = new PlayActivityShotImagePermissionRequest(target, v);
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOTIMAGE)) {
                target.showRationaleForCamera(PENDING_SHOTIMAGE);
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOTIMAGE, REQUEST_SHOTIMAGE);
            }
        }
    }

    static void onRequestPermissionsResult(@NonNull PlayActivity target, int requestCode,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STARTGIF:
                if (PermissionUtils.verifyPermissions(grantResults)) {
//                    target.startGif();
                } else {
                    target.showDeniedForCamera();
                }
                break;
            case REQUEST_SHOTIMAGE:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    if (PENDING_SHOTIMAGE != null) {
                        PENDING_SHOTIMAGE.grant();
                    }
                } else {
                    target.showDeniedForCamera();
                }
                PENDING_SHOTIMAGE = null;
                break;
            default:
                break;
        }
    }

    private static final class PlayActivityStartGifPermissionRequest implements PermissionRequest {
        private final WeakReference<PlayActivity> weakTarget;

        private PlayActivityStartGifPermissionRequest(@NonNull PlayActivity target) {
            this.weakTarget = new WeakReference<PlayActivity>(target);
        }

        @Override
        public void proceed() {
            PlayActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_STARTGIF, REQUEST_STARTGIF);
        }

        @Override
        public void cancel() {
            PlayActivity target = weakTarget.get();
            if (target == null) return;
            target.showDeniedForCamera();
        }
    }

    private static final class PlayActivityShotImagePermissionRequest implements GrantableRequest {
        private final WeakReference<PlayActivity> weakTarget;

        private final View v;

        private PlayActivityShotImagePermissionRequest(@NonNull PlayActivity target,
                                                                View v) {
            this.weakTarget = new WeakReference<PlayActivity>(target);
            this.v = v;
        }

        @Override
        public void proceed() {
            PlayActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOTIMAGE, REQUEST_SHOTIMAGE);
        }

        @Override
        public void cancel() {
            PlayActivity target = weakTarget.get();
            if (target == null) return;
            target.showDeniedForCamera();
        }

        @Override
        public void grant() {
            PlayActivity target = weakTarget.get();
            if (target == null) return;
            target.shotImage(v);
        }
    }

}
