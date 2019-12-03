package com.ms;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BhbiApplicationTests {

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private FormService formService;
	@Autowired
	private ManagementService managementService;

	@Test
	public void test3() {
		String taskId = "137514";
		List<String> outcome = new ArrayList<>();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 取出任务定义的KEY
		String taskDefinitionKey = task.getTaskDefinitionKey();
		// 找出流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 根据流程定义ID找到流程定义对象
		ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		// 取出流程定义的KEY
		String key = processDefinition.getKey();

		// 通过流程定义ID找到BPM的对象
		BpmnModel bpmnModel = this.repositoryService.getBpmnModel(processDefinitionId);
		// 根据流程定义KEY找到从bpmnModel里面找出流程定义
		Process process = bpmnModel.getProcessById(key);
		// 取出流程定义对象里面的所有节点
		Collection<FlowElement> flowElements = process.getFlowElements();
		for (FlowElement flowElement : flowElements) {
			// 找出里面的UserTask节点
			if (flowElement instanceof UserTask) {
				UserTask userTask = (UserTask) flowElement;
				//System.out.println(userTask.getId());
				if (userTask.getId().equals(taskDefinitionKey)) {
					// 取出出口连线信息
					List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
					for (SequenceFlow sequenceFlow : outgoingFlows) {
						System.out.println(sequenceFlow.getName());
					}
					break;
				}
			}
		}

		System.out.println("success");
	}

}
