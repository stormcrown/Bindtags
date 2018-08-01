package com.jar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mexxen.barcode.BarcodeEvent;
import com.mexxen.barcode.BarcodeListener;
import com.mexxen.barcode.BarcodeUtils;

import java.util.LinkedList;
import java.util.Queue;

/***/
public class BarcodeManager {
    private BarcodeListener mBarcodeListener;
    private BarcodeEvent mBarcodeEvent;
    private Context mContext;
    private static Queue<String> allResults = new LinkedList();
    public BarcodeManager(Context context) {
        this.mContext = context;
        this.mBarcodeEvent = new BarcodeEvent("SCANNER_IDLE");
        IntentFilter scannerFilter = new IntentFilter("com.android.server.scannerservice.broadcast");
    }

    private void addBarcodeResult(String code) {
        allResults.add(code);
    }

    private void notifyListener(BarcodeEvent barcodeEvent) {
        this.mBarcodeListener.barcodeEvent(barcodeEvent);
    }

    public String getBarcode() {
        String code = null;
        if (!allResults.isEmpty()) {
            code = (String)allResults.poll();
        }

        return code;
    }

    public void startScanner() {
        this.scanStart();
    }

    public void stopScanner() {
        this.scanDispose();
    }

    public void dismiss() {
       // this.mContext.unregisterReceiver(this.mScannerRunningReceiver);
    }

    public void scanStart() {
        this.mBarcodeEvent.setOrder("SCANNER_START");
        this.notifyListener(this.mBarcodeEvent);
        BarcodeUtils.sendStaticBroadcast(this.mContext, "android.intent.action.SCANNER_BUTTON_DOWN");
    }

    public void scanDispose() {
        this.mBarcodeEvent.setOrder("SCANNER_STOP");
        this.notifyListener(this.mBarcodeEvent);
        BarcodeUtils.sendStaticBroadcast(this.mContext, "android.intent.action.SCANNER_BUTTON_UP");
    }

    public void addListener(BarcodeListener bl) {
        this.mBarcodeListener = bl;
    }

    public void removeListener(BarcodeListener bl) {
        this.mBarcodeListener = null;
    }
}
