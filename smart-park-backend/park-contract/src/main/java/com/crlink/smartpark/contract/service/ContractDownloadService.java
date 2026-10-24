package com.crlink.smartpark.contract.service;

import com.crlink.smartpark.contract.model.request.ContractGenerateReqDTO;

/**
 * @ClassName: ContractDownloadService
 * @Description: 合同下载
 * @Version 1.0
 */
public interface ContractDownloadService {


    /**
     * 合同生成
     *
     * @param reqDTO
     * @return
     */
    Boolean generateContract(ContractGenerateReqDTO reqDTO);


}
