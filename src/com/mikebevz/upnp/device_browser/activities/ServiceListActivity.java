package com.mikebevz.upnp.device_browser.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.mikebevz.upnp.R;
import com.mikebevz.upnp.UpnpBrowserApp;
import com.mikebevz.upnp.device_browser.ServiceListAdapter;
import com.mikebevz.upnp.tasks.GetDeviceServicesTask;
import com.mikebevz.upnp.tasks.OnDeviceServiceList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.ServiceList;

/**
 * @author mikebevz
 */
public class ServiceListActivity extends Activity implements OnDeviceServiceList, OnItemClickListener {

private ServiceListAdapter adapter;
private ProgressDialog dialog;

@Override
public void onCreate(Bundle icicle) {
  super.onCreate(icicle);
  setContentView(R.layout.list_view);


  Bundle bundle = getIntent().getExtras();
  int position = bundle.getInt("device");
  Device device = (Device) ((UpnpBrowserApp) getApplication()).getDeviceList().get(position);
  this.setTitle("Services at " + device.getFriendlyName());

  GetDeviceServicesTask getServiceTask = new GetDeviceServicesTask();
  getServiceTask.setOnDeviceServiceListHandler(this);
  getServiceTask.execute(device);


  ListView listView = (ListView) findViewById(R.id.list_view);
  adapter = new ServiceListAdapter(this);
  listView.setAdapter(adapter);
  listView.setOnItemClickListener(this);

}

public void OnDeviceServiceListPreExecute() {
  dialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);
  dialog.setCancelable(true);
}

public void OnDeviceServiceListSuccess(ServiceList sList) {
  Log.d("ServiceList", String.valueOf(sList.size()));
  ((UpnpBrowserApp) getApplication()).setServiceList(sList);
  adapter.setServices(sList);
  adapter.notifyDataSetChanged();
  if (dialog.isShowing()) {
    dialog.dismiss();
  }
}

public void onItemClick(AdapterView<?> av, View view, int position, long id) {

  Intent intent = new Intent(this, ServiceDetailsActivity.class);
  intent.putExtra("position", position);
  startActivity(intent);

}
}
