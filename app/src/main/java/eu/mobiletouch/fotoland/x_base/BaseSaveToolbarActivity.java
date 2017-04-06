package eu.mobiletouch.fotoland.x_base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import eu.mobiletouch.fotoland.R;

public abstract class BaseSaveToolbarActivity extends BaseToolbarActivity {

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_with_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item != null) {
                        if (R.id.action_save == item.getItemId()) {
                            onSaveMenuItemAction();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    protected abstract void onSaveMenuItemAction();
}
