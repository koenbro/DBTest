package com.koenbro.android.dbtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * <b>IN PROGRESS. DOES NOT WORK.</b>
 * If you have many tables/data objects, it might make sense to abstract them out into a DataAdapter
 * like this, and then extend it, overloading the {@link
 * com.koenbro.android.dbtest.DataAdapter#valueText(int) valueText} field only. Each data object
 * extends a DataObject superclass. <br>
 * If you only have 2-3 tables, it may be just easier to skip the DataAdapter and DataObject and
 * just manually code each object and its adapter. Judgment call. <p></p>
 * Otherwise, this is an adapter superclass. Its subclasses <b>display</b> the contents of the ArrayList
 * with all objects of one table (like the class Project). Uses the layout {@code row}.
 *
 * @see com.koenbro.android.dbtest.DataObject
 */
abstract class DataAdapter extends ArrayAdapter<DataObject> {
    private final Context context;
    private final ArrayList<DataObject> mDataArrayList;

    /**
     * constructor uses {@code ArrayList<Project>} and the layout {@code row}
     *
     * @param context
     * @param mDataArrayList This ArrayList is obtained form the databases using the method
     *                       getAllRecords() from ProjectCRUD
     */
    public DataAdapter(Context context, ArrayList<DataObject> mDataArrayList) {
        super(context, R.layout.row, mDataArrayList);
        this.context = context;
        this.mDataArrayList = mDataArrayList;
    }

    //private final ArrayList<Project> mProjectArrayList;
    protected abstract String valueText();
//    public DataAdapter(Context context, ArrayList<Project> mDataArrayList) {
//        super(context, R.layout.row, mProjectArrayList);
//        this.context = context;
//        this.mProjectArrayList = mProjectArrayList;
//    }

    /**
     * inflates the layout {@code row}. The construct
     * {@code mDataArrayList.get(position).getCompleted()} gets the position of each item, and
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
        TextView label = (TextView) rowView.findViewById(R.id.label);
        TextView value = (TextView) rowView.findViewById(R.id.value);
        label.setText(labelText(position));
        value.setText(valueText());
        return (rowView);
    }

    public String labelText(int position) {
        return mDataArrayList.get(position).getName();
    }

//    public String valueText(int position){
//        return "id: "+ String.valueOf(mDataArrayList.get(position).getId());
//    }

}
