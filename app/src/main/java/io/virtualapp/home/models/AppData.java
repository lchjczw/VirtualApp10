package io.virtualapp.home.models;
import android.graphics.drawable.Drawable;
public interface AppData {
    boolean isLoading();
    boolean isFirstOpen();
    Drawable getIcon();
    String getName();
    boolean canReorder();
    boolean canLaunch();
    boolean canDelete();
    boolean canCreateShortcut();
}
