package com.crlink.smartpark.visitor.controller.mobile;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.vo.CompanyClientInfoVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.NoticeDTO;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordVO;
import com.crlink.smartpark.visitor.service.AccessPolicyService;
import com.crlink.smartpark.visitor.service.NoticeService;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import com.crlink.smartpark.visitor.service.mobile.VisitorCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/mobile/visitor/mobile")
@Api(tags = "移动端访客--公用接口")
public class VisitorCommonController {

    @Resource
    private VisitorCommonService visitorCommonService;

    @Resource
    private AccessPolicyService accessPolicyService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private VisitorTypeService visitorTypeService;

    /**
     * 预约详情
     *
     * @param id
     */
    @GetMapping("/appointmentDetail/{id}")
    @ApiOperation(value = "预约详情")
    public ResponseResult<MobileVisitorRecordVO> appointmentDetail(@PathVariable String id) {
        MobileVisitorRecordVO mobileVisitorRecordVO = visitorCommonService.appointmentDetail(id);
        return ResponseResult.success(mobileVisitorRecordVO);
    }

    /**
     * 查询当前企业宣传信息
     */
    @GetMapping("/curCompanyPublicInfo/{companyId}")
    @ApiOperation(value = "查询当前企业宣传信息")
    public ResponseResult<CompanyClientInfoVO> getCurCompanyPublicInfo(@PathVariable String companyId) {
        CompanyClientInfoVO companyClientInfoVO = visitorCommonService.getCurCompanyPublicInfo(companyId);
        return ResponseResult.success(companyClientInfoVO);
    }

    /**
     * 通过园区ID查询来访须知详情
     *
     * @param parkOrgId
     */
    @GetMapping("/noticeByParkOrg/{parkOrgId}")
    @ApiOperation(value = "通过园区ID查询来访须知详情")
    public ResponseResult<NoticeDTO> getNoticeByParkOrgId(@PathVariable String parkOrgId) {
        NoticeDTO noticeDTO = noticeService.getNoticeByParkOrgId(parkOrgId);
        return ResponseResult.success(noticeDTO);
    }

    @ApiOperation(value = "根据入驻企业获取访客类型")
    @GetMapping("/listCurCompanyVisitorType/{companyId}")
    public ResponseResult<List<DictVO>> listVisitorTypeByCompany(@PathVariable String companyId) {
        List<DictVO> result = visitorTypeService.listVisitorTypeByCompany(companyId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "根据入驻企业获取访客策略")
    @GetMapping("/listCurCompanyAccessPolicy/{companyId}")
    public ResponseResult<List<DictVO>> listCurCompanyAccessPolicy(@PathVariable String companyId) {
        List<DictVO> result = accessPolicyService.listAccessPolicyByCompany(companyId);
        return ResponseResult.success(result);
    }


}
