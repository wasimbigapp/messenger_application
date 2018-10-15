package messenger.messenger.messanger.messenger.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Grid layout manager for app list
 * Created by bedprakash.r on 17/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private final int startOffset;
    private final int endOffset;
    private final int spanCount;
    private int space;

    public GridItemDecoration(int space, int startOffset, int endOffset, int spanCount) {
        this.space = space;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        if (position < startOffset || position >= parent.getAdapter().getItemCount() - endOffset) {
            return;
        }
        if (isLeftMostView(position, startOffset, spanCount)) {
            outRect.left = space;
        } else if (isRightMostView(position, startOffset, spanCount)) {
            outRect.right = space;
        }
    }

    private boolean isLeftMostView(int position, int startOffset, int spanCount) {
        return (position - startOffset) % spanCount == 0;
    }

    private boolean isRightMostView(int position, int startOffset, int spanCount) {
        return (position - startOffset) % spanCount == spanCount - 1;
    }

}
