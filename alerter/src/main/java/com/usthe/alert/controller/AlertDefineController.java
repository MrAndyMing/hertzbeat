package com.usthe.alert.controller;

import com.usthe.alert.pojo.entity.AlertDefine;
import com.usthe.alert.service.AlertDefineService;
import com.usthe.common.entity.dto.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Map;

import static com.usthe.common.util.CommonConstants.MONITOR_NOT_EXIST_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 告警定义管理API
 * @author tom
 * @date 2021/12/9 10:32
 */
@Api(tags = "告警定义管理API")
@RestController
@RequestMapping(path = "/alert/define", produces = {APPLICATION_JSON_VALUE})
public class AlertDefineController {

    @Autowired
    private AlertDefineService alertDefineService;

    @PostMapping
    @ApiOperation(value = "新增告警定义", notes = "新增一个告警定义")
    public ResponseEntity<Message<Void>> addNewAlertDefine(@Valid @RequestBody AlertDefine alertDefine) {
        // 校验请求数据
        alertDefineService.validate(alertDefine, false);
        alertDefineService.addAlertDefine(alertDefine);
        return ResponseEntity.ok(new Message<>("Add success"));
    }

    @PutMapping
    @ApiOperation(value = "修改告警定义", notes = "修改一个已存在告警定义")
    public ResponseEntity<Message<Void>> modifyAlertDefine(@Valid @RequestBody AlertDefine alertDefine) {
        // 校验请求数据
        alertDefineService.validate(alertDefine, true);
        alertDefineService.modifyAlertDefine(alertDefine);
        return ResponseEntity.ok(new Message<>("Modify success"));
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询告警定义", notes = "根据告警定义ID获取告警定义信息")
    public ResponseEntity<Message<AlertDefine>> getAlertDefine(
            @ApiParam(value = "告警定义ID", example = "6565463543") @PathVariable("id") long id) {
        // 获取监控信息
        AlertDefine alertDefine = alertDefineService.getAlertDefine(id);
        Message.MessageBuilder<AlertDefine> messageBuilder = Message.builder();
        if (alertDefine == null) {
            messageBuilder.code(MONITOR_NOT_EXIST_CODE).msg("AlertDefine not exist.");
        } else {
            messageBuilder.data(alertDefine);
        }
        return ResponseEntity.ok(messageBuilder.build());
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除告警定义", notes = "根据告警定义ID删除告警定义,告警定义不存在也是删除成功")
    public ResponseEntity<Message<Void>> deleteAlertDefine(
            @ApiParam(value = "告警定义ID", example = "6565463543") @PathVariable("id") long id) {
        // 删除告警定义不存在或删除成功都返回成功
        alertDefineService.deleteAlertDefine(id);
        return ResponseEntity.ok(new Message<>("Delete success"));
    }

    @PostMapping(path = "/{alertId}/monitors")
    @ApiOperation(value = "应用告警定义与监控关联", notes = "应用指定告警定义与监控关联关系")
    public ResponseEntity<Message<Void>> applyAlertDefineMonitorsBind(
            @ApiParam(value = "告警定义ID", example = "6565463543") @PathVariable("alertId") long alertId,
            @RequestBody Map<Long, String> monitorMap) {
        alertDefineService.applyBindAlertDefineMonitors(alertId, monitorMap);
        return ResponseEntity.ok(new Message<>("Apply success"));
    }

}
