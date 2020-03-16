package com.speedata.xu.myapplication.print.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.speedata.xu.myapplication.R;

import java.util.Set;


/**
 * @author xuyan
 */
public class BluetoothDeviceList extends Activity {
	private static final String TAG = "DeviceListActivity";
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	public static String EXTRA_RE_PAIR = "re_pair";
	public static String EXTRA_DEVICE_NAME = "device_name";

	// Member fields
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ListView pairedListView;
	private Button scanButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		setTitle(R.string.select_device);

		setResult(Activity.RESULT_CANCELED);

		initView();
	}

	private void initView() {
		scanButton = findViewById(R.id.button_scan);
		scanButton.setOnClickListener(v -> {
			doDiscovery();
			v.setEnabled(false);
		});

		Button backButton = findViewById(R.id.button_bace);
		backButton.setOnClickListener(v -> finish());


		mPairedDevicesArrayAdapter = new ArrayAdapter<>(this,
				R.layout.device_item);

		pairedListView = findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + " ( "
						+ getResources().getText(R.string.has_paired) + " )"
						+ "\n" + device.getAddress());
			}
		}
	}

	@Override
	protected void onStop() {
		if (mBtAdapter != null && mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		this.unregisterReceiver(mReceiver);
		super.onStop();
	}

	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
		super.onResume();
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery()
	{
		Log.d(TAG, "doDiscovery()");

		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		mPairedDevicesArrayAdapter.clear();
		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	private void returnToPreviousActivity(String address, boolean repair, String name) {
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		Intent intent = new Intent();
		intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
		intent.putExtra(EXTRA_RE_PAIR, repair);
		intent.putExtra(EXTRA_DEVICE_NAME, name);

		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private OnItemClickListener mDeviceClickListener = (av, v, arg2, arg3) -> {

		String info = ((TextView) v).getText().toString();
		String address = info.substring(info.length() - 17);
		String name = info.substring(0,info.length() - 17);
		returnToPreviousActivity(address, false,name);
	};



	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				String itemName = device.getName()
						+ " ( "
						+ getResources()
								.getText(
										device.getBondState() == BluetoothDevice.BOND_BONDED ? R.string.has_paired
												: R.string.not_paired) + " )"
						+ "\n" + device.getAddress();

				mPairedDevicesArrayAdapter.remove(itemName);
				mPairedDevicesArrayAdapter.add(itemName);
				pairedListView.setEnabled(true);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mPairedDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.none_found).toString();
					mPairedDevicesArrayAdapter.add(noDevices);
					pairedListView.setEnabled(false);
				}
				scanButton.setEnabled(true);
			}
		}
	};

}
