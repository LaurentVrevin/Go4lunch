package viewmodels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationPermissionViewModel extends ViewModel implements EasyPermissions.PermissionCallbacks {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationPermissionCallback permissionCallback;
    private MutableLiveData<Boolean> locationPermissionGranted = new MutableLiveData<>();



    public void setLocationPermissionCallback(LocationPermissionCallback callback) {
        permissionCallback = callback;
    }

    public boolean hasLocationPermission(Context context) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        return EasyPermissions.hasPermissions(context, perms);
    }
    public LiveData<Boolean> getLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean granted) {
        locationPermissionGranted.setValue(granted);
    }

    public void requestLocationPermission(Activity activity) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        EasyPermissions.requestPermissions(activity, "Location permission is required to access your current location.", LOCATION_PERMISSION_REQUEST_CODE, perms);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && permissionCallback != null) {
            permissionCallback.onPermissionGranted();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && permissionCallback != null) {
            permissionCallback.onPermissionDenied();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public interface LocationPermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }
}
