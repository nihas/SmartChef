package com.nihas.smart.chef.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by Nihas on 17-03-2016.
 */
public class FabBehaviour extends FloatingActionButton.Behavior {

    public FabBehaviour() {
    }

    public FabBehaviour(Context context, AttributeSet attributeSet) {
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout) {
            return super.onDependentViewChanged(parent, child, dependency);
        } else if(dependency instanceof AppBarLayout) {
            this.updateFabVisibility(parent, (AppBarLayout)dependency, child);
        }

        return false;
    }

    private boolean updateFabVisibility(CoordinatorLayout parent, AppBarLayout appBarLayout, FloatingActionButton child) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
        if(lp.getAnchorId() != appBarLayout.getId()) {
            return false;
        } else {

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int point = child.getTop() - params.topMargin;
            try {
                Method method = AppBarLayout.class.getDeclaredMethod("getMinimumHeightForVisibleOverlappingContent");
                method.setAccessible(true);
                if(point <= (int) method.invoke(appBarLayout)) {
                    child.hide();
                } else {
                    child.show();
                }
                return true;
            } catch (Exception e) {
                return true;
            }
        }
    }
}
