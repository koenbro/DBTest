package com.koenbro.android.dbtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This is an adapter for showing the contents of the ArrayList with all objects of the
 * class Project. Uses the layout {@code row}. This is different from the {@link
 * ProjectCRUD ProjectDBAdaptor} which handles the SQLite
 * database read, write operations.
 *
 * @see ProjectCRUD
 */
class ProjectAdapter extends ArrayAdapter<Project> {

    private final Context context;
    private final ArrayList<Project> mProjectArrayList;

    /**
     * constructor uses {@code ArrayList<Project>} and the layout {@code row}
     *
     * @param context
     * @param mProjectArrayList This ArrayList is obtained form the databases using the method
     *                          getAllRecords() from ProjectCRUD
     */
    public ProjectAdapter(Context context, ArrayList<Project> mProjectArrayList) {
        super(context, R.layout.row, mProjectArrayList);
        this.context = context;
        this.mProjectArrayList = mProjectArrayList;
    }

    /**
     * inflates the layout {@code row}. The construct
     * {@code mProjectArrayList.get(position).getCompleted()} gets the position of each item, and
     * uses getters to retrieve some info from the object. Very clever!
     *
     * @param position
     * @param convertView
     * @param parent
     * @return View  A row with a label and a text under it.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        String valueText = String.valueOf(mProjectArrayList.get(position).getCompleted()) +
                "% completed";
        TextView label = (TextView) rowView.findViewById(R.id.label);
        TextView value = (TextView) rowView.findViewById(R.id.value);
        label.setText(mProjectArrayList.get(position).getName());
        value.setText(valueText);
        return (rowView);
    }
}
