//package com.example.pathfinderapp;
//
//import android.graphics.Canvas;
//import androidx.recyclerview.widget.ItemTouchHelper.Callback;
//import android.view.View;
//
//import com.example.pathfinderapp.Adapters.AdapterTour;
//
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
//
///**
// * Created by ravi on 29/09/17.
// */
//
//public class SwipeHandler extends ItemTouchHelper.SimpleCallback {
//    private RecyclerItemTouchHelperListener listener;
//
//    public SwipeHandler(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
//        super(dragDirs, swipeDirs);
//        this.listener = listener;
//    }
//
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        return false;
//    }
//
//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        return makeMovementFlags(0, LEFT);
//    }
//
//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        super.onSelectedChanged(viewHolder, actionState);
//
//        if (viewHolder != null) {
//            final View foregroundView = ((AdapterTour.ViewHolderItem) viewHolder).viewForeground;
//
//            getDefaultUIUtil().onSelected(foregroundView);
//        }
//    }
//
//    @Override
//    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
//                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                                int actionState, boolean isCurrentlyActive) {
//        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//
//        final View foregroundView = ((AdapterTour.ViewHolderItem) viewHolder).viewForeground;
//        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
//                actionState, isCurrentlyActive);
//    }
//
//    @Override
//    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
//
//        final View foregroundView = ((AdapterTour.ViewHolderItem) viewHolder).viewForeground;
//        getDefaultUIUtil().clearView(foregroundView);
//    }
//
//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView,
//                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                            int actionState, boolean isCurrentlyActive) {
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        final View foregroundView = ((AdapterTour.ViewHolderItem) viewHolder).viewForeground;
//        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
//    }
//
//    @Override
//    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
//        return super.convertToAbsoluteDirection(flags, layoutDirection);
//    }
//
//    public interface RecyclerItemTouchHelperListener {
//        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
//    }
//}
