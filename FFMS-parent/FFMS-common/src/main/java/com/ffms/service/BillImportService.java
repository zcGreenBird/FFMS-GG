package com.ffms.service;

import com.ffms.domain.Bill;
import lombok.Data;

import java.io.File;
import java.io.IOException;

/**
 * 账单导入接口  excl表格实现
 */
public interface  BillImportService {
    /**
     *
     * @param file
     * @param num
     * @return
     */
    public int  importExcel(File file,int num,int userId) throws IOException;
}
