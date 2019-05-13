package com.example.user.print.Interface;

import com.example.user.print.util.NoScanResultException;

public interface ScanResultReceiver {
    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}
