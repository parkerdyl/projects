package com2027.cw.dp00405.vsa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    AdapterBook adapterBook;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.recyclerBook);

        DatabaseClass receive = new DatabaseClass(null, null, BookmarkFragment.this);
        receive.execute(((NavigationActivity) getActivity()).getEmail() + "#BOOKGET");

        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Log.e("log_swiped_left", "Swiped left");
                    //delete bookmark
                    DatabaseClass receive = new DatabaseClass(null, null, BookmarkFragment.this);
                    receive.execute(((NavigationActivity) getActivity()).getEmail() + "," + urls.get(position) + "#BOOKREMOVE");

                    ((NavigationActivity) getActivity()).replaceFragment(new BookmarkFragment(), null, "BookmarkFragment");

                    break;
                case ItemTouchHelper.RIGHT:
                    //open webpage or more info
                    ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Website");
                    WebsiteFragment website = new WebsiteFragment();
                    //Log.e("log_url", descriptions.get(position));
                    website.setURL(urls.get(position));
                    ((NavigationActivity) getActivity()).replaceFragment(website, null, "WebsiteFragment");
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            RectF dest;

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                Paint p = new Paint();
                if (dX > 0) {
                    // website
                    p.setARGB(255, 0, 0, 255);
                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX + itemView.getLeft(),
                            (float) itemView.getBottom(), p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.website_image);
                    dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, dest, p);
                } else {
                    // delete
                    p.setARGB(255, 255, 0, 0);
                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.bin_image);
                    dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, dest, p);
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void initRecyclerView() {
        adapterBook = new AdapterBook(getActivity(), titles, descriptions);
        recyclerView.setAdapter(adapterBook);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setBookmarks(ArrayList<String[]> bookmarks) {
        for (String[] bookmark : bookmarks) {
            Log.e("log_boobobobok", bookmark[0] + " " + bookmark[1] + " " + bookmark[2]);
            titles.add(bookmark[0]);
            descriptions.add(bookmark[1]);
            urls.add(bookmark[2]);
        }
        initRecyclerView();
    }

}
