package com.hdweiss.morgand.gui.outline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hdweiss.morgand.Application;
import com.hdweiss.morgand.R;
import com.hdweiss.morgand.data.dao.OrgNode;
import com.hdweiss.morgand.data.dao.OrgNodeRepository;
import com.hdweiss.morgand.events.DataUpdatedEvent;
import com.hdweiss.morgand.gui.edit.BaseEditFragment;
import com.hdweiss.morgand.gui.edit.EditHeadingFragment;
import com.hdweiss.morgand.gui.edit.controller.AddController;
import com.hdweiss.morgand.gui.edit.controller.BaseEditController;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.otto.Subscribe;

public class OutlineFragment extends Fragment {

    protected OutlineListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getBus().register(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        Application.getBus().unregister(this);
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outline, container, false);
        listView = (OutlineListView) rootView.findViewById(R.id.list);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setActivity(getActivity());
        refreshView();
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null)
            return;

        listView.loadState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listView.saveState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.outline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int position = listView.getCheckedItemPosition();
        switch(item.getItemId()) {
            case android.R.id.home:
                if (listView != null)
                    listView.collapseCurrent();
                break;

            case R.id.add_child:
                showAddDialog(position);
                break;

            case R.id.delete:
                showDeleteDialog(position);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showDeleteDialog(int position) {
        if (position < 0)
            return;

        OrgNode node = (OrgNode) listView.getAdapter().getItem(position);
        OrgNodeRepository.delete(node);
        Application.getBus().post(new DataUpdatedEvent());
    }

    private void showAddDialog(int position) {
        if (position < 0)
            return;

        OrgNode node = (OrgNode) listView.getAdapter().getItem(position);
        BaseEditController editController = new AddController(node, OrgNode.Type.Headline);
        BaseEditFragment fragment = new EditHeadingFragment(editController);
        fragment.show(getActivity());
    }


    @Subscribe
    public void refreshView(DataUpdatedEvent event) {
        refreshView();
    }
    protected void refreshView() {
        listView.refresh();
    }
}
