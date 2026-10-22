package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.crlink.smartpark.contract.dto.Extension;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.mphandler.FastjsonFilterNullTypeHandler;
import com.crlink.smartpark.core.mphandler.HouseMessageListTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "con_contract", autoResultMap = true)
@ApiModel(value="Contract对象", description="合同")
public class Contract implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 合同编号
     */
    private String no;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 园区Id
     */
    private String parkOrgId;

    private String parkOrgName;
    /**
     * 总面积
     */
    private Double totalArea;
    /**
     * 经办人id，多个时,分隔
     */
    private String employeeId;

    /**
     * 合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史
     */
    private Integer attributes;

    /**
     * 合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；
     */
    private Integer type;

    /**
     * 合同开始日期
     */
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    private LocalDate endDate;

    /**
     * 签约日期
     */
    private LocalDate contractedDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 甲方公司id
     */
    private String companyIdA;

    /**
     * 甲方统一社会信用代码
     */
    private String creditCodeA;

    /**
     * 甲方名称
     */
    private String nameA;

    /**
     * 甲方联系人
     */
    private String contactA;

    /**
     * 甲方联系电话
     */
    private String contactTelA;

    /**
     * 甲方地址
     */
    private String addressA;

    /**
     * 乙方公司id
     */
    private String customerIdB;

    /**
     * 乙方企业名称
     */
    private String nameB;

    /**
     * 乙方统一社会信用代码
     */
    private String creditCodeB;

    /**
     * 乙方联系人
     */
    private String contactB;

    /**
     * 乙方联系电话
     */
    private String contactTelB;

    /**
     * 乙方地址
     */
    private String addressB;

    /**
     * 附件key，逗号隔开
     */
    private String attachmentKey;

    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    private Integer state;

    /**
     * 当前流程节点
     */
    private String processNo;

    /**
     * 扩展字段json字符串(原合同编号:originContractNo、原合同id:originContractId、变更说明:changeExplanation、变更类型:changeType)
     */
    @TableField(typeHandler = FastjsonFilterNullTypeHandler.class)
    private Extension extension;
    @TableField(typeHandler = HouseMessageListTypeHandler.class)
    private List<HouseMessage> houseMessages;

    /**
     * 退约日期
     */
    private LocalDate withdrawDate;

    /**
     * 退约原因
     */
    private String withdrawReason;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态
     */
    private Boolean deleted;

}
